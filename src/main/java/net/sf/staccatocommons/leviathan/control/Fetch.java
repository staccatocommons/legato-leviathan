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

import net.sf.staccatocommons.defs.function.Function;
import net.sf.staccatocommons.lang.SoftException;
import net.sf.staccatocommons.lang.function.AbstractFunction;
import net.sf.staccatocommons.leviathan.DocumentContentTypes;
import net.sf.staccatocommons.restrictions.Constant;
import net.sf.staccatocommons.restrictions.check.NonNull;
import net.sf.staccatocommons.restrictions.processing.EnforceRestrictions;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.w3c.dom.Document;

import ar.com.zauber.commons.web.transformation.processors.DocumentProvider;
import ar.com.zauber.leviathan.api.URIFetcherHttpResponse;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

public class Fetch {

  public static <A> Function<Document, A> parse(final Unmarshaller unmarshaller, Class<A> clazz) {
    return new AbstractFunction.Soft<Document, A>() {
      @Override
      @EnforceRestrictions
      public A softApply(@NonNull Document arg0) throws Exception {
        return (A) unmarshaller.unmarshal(new DOMSource(arg0));
      }
    };
  }

  @Constant
  public static Function<URIFetcherResponse, URIFetcherHttpResponse> httpResponse() {
    return lambda($(URIFetcherResponse.class).getHttpResponse());
  }

  @Constant
  public static Function<URIFetcherResponse, String> httpResponseContent() {
    return httpResponse().then(new AbstractFunction.Soft<URIFetcherHttpResponse, String>() {
      public String softApply(URIFetcherHttpResponse arg0) throws Throwable {
        return arg0.getContentAsString();
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
      return new AbstractFunction.Soft<Document, Document>() {
        public Document softApply(Document arg0) throws Exception {
          Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
          transformer.transform(new DOMSource(arg0), new DOMResult(document));
          return document;
        }
      };
    } catch (Exception e) {
      throw SoftException.soften(e);
    }
  }
}
