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

package com.izforge.izpack.util.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import com.izforge.izpack.util.i18n.Messages;

/**
 * A class loader that allows loading the classes and resources contained in a
 * Jar archive.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class JarFileClassLoader extends ClassLoader
{

  /** Our internal logger. */
  private Logger logger = Logger.getLogger(JarFileClassLoader.class.getName());

  /** Our messages. */
  private Messages msg = Messages
      .newMessages("com.izforge.izpack.util.classloader.messages");

  /** The Jar input stream. */
  protected JarFile jarFile;

  /** Holds the Jar entries. */
  protected Map entries = new HashMap();

  /** Holds the instances of the loaded classes. */
  protected Map classes = new HashMap();

  /**
   * Constructs a new loader, making the Jar source content available to the
   * JVM. The Jar entries are scanned in this constructor.
   *
   * @param jarFile The Jar file to load.
   * @throws IOException Thrown if an I/O error occurs.
   */
  public JarFileClassLoader(JarFile jarFile) throws IOException
  {
    super();
    this.jarFile = jarFile;

    scanEntries();
  }

  /**
   * Scans the Jar file entries and builds a cache from it.
   *
   * @throws IOException Thrown if an I/O error occurs.
   */
  protected void scanEntries() throws IOException
  {
    ZipEntry entry;
    String name;

    Enumeration en = jarFile.entries();
    while (en.hasMoreElements())
    {
      entry = (ZipEntry) en.nextElement();
      if (entry.isDirectory())
      {
        continue;
      }
      name = entry.getName();
      if (name.endsWith(".class"))
      {
        // Path -> fully qualified class name
        name = name.substring(0, name.length() - 6);
        name = name.replace('/', '.');
      }
      else
      {
        name = "/" + name;
      }
      entries.put(name, entry);
    }
  }

  /**
   * Tries to load a given class name from the Jar.
   *
   * @param name The class name to look for.
   * @return The loaded class, if any.
   * @throws ClassNotFoundException Thrown if the class could not be found.
   * @see java.lang.ClassLoader#findClass(java.lang.String)
   */
  protected Class findClass(String name) throws ClassNotFoundException
  {
    // Do we have it in cache ?
    if (classes.containsKey(name)) { return (Class) classes.get(name); }

    // Can we have it ?
    if (!entries.containsKey(name)) { return super.findClass(name); }

    // Lazzy instanciation
    try
    {
      ZipEntry entry = (ZipEntry) entries.get(name);
      InputStream in = jarFile.getInputStream(entry);
      int size = (int) entry.getSize();
      byte[] buffer = new byte[size];
      in.read(buffer);

      Class cl = defineClass(name, buffer, 0, size);
      classes.put(name, cl);
      return cl;
    }
    catch (ClassFormatError err)
    {
      logger.log(Level.SEVERE, msg
          .getString("JarFileClassLoader.InvalidClass")
          + jarFile.getName(), err);
    }
    catch (IOException err)
    {
      logger.log(Level.SEVERE, msg
          .getString("JarFileClassLoader.IOProblem")
          + jarFile.getName(), err);
    }
    return super.findClass(name);
  }

  /**
   * Returns an input stream for the given resource or <code>null</code> if
   * none matches the requested name.
   *
   * @param name The resource name.
   * @return The input stream for the resource or <code>null</code>.
   * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String name)
  {
    if (!entries.containsKey(name)) { return null; }

    // Quite simple
    InputStream in;
    try
    {
      in = jarFile.getInputStream((ZipEntry) entries.get(name));
      return in;
    }
    catch (IOException err)
    {
      logger.log(Level.SEVERE, msg
          .getString("JarFileClassLoader.IOProblem")
          + jarFile.getName(), err);
    }
    return null;
  }

}
