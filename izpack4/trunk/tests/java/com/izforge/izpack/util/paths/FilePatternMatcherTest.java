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

package com.izforge.izpack.util.paths;

import junit.framework.TestCase;

/**
 *
 */
public class FilePatternMatcherTest extends TestCase
{

  /**
   * Constructor for FilePatternMatcherTest.
   * @param arg0
   */
  public FilePatternMatcherTest(String arg0)
  {
    super(arg0);
  }

  public void testMatches()
  {
    FilePatternMatcher matcher = new FilePatternMatcher("foo");
    TestCase.assertTrue(matcher.matches("foo"));
    TestCase.assertFalse(matcher.matches("_foo"));

    matcher = new FilePatternMatcher("foo/bar");
    TestCase.assertTrue(matcher.matches("foo/bar"));
    TestCase.assertFalse(matcher.matches("foo/_bar"));

    matcher = new FilePatternMatcher("*.plop");
    TestCase.assertTrue(matcher.matches("foo.plop"));
    TestCase.assertFalse(matcher.matches("foo.bar"));
    TestCase.assertFalse(matcher.matches("a/foo.bar"));

    matcher = new FilePatternMatcher("foo/*.plop");
    TestCase.assertTrue(matcher.matches("foo/bar.plop"));
    TestCase.assertFalse(matcher.matches("foo/bar.bar"));
    TestCase.assertFalse(matcher.matches("bar/bar.plop"));

    matcher = new FilePatternMatcher("**");
    TestCase.assertTrue(matcher.matches("foo"));
    TestCase.assertTrue(matcher.matches("foo/bar"));
    TestCase.assertTrue(matcher.matches("foo/bar.foo"));

    matcher = new FilePatternMatcher("foo/**");
    TestCase.assertTrue(matcher.matches("foo/bar"));
    TestCase.assertTrue(matcher.matches("foo/bar/bar"));
    TestCase.assertFalse(matcher.matches("bar/foo"));

    matcher = new FilePatternMatcher("foo/**/b/*.bar");
    TestCase.assertTrue(matcher.matches("foo/abc/def/b/toto.bar"));
    TestCase.assertTrue(matcher.matches("foo/b/toto.bar"));
    TestCase.assertFalse(matcher.matches("foo.bar"));
    TestCase.assertFalse(matcher.matches("foo/b/foo"));
  }

}
