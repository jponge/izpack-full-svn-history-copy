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

package com.izforge.izpack.util.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds the i18n messages strings for a given locale and a
 * properties file. This class is helper around <code>ResourceBundle</code>.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class Messages
{
  /** The internal resource bundle. */
  protected ResourceBundle bundle;

  /** Our internal logger. */
  private Logger logger = Logger.getLogger(Messages.class.getName());

  /**
   * Constructs a new messages holder.
   *
   * @param bundle The resource bundle to use.
   */
  protected Messages(ResourceBundle bundle)
  {
    this.bundle = bundle;
  }

  /**
   * Creates a new <code>Messages</code> instance.
   *
   * @param stream The stream to load the messages from.
   * @throws IOException Thrown when a I/O error occurs.
   */
  public static Messages newMessages(InputStream stream) throws IOException
  {
    PropertyResourceBundle bundle = new PropertyResourceBundle(stream);
    return new Messages(bundle);
  }

  /**
   * Creates a new <code>Messages</code> instance
   *
   * @param baseName The resource base name (for instance <code>foo.bar.i18n</code>).
   * @return The new <code>Messages</code> instance.
   */
  public static Messages newMessages(String baseName)
  {
    return Messages.instanciateMessages(baseName, Locale.getDefault(),
        Messages.class.getClassLoader());
  }

  /**
   * Creates a new <code>Messages</code> instance
   *
   * @param baseName The resource base name (for instance <code>foo.bar.i18n</code>).
   * @param locale The locale to use while locating the resource containing the
   * translations.
   * @return The new <code>Messages</code> instance.
   */
  public static Messages newMessages(String baseName, Locale locale)
  {
    return Messages.instanciateMessages(baseName, locale, Messages.class
        .getClassLoader());
  }

  /**
   * Creates a new <code>Messages</code> instance
   *
   * @param baseName The resource base name (for instance <code>foo.bar.i18n</code>).
   * @param locale The locale to use while locating the resource containing the
   * translations.
   * @param loader The class loader containing the resources containing the
   * translations.
   * @return The new <code>Messages</code> instance.
   */
  public static Messages newMessages(String baseName, Locale locale,
      ClassLoader loader)
  {
    return Messages.instanciateMessages(baseName, locale, loader);
  }

  /**
   * Creates a new <code>Messages</code> instance
   *
   * @param baseName The resource base name (for instance <code>foo.bar.i18n</code>).
   * @param locale The locale to use while locating the resource containing the
   * translations.
   * @param loader The class loader containing the resources containing the
   * translations.
   * @return The new <code>Messages</code> instance.
   */
  private static Messages instanciateMessages(String baseName, Locale locale,
      ClassLoader loader)
  {
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, loader);
    return new Messages(bundle);
  }

  /**
   * Gets the message for a given key.
   *
   * @param key The key for the desired message.
   * @return The message, or <code>!key!</code> if there is no matching
   * string for <code>key</code>.
   */
  public String getString(String key)
  {
    String value;
    try
    {
      value = bundle.getString(key);
    }
    catch (MissingResourceException err)
    {
      logger.log(Level.WARNING, "No message matches " + key);
      value = "!" + key + "!";
    }
    return value;
  }
}
