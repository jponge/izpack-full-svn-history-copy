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
 * A package contains both sub-packages and sources. A package forms a tree
 * where the sources are the leafs. Dependencies between packages are expressed
 * by the packages identifiers strings.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class Package
{

  /** The package identifier. */
  protected String id;

  /** The package description. */
  protected String description;

  /** The sub-packages. */
  protected List subPackages = new ArrayList();

  /** The sources. */
  protected List sources = new ArrayList();

  /** The dependencies. */
  protected List dependencies = new ArrayList();

  /**
   * Constructs a new package.
   *
   * @param id The package identifier.
   */
  public Package(String id)
  {
    super();

    this.id = id;
  }

  /**
   * Returns the package identifier.
   *
   * @return The package identifier.
   */
  public String getId()
  {
    return id;
  }

  /**
   * Changes the package identifier.
   *
   * @param id The new identifier.
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Returns the description.
   *
   * @return Returns the description.
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description The description to set.
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Computes the package size, in bytes.
   *
   * @return The package size.
   */
  public long getSize()
  {
    long bytes = 0;
    Iterator it = subPackages.iterator();
    while (it.hasNext())
    {
      Package p = (Package) it.next();
      bytes += p.getSize();
    }
    it = sources.iterator();
    while (it.hasNext())
    {
      Source s = (Source) it.next();
      bytes += s.getSize();
    }
    return bytes;
  }

  /**
   * Returns the number of sub-packages.
   *
   * @return The nunber of sub-packages.
   */
  public int getSubPackagesCount()
  {
    return subPackages.size();
  }

  /**
   * Adds a new sub-packages.
   *
   * @param p The package to add.
   */
  public void addSubPackage(Package p)
  {
    subPackages.add(p);
  }

  /**
   * Clears the sub-packages.
   */
  public void clearSubPackages()
  {
    subPackages.clear();
  }

  /**
   * Removes a sub-package at a specified index.
   *
   * @param index The index of the sub-package to remove.
   * @return The removed package.
   */
  public Package removeSubPackageAt(int index)
  {
    return (Package) subPackages.remove(index);
  }

  /**
   * Gets the sub-package at a specified index.
   *
   * @param index The index of the desired package.
   * @return The package.
   */
  public Package getSubPackageAt(int index)
  {
    return (Package) subPackages.get(index);
  }

  /**
   * Returns the number of sources.
   *
   * @return The number of sources.
   */
  public int getSourcesCount()
  {
    return sources.size();
  }

  /**
   * Adds a new source.
   *
   * @param source The source to add.
   */
  public void addSource(Source source)
  {
    sources.add(source);
  }

  /**
   * Clears the sources.
   */
  public void clearSources()
  {
    sources.clear();
  }

  /**
   * Removes a source at a specified index.
   *
   * @param index The index of the source to remove.
   * @return The removed source.
   */
  public Source removeSourceAt(int index)
  {
    return (Source) sources.remove(index);
  }

  /**
   * Gets the source at a specified index.
   *
   * @param index The index of the desired source.
   * @return The source.
   */
  public Source getSourceAt(int index)
  {
    return (Source) sources.get(index);
  }

  /**
   * Returns the number of dependencies.
   *
   * @return The number of dependencies.
   */
  public int getDependenciesCount()
  {
    return dependencies.size();
  }

  /**
   * Adds a new dependency for this package.
   *
   * @param id The package identifier.
   *
   */
  public void addDependency(String id)
  {
    dependencies.add(id);
  }

  /**
   * Removes all the dependencies for this package.
   */
  public void clearDependencies()
  {
    dependencies.clear();
  }

  /**
   * Removes the dependency at a given index.
   *
   * @param index The dependency index.
   * @return The removed dependency identifier.
   *
   */
  public String removeDependencyAt(int index)
  {
    return (String) dependencies.remove(index);
  }

  /**
   * Gets the dependency at a specified index.
   *
   * @param index The dependency index.
   * @return The dependency identifier.
   */
  public String getDependencyAt(int index)
  {
    return (String) dependencies.get(index);
  }

}
