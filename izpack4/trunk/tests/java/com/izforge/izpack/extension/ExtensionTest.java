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

package com.izforge.izpack.extension;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 *
 */
public class ExtensionTest extends TestCase
{

  protected Extension extension;

  protected String descriptor = "/com/izforge/izpack/extension/description.xml";

  /**
   * Constructor for ExtensionTest.
   * @param arg0
   */
  public ExtensionTest(String arg0)
  {
    super(arg0);
  }

  public void setUp() throws Exception
  {
    InputStream in = ExtensionTest.class.getResourceAsStream(descriptor);
    extension = new Extension(in);
  }

  public void testGetExtensionHeader()
  {
    Map data = extension.getExtensionHeader();

    TestCase.assertEquals("SampleExtension", data.get("name"));
    TestCase.assertEquals("It rulez.", data.get("description"));
    TestCase.assertEquals("http://www.izforge.com/izpack/", data.get("url"));
    TestCase.assertEquals("MIT", data.get("license"));

    List authors = (List)data.get("authors");
    TestCase.assertEquals("Julien Ponge <julien@izforge.com>", authors.get(0));
    TestCase.assertEquals(
      "Julien Ponge <julien_ponge@yahoo.fr>",
      authors.get(1));
  }

  public void testGetWhatIsProvided()
  {
    Set what = extension.getWhatIsProvided();

    TestCase.assertEquals(1, what.size());
    TestCase.assertTrue(what.contains("com.izforge.izpack.panel"));
  }

  public void testGetIdentsFor()
  {
    Set ids = extension.getIdentsFor("none");
    TestCase.assertEquals(0, ids.size());

    ids = extension.getIdentsFor("com.izforge.izpack.panel");
    TestCase.assertEquals(2, ids.size());
    TestCase.assertTrue(ids.contains("SamplePanel1"));
    TestCase.assertTrue(ids.contains("SamplePanel2"));
  }

  public void testGetWhatProvides()
  {
    Map what = extension.getWhatProvides("SamplePanel1");

    TestCase.assertNotNull(what);
    TestCase.assertEquals(2, what.size());
    TestCase.assertTrue(
      what.get("class").equals("com.izforge.izpack.foo.SamplePanel1"));

    List jars = (List)what.get("jars");
    TestCase.assertEquals(2, jars.size());
    TestCase.assertTrue(jars.get(0).equals("SamplePanel1.jar"));
    TestCase.assertTrue(jars.get(1).equals("FooLib.jar"));
  }

}
