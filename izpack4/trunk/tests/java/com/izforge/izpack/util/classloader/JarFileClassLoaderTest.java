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

package com.izforge.izpack.util.classloader;

import java.io.InputStream;
import java.util.jar.JarFile;

import junit.framework.TestCase;

/**
 *
 */
public class JarFileClassLoaderTest extends TestCase
{
  protected String jarPath = "com/izforge/izpack/util/classloader/foo.jar";

  protected JarFile jar = null;

  protected JarFileClassLoader classLoader = null;

  /**
   * Constructor for JarFileClassLoaderTest.
   *
   * @param arg0
   */
  public JarFileClassLoaderTest(String arg0)
  {
    super(arg0);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    if (jar == null)
    {
      jar = new JarFile(jarPath, true);
      classLoader = new JarFileClassLoader(jar);
    }
  }

  public void testScanEntries()
  {
    TestCase.assertTrue(classLoader.entries.containsKey("/foo/bar/Foo.java"));
    TestCase.assertTrue(classLoader.entries.containsKey("foo.bar.Foo"));
    TestCase.assertTrue(classLoader.entries.containsKey("foo.bar.Foo$Bar"));
  }

  public void testFindClass()
  {
    try
    {
      Class cl = Class.forName("foo.bar.Foo", true, classLoader);
      Object instance = cl.newInstance();
      String res = (String) cl.getMethod("getHelloWorld", null).invoke(
          instance, null);
      TestCase.assertTrue(res.equals("Hello World"));
    }
    catch (Exception e)
    {
      TestCase.fail(e.toString());
    }
  }

  public void testGetResourceAsStream()
  {
    InputStream in = classLoader.getResourceAsStream("/foo/bar/Foo.java");
    TestCase.assertNotNull(in);

    in = classLoader.getResourceAsStream("plop");
    TestCase.assertNull(in);
  }

}
