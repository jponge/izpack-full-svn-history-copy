/*
 * IzPack - Copyright 2001-2009 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2009 Ari Voutilainen
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
package IzPack.TestLangPacks;

import java.io.IOException;

/**
 * Static utility class.
 */
public final class Utils
{
   /**
    * Returns the string made with id and txt.
    * @param key     ID found from 'str' element.
    * @param value   Text value found from 'str' element.
    */
   public static final String makeKeyValueString(String key, String value)
   {
      String msg = "   str ";
      //"   str id=\"" + key + "\" txt=\"" + itemBase.GetValue() + "\"");
      if (key == null || key.equals(""))
      {
         msg = msg + "(id missing!) ";
      }
      else
      {
         msg = msg + "id=\"" + key + "\" ";
      }

      if (value == null || value.equals(""))
      {
         msg = msg + "(txt missing!)";
      }
      else
      {
         msg = msg + "txt=\"" + value + "\"";
      }

      return msg;
   }

   /**
    * Gets last word in ID string.
    * @param idString   ID string where to get last word. Words must be separated by
    *                   full stop '.' .
    * @return           Returns last word in ID string. Returns null if 'idString'
    *                   is empty or 'idString' contains only one word. ID strings
    *                   containing only one word must always be different so it has
    *                   no use to compare them. There must more than 1 word in
    *                   ID string.
    */
   public static final String getLastWord(String idString)
   {
      String strReturn = null;
      String[] sArr = idString.split("\\x2e"); // "."
      int size = sArr.length;
      if (size > 1)
      {
         strReturn = sArr[size - 1];
      }

      return strReturn;
   }

   /**
    * Gets the name and the version of this application.
    * These will be taken from TestLangPacks.jar and from /meta-inf/Manifest.mf file.
    * If not found returns short name and debug version without version number.
    */
   public static String getNameAndVersionInfo()
   {
      Class<IzPack.TestLangPacks.Main> clazz = IzPack.TestLangPacks.Main.class;

      String className = clazz.getSimpleName();
      String classFileName = className + ".class";
      String pathToThisClass = clazz.getResource(classFileName).toString();

      int mark = pathToThisClass.indexOf("!");
      String pathToManifest = pathToThisClass.toString().substring(0, mark + 1);
      pathToManifest += "/META-INF/MANIFEST.MF";

      java.util.jar.Manifest manifest = null;
      String appName = null;
      String version = null;
      try
      {
         // Let's load manifest file.
         manifest = new java.util.jar.Manifest(new java.net.URL(pathToManifest).openStream());
         java.util.jar.Attributes attr = manifest.getMainAttributes();
         // Get application name and version.
         appName = attr.getValue("Implementation-Title");
         version = attr.getValue("Implementation-Version");
      }
      catch (IOException e)
      {
         // What ever exception occures we'll make name and version strings anyway.
         appName = "TestLangPacks";
         version = "for DEBUGGING";
      }
      String retVal = appName + "     version " + version;
      return retVal;
   }
}
