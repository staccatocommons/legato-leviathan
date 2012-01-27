/*
 Copyright (c) 2012, The Staccato-Commons Team

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; version 3 of the License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 */
package net.sf.staccatocommons.leviathan;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.NullWriter;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;

import ar.com.zauber.commons.web.transformation.processors.DocumentProvider;

/**
 * @author flbulgarelli
 * 
 */
public abstract class AbstractTidyDocumentProvider implements DocumentProvider {

  public Document parse(InputStream input) {
    return newTidy().parseDOM(input, new NullOutputStream());
  }

  public Document parse(InputSource arg0) {
    return newTidy().parseDOM(arg0.getCharacterStream(), new NullWriter());
  }

  public void serialize(Document arg0, OutputStream arg1) {
    throw new UnsupportedOperationException();
  }

  /**
   * @return
   */
  protected abstract Tidy newTidy();

}
