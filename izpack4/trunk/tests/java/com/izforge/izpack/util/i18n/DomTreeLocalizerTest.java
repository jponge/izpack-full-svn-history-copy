/*
 * IzPack 4 tests suite
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 *
 * Copyright (c) 2004 Julien Ponge
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.izforge.izpack.util.i18n;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;

/**
 *
 */
public class DomTreeLocalizerTest extends TestCase
{

  public void testTranslate() throws Exception
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document dom = builder.parse(this.getClass().getResourceAsStream(
        "/com/izforge/izpack/util/i18n/DomTreeLocalizerTest.xml"));
    Messages msg = Messages
        .newMessages("com.izforge.izpack.util.i18n.MessagesTest");

    DomTreeLocalizer localizer = new DomTreeLocalizer(msg);
    localizer.translate(dom);
    JXPathContext jxpath = JXPathContext.newContext(dom);
    String value;

    value = (String)jxpath.getValue("/test/@name");
    TestCase.assertEquals("Foo", value);

    value = (String)jxpath.getValue("/test/element/@name");
    TestCase.assertEquals("Foo", value);

    value = (String)jxpath.getValue("/test/element/text()");
    TestCase.assertEquals("Bar", value);
  }

}
