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

import java.io.InputStream;

/**
 * This interface describes a source entry, ie a node of a hierarchy of entries.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public interface ISourceEntry
{
  /**
   * Returns the entry name.
   *
   * @return The name of the entry.
   */
  public String getName();

  /**
   * Gives the size in bytes for the entry.
   *
   * @return The entry size.
   */
  public long getSize();

  /**
   * Accesses a meta-data value.
   *
   * @param id The meta-data identifier.
   * @return The meta-data value or <code>null</code> if there is no entry for
   * this meta-data identifier.
   */
  public Object getMetaData(String id);

  /**
   * Returns the parent entry.
   *
   * @return The parent entry or <code>null</code> for a root of hierarchy.
   */
  public ISourceEntry getParent();

  /**
   * Returns the number of children for this entry.
   *
   * @return The number of children.
   */
  public int getChildrenCount();

  /**
   * Gets the children at the specified index.
   *
   * @param index The 0-based index of the requested children.
   * @return The children at index or <code>null</code> if the index is invalid.
   */
  public ISourceEntry getChildAt(int index);

  /**
   * Tells wether the entry provides a stream or not. For instance, an entry
   * representing a directory won't provide a stream.
   *
   * @return Wether a stream for this entry is available or not.
   */
  public boolean providesStream();

  /**
   * Gets the input stream for this entry.
   *
   * @return The input stream or <code>null</code> if no stream is available.
   */
  public InputStream getStream();
}
