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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A mock source entry, used for testing purposes.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class SourceEntryMock implements ISourceEntry
{

  /** The size. */
  protected long size;

  /** The parent entry. */
  protected ISourceEntry parent = null;

  /** The children list. */
  protected List children = new ArrayList();

  /**
   * Constructs a new instance.
   *
   * @param size The size for this dumb entry.
   */
  SourceEntryMock(long size)
  {
    this.size = size;
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getName()
   */
  public String getName()
  {
    return null;
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getSize()
   */
  public long getSize()
  {
    long total = size;
    Iterator it = children.iterator();
    while (it.hasNext())
    {
      ISourceEntry entry = (ISourceEntry)it.next();
      total += entry.getSize();
    }
    return total;
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getMetaData(java.lang.String)
   */
  public Object getMetaData(String id)
  {
    return null;
  }

  /**
   * Addes a child.
   *
   * @param child The new child
   */
  public void addChild(ISourceEntry child)
  {
    children.add(child);
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getParent()
   */
  public ISourceEntry getParent()
  {
    return parent;
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getChildrenCount()
   */
  public int getChildrenCount()
  {
    return children.size();
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getChildAt(int)
   */
  public ISourceEntry getChildAt(int index)
  {
    return (ISourceEntry)children.get(index);
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#providesStream()
   */
  public boolean providesStream()
  {
    return false;
  }

  /**
   * @see com.izforge.izpack.packaging.ISourceEntry#getStream()
   */
  public InputStream getStream()
  {
    return null;
  }

}
