package net.sf.staccatocommons.leviathan;

import java.io.PrintWriter;

import net.sf.staccatocommons.restrictions.Constant;

import org.apache.commons.io.output.NullWriter;
import org.w3c.tidy.Tidy;

import ar.com.zauber.commons.web.transformation.processors.DocumentProvider;

@SuppressWarnings("serial")
public class DocumentContentTypes {

  @Constant
  public static DocumentProvider html() {
    return new AbstractTidyDocumentProvider() {
      protected Tidy newTidy() {
        return new SilentTidy() {
          {
            setXHTML(true);
            setXmlOut(true);
          }
        };
      }
    };
  }

  @Constant
  public static DocumentProvider xhtml() {
    return new AbstractTidyDocumentProvider() {
      protected Tidy newTidy() {
        return new SilentTidy() {
          {
            setInputEncoding("utf-8");
            setXHTML(true);
            setRawOut(true);
            setXmlTags(true);
          }
        };
      }
    };
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
