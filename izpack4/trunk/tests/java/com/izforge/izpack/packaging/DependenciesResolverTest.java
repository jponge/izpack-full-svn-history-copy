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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.TestCase;

/**
 *
 */
public class DependenciesResolverTest extends TestCase
{

  protected Map packages;

  protected DependenciesResolver resolver;

  /**
   * Constructor for DependenciesResolverTest.
   * @param arg0
   */
  public DependenciesResolverTest(String arg0)
  {
    super(arg0);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    packages = new TreeMap();

    // root -> {dep1, dep2}
    Package root = new Package("root");
    Package dep1 = new Package("dep1");
    Package dep2 = new Package("dep2");
    root.addDependency("dep1");
    root.addDependency("dep2");

    // dep1 -> {sub}
    Package sub = new Package("sub");
    dep1.addDependency("sub");

    packages.put("root", root);
    packages.put("dep1", dep1);
    packages.put("dep2", dep2);
    packages.put("sub", sub);

    resolver = new DependenciesResolver(packages);
  }

  public void testResolve()
  {
    // Normal dependencies tests

    Set deps = resolver.resolve("sub");
    TestCase.assertEquals(0, deps.size());

    deps = resolver.resolve("dep1");
    TestCase.assertEquals(1, deps.size());
    TestCase.assertTrue(deps.contains("sub"));

    deps = resolver.resolve("root");
    TestCase.assertEquals(3, deps.size());
    TestCase.assertTrue(deps.contains("dep1"));
    TestCase.assertTrue(deps.contains("dep2"));
    TestCase.assertTrue(deps.contains("sub"));

    // Let's put some loops

    Package p = (Package)packages.get("sub");
    p.addDependency("root");
    p = (Package)packages.get("dep1");
    p.addDependency("dep2");
    p = (Package)packages.get("dep2");
    p.addDependency("dep1");

    deps = resolver.resolve("root");
    TestCase.assertEquals(3, deps.size());
    TestCase.assertTrue(deps.contains("dep1"));
    TestCase.assertTrue(deps.contains("dep2"));
    TestCase.assertTrue(deps.contains("sub"));

    deps = resolver.resolve("dep1");
    TestCase.assertEquals(3, deps.size());
    TestCase.assertTrue(deps.contains("root"));
    TestCase.assertTrue(deps.contains("dep2"));
    TestCase.assertTrue(deps.contains("sub"));
  }

}
