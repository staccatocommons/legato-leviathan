package net.sf.staccatocommons.leviathan.control;

import static net.sf.staccatocommons.lambda.Lambda.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.w3c.dom.Document;

import ar.com.zauber.commons.web.transformation.processors.DocumentProvider;
import ar.com.zauber.leviathan.api.URIFetcherHttpResponse;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

import net.sf.staccatocommons.defs.function.Function;
import net.sf.staccatocommons.lang.SoftException;
import net.sf.staccatocommons.lang.function.AbstractFunction;
import net.sf.staccatocommons.leviathan.DocumentContentTypes;
import net.sf.staccatocommons.restrictions.Constant;
import net.sf.staccatocommons.restrictions.check.NonNull;
import net.sf.staccatocommons.restrictions.processing.ForceRestrictions;

public class Fetch {

  public static <A> Function<Document, A> parse(final Unmarshaller unmarshaller, Class<A> clazz) {
    return new AbstractFunction<Document, A>() {
      @Override
      @ForceRestrictions
      public A apply(@NonNull Document arg0) {
        try {
          return (A) unmarshaller.unmarshal(new DOMSource(arg0));
        } catch (Exception e) {
          throw SoftException.soften(e);
        }
      }
    };
  }

  @Constant
  public static Function<URIFetcherResponse, URIFetcherHttpResponse> httpResponse() {
    return lambda($(URIFetcherResponse.class).getHttpResponse());
  }

  @Constant
  public static Function<URIFetcherResponse, String> httpResponseContent() {
    return httpResponse().then(new AbstractFunction<URIFetcherHttpResponse, String>() {
      public String apply(URIFetcherHttpResponse arg0) {
        try {
          return (String) FieldUtils.readDeclaredField(arg0, "content", true);
        } catch (IllegalAccessException e) {
          throw SoftException.soften(e);
        }
      }
    });
  }

  @Constant
  public static Function<URIFetcherResponse, InputStream> httpResponseRawContent() {
    return httpResponse().then(lambda($(URIFetcherHttpResponse.class).getRawContent()));
  }

  public static Function<URIFetcherResponse, Document> transformHtmlContent(Resource resource) {
    return tidyContent(DocumentContentTypes.html()).then(xslt(resource));
  }

  public static Function<String, Document> tidy(final DocumentProvider contentType) {
    return new AbstractFunction<String, Document>() {
      public Document apply(String arg0) {
        return contentType.parse(new ByteArrayInputStream(arg0.getBytes()));
      }
    };
  }

  public static Function<URIFetcherResponse, Document> tidyContent(DocumentProvider contentType) {
    return httpResponseContent().then(tidy(contentType));
  }

  public static Function<Document, Document> xslt(final Resource resource) {
    try {
      final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      final Transformer transformer = TransformerFactory.newInstance().newTransformer(
        new StreamSource(resource.getInputStream()));
      return new AbstractFunction<Document, Document>() {
        public Document apply(Document arg0) {
          try {
            Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
            transformer.transform(new DOMSource(arg0), new DOMResult(document));
            return document;
          } catch (Exception e) {
            throw SoftException.soften(e);
          }
        }
      };
    } catch (Exception e) {
      throw SoftException.soften(e);
    }

  }
}
