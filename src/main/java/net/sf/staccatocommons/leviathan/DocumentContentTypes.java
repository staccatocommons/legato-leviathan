package net.sf.staccatocommons.leviathan;

import java.io.PrintWriter;

import org.apache.commons.io.output.NullWriter;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import ar.com.zauber.commons.web.transformation.processors.DocumentProvider;
import ar.com.zauber.commons.web.transformation.processors.impl.JTidyDocumentProvider;

import net.sf.staccatocommons.restrictions.Constant;

@SuppressWarnings("serial")
public class DocumentContentTypes {

  @Constant
  public static DocumentProvider html() {
    return new JTidyDocumentProvider(new SilentTidy() {
      {
        setCharEncoding(Configuration.UTF8);
        setXHTML(true);
      }
    });
  }

  @Constant
  public static DocumentProvider xhtml() {
    return new JTidyDocumentProvider(new SilentTidy() {
      {
        setCharEncoding(Configuration.UTF8);
        setXHTML(true);
        setRawOut(true);
        setXmlTags(true);
      }
    });
  }

  private static class SilentTidy extends Tidy {
    {
      setErrout(new PrintWriter(NullWriter.NULL_WRITER));
      setQuiet(true);
      setErrfile("foo");
      setOnlyErrors(true);
      setShowWarnings(false);
    }
  }
}
