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

package com.izforge.izpack.extension;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.izforge.izpack.util.i18n.DomTreeLocalizer;
import com.izforge.izpack.util.i18n.Messages;

/**
 * An extension can provides several things to build installers such as panels,
 * new libraries and so on. It can also provide other classes. For instance it
 * could bring the user interface to set up a panel it provides in a tool aimed
 * at creating installers.
 *
 * For each feature provided by an extension, there are some attributes to use:
 * <ul>
 * <li>an identifier, for instance <code>FunkyPanel</code></li>
 * <li>a specification of what it specifies, for instance if it is an
 * installer panel, it could be <code>com.izforge.izpack.installer.panel</code>
 * </li>
 * <li>the fully qualified class name of what is provided</li>
 * <li>at least one Jar file to load</li>
 * </ul>
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class Extension
{

  /** The URI of the extension descriptor DTD. */
  public static String DTD_URI = "http://www.izforge.com/izpack/dtd/extension-description.dtd";

  /** The extension descriptor DOM tree. */
  protected Document extensionDescriptor = null;

  /** The JXPath context used to retrievd the various data. */
  protected JXPathContext jxpath = null;

  /** The extension header map (lazzy instanciation). */
  protected Map headerMap = null;

  /** What the extension provides (lazzy instanciation). */
  protected Set whatSet = null;

  /** The internal logger. */
  private Logger logger = Logger.getLogger(Extension.class.getName());

  /** Our messages. */
  private Messages msg = Messages.newMessages("com.izforge.izpack.extension.messages");

  /**
   * Constructs a new extension from a descriptor file. This is equivalent to
   * calling: <code><pre>
   *  (...)
   *  Extension ext = new Extension(descrIn, null);
   *  (...)
   * </pre></code>
   *
   * @param descriptor The descriptor input stream.
   * @throws ParserConfigurationException Thrown if the XML parser has not been
   * set up properly.
   * @throws SAXException Thrown if an error occurs while parsing the XML
   * descriptor.
   * @throws IOException Thrown when an I/O problem occurs.
   */
  public Extension(InputStream descriptor) throws ParserConfigurationException,
      SAXException, IOException
  {
    this(descriptor, null);
  }

  /**
   * Constructs a new extension from a descriptor file.
   *
   * @param descriptor The descriptor input stream.
   * @param messages The i18n messages input stream or <code>null</code> if
   * there are no strings to translate in the descriptor file. See <code>com.izforge.izpack.util.i18n.Messages</code>.
   * @throws ParserConfigurationException Thrown if the XML parser has not been
   * set up properly.
   * @throws SAXException Thrown if an error occurs while parsing the XML
   * descriptor.
   * @throws IOException Thrown when an I/O problem occurs.
   */
  public Extension(InputStream descriptor, InputStream messages)
      throws ParserConfigurationException, SAXException, IOException
  {
    // Factory setup
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);

    // Builder setup
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(new EntityResolver() {
      public InputSource resolveEntity(String publicId, String systemId)
          throws SAXException, IOException
      {
        if (systemId.equals(DTD_URI))
        {
          return new InputSource(
              Extension.class
                  .getResourceAsStream("/com/izforge/izpack/extension/extension-description.dtd"));
        }
        else
        {
          return null;
        }
      }
    });
    builder.setErrorHandler(new ErrorHandler() {

      public void error(SAXParseException exception) throws SAXException
      {
        logger.log(Level.SEVERE, msg.getString("Extension.XmlParseError"), exception);
      }

      public void fatalError(SAXParseException exception) throws SAXException
      {
        logger.log(Level.SEVERE, msg.getString("Extension.XmlParseError"), exception);
      }

      public void warning(SAXParseException exception) throws SAXException
      {
        logger.log(Level.WARNING, msg.getString("Extension.XmlParseWarn"), exception);
      }
    });

    // Parsing
    extensionDescriptor = builder.parse(descriptor);
    if (messages != null)
    {
      DomTreeLocalizer trans = new DomTreeLocalizer(Messages
          .newMessages(messages));
      trans.translate(extensionDescriptor);
    }
    jxpath = JXPathContext.newContext(extensionDescriptor);
  }

  /**
   * Returns a map containing the extension header data. It has the following
   * keys:
   * <ul>
   * <li><code>name</code>: the extension name</li>
   * <li><code>description</code>: the extension description</li>
   * <li><code>url</code>: the extension provider URL (as a string)</li>
   * <li><code>license</code>: a short name for the license of the
   * extension (for instance MIT, BSD, ...)</li>
   * <li><code>authors</code>: a <code>java.util.List</code> of strings
   * specifing the extension authors.</li>
   * </ul>
   */
  public Map getExtensionHeader()
  {
    if (headerMap != null)
    {
      return headerMap;
    }
    TreeMap map = new TreeMap();

    map.put("name", jxpath.getValue("/extension/header/name/text()"));
    map.put("description", jxpath
        .getValue("/extension/header/description/text()"));
    map.put("url", jxpath.getValue("/extension/header/url/text()"));
    map.put("license", jxpath.getValue("/extension/header/license/text()"));

    Iterator it = jxpath.iterate("/extension/header/author/text()");
    ArrayList list = new ArrayList();
    while (it.hasNext())
    {
      list.add(it.next());
    }
    map.put("authors", list);

    headerMap = Collections.unmodifiableMap(map);
    return headerMap;
  }

  /**
   * Returns a set of strings representing the kind of features that the
   * extension provides (<code>what</code> XML tag).
   *
   * @return What the extension provides.
   */
  public Set getWhatIsProvided()
  {
    if (whatSet != null)
    {
      return whatSet;
    }
    Set set = new TreeSet();

    Iterator it = jxpath.iterate("/extension/provides/what/text()");
    while (it.hasNext())
    {
      set.add(it.next());
    }

    whatSet = Collections.unmodifiableSet(set);
    return whatSet;
  }

  /**
   * Computes the identifiers that match a requested provided feature.
   *
   * @param what The feature to get the identifiers for.
   * @return The matched identifiers.
   */
  public Set getIdentsFor(String what)
  {
    Set idents = new TreeSet();

    String path = "/extension/provides[what/text() = '" + what
        + "']/ident/text()";
    Iterator it = jxpath.iterate(path);
    while (it.hasNext())
    {
      idents.add(it.next());
    }

    return idents;
  }

  /**
   * Returns what the extension provides for the specified identifier.
   *
   * @param id The identifier to look for.
   * @return A map containing what the extension provides for <code>id</code>,
   * where the keys are:
   * <ul>
   * <li><code>class</code>: the name of the class providing the feature
   * </li>
   * <li><code>jars</code>: a <code>List</code> of strings containing the
   * names of the Jar files to load for the feature.
   * </ul>
   * If <code>id</code> is not a valid identifier, then <code>null</code>
   * is returned.
   */
  public Map getWhatProvides(String id)
  {
    String path = "/extension/provides[ident/text() = '" + id
        + "']/class/text()";
    Object res = jxpath.getValue(path);
    if (res == null)
    {
      return null;
    }

    Map map = new TreeMap();
    map.put("class", res);

    path = "/extension/provides[ident/text() = '" + id + "']/jar/text()";
    Iterator it = jxpath.iterate(path);
    List jars = new ArrayList();
    while (it.hasNext())
    {
      jars.add(it.next());
    }
    map.put("jars", jars);

    return map;
  }

}
