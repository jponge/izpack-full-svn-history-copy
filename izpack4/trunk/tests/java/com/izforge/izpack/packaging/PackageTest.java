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

package com.izforge.izpack.packaging;

import junit.framework.TestCase;

/**
 *
 */
public class PackageTest extends TestCase
{

  /**
   * Constructor for PackageTest.
   * @param arg0
   */
  public PackageTest(String arg0)
  {
    super(arg0);
  }

  public void testGetSize()
  {
    Package root = new Package("root");
    Package subp = new Package("sub");
    root.addSource(SourceTest.createDumbSource("spam"));
    root.addSource(SourceTest.createDumbSource("egg"));
    subp.addSource(SourceTest.createDumbSource("foo"));
    subp.addSource(SourceTest.createDumbSource("bar"));
    root.addSubPackage(subp);

    TestCase.assertEquals(1600, root.getSize());
  }

}
