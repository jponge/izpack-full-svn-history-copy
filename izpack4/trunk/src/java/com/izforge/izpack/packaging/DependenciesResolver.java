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

import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.izforge.izpack.util.i18n.Messages;

/**
 * This class is able to resolve the dependencies for a set of packages.
 * Potential dependencies loops are broken. Anyway, you should not make loops
 * in your dependencies isn't it :-) ?
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class DependenciesResolver
{

  /** The packages map. */
  protected Map packages;

  /** Our logger. */
  private Logger logger = Logger
      .getLogger(DependenciesResolver.class.getName());

  /** Our messages. */
  private Messages msg = Messages
      .newMessages("com.izforge.izpack.packaging.messages");

  /**
   * Constructs a new dependencies resolver from a dictionnary of <code>Package</code>
   * instances where the keys are the packages identifiers.
   *
   * @param packages The packages dictionnary.
   */
  public DependenciesResolver(Map packages)
  {
    this.packages = packages;
  }

  /**
   * Resolves the dependencies for a given package.
   *
   * @param packageId The package identifier.
   * @return A set of the packages identifiers required by <code>packageId</code>.
   */
  public Set resolve(String packageId)
  {
    Set set = new TreeSet();

    // Sanity check
    Package pkg = (Package) packages.get(packageId);
    if (pkg == null)
    {
      logger.log(Level.FINE, packageId
          + msg.getString("DependenciesResolver.NotValidPkg"));
      return set;
    }

    // Exploration
    Stack stack = new Stack();
    stack.push(pkg);
    Package cur;
    Package dep;
    String id;
    int count;
    int i;
    while (!stack.isEmpty())
    {
      cur = (Package) stack.pop();
      count = cur.getDependenciesCount();
      for (i = 0; i < count; ++i)
      {
        dep = (Package) packages.get(cur.getDependencyAt(i));
        if (dep == null)
        {
          logger.log(Level.FINE, cur.getDependencyAt(i)
              + msg.getString("DependenciesResolver.NotValidPkg"));
          continue;
        }
        id = dep.getId();
        if (id != packageId && !set.contains(id))
        {
          set.add(id);
          stack.push(dep);
        }
      }
    }

    return set;
  }
}
