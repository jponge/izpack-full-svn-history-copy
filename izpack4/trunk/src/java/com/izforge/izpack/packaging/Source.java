/*
 * IzPack 4
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 *
 * Copyright (c) 2004 Julien Ponge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.packaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A source contains a set of entries that represent hierarchies. A source can
 * have several hierarchies, ie several roots for entries trees.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class Source
{

  /** The source identifier. */
  protected String sourceId;

  /** The root entries. */
  protected List rootEntries = new ArrayList();

  /**
   * Constructs a new source instance.
   *
   * @param id The source identifier.
   */
  public Source(String id)
  {
    super();
    sourceId = id;
  }

  /**
   * Gets the source identifier.
   *
   * @return The identifier.
   */
  public String getSourceId()
  {
    return sourceId;
  }

  /**
   * Changes the source identifier.
   *
   * @param id The new identifier.
   */
  public void setSourceId(String id)
  {
    sourceId = id;
  }

  /**
   * Computes the source size, in bytes.
   *
   * @return The source size.
   */
  public long getSize()
  {
    long size = 0;
    Iterator it = rootEntries.iterator();
    while (it.hasNext())
    {
      ISourceEntry entry = (ISourceEntry)it.next();
      size += entry.getSize();
    }
    return size;
  }

  /**
   * Gets the number of root entries.
   *
   * @return The number of root entries.
   */
  public int getRootsCount()
  {
    return rootEntries.size();
  }

  /**
   * Returns the root entry at a specific index.
   *
   * @param index The root entry index.
   * @return The entry.
   */
  public ISourceEntry getRootAt(int index)
  {
    return (ISourceEntry)rootEntries.get(index);
  }

  /**
   * Adds a new root entry.
   *
   * @param element The new root entry.
   */
  public void add(ISourceEntry element)
  {
    rootEntries.add(element);
  }

  /**
   * Removes all the root entries.
   */
  public void clear()
  {
    rootEntries.clear();
  }

  /**
   * Removes the source entry at a specified index.
   *
   * @return The removed entry.
   */
  public ISourceEntry remove(int index)
  {
    return (ISourceEntry)rootEntries.remove(index);
  }

}
