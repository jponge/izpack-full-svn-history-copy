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

import junit.framework.TestCase;

/**
 *
 */
public class MessagesTest extends TestCase
{

  private static final String BUNDLE_NAME = "com.izforge.izpack.util.i18n.MessagesTest";

  public void testGetString()
  {
    Messages msg = Messages.newMessages(BUNDLE_NAME);

    TestCase.assertEquals("Foo", msg.getString("test.foo"));
    TestCase.assertEquals("Bar", msg.getString("test.bar"));
    TestCase.assertEquals("!test.fail!", msg.getString("test.fail"));
  }

}
