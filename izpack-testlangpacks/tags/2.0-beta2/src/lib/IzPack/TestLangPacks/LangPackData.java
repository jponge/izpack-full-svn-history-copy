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

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Contains language pack data.
 */
public class LangPackData
{
   /**
    * Result in text format.
    */
   protected String txtResult = null;
   /**
    * Result in binary format.
    */
   protected Hashtable<String,ArrayList<LanguageItem>> binResult = null;

   /**
    * List of base language items.
    */
   protected ArrayList<LanguageItem> languageItemsBase;

   /**
    * List of language items to be tested.
    */
   protected ArrayList<LanguageItem> languageItemsTest;

   /**
    * Default constructor of the class.
    */
   public LangPackData ()
   {
      this.languageItemsBase = null;
      this.languageItemsTest = null;
   }

   /**
    * Constructor of the class with parameters.
    *
    * @param languageItemsBase   Base language items.
    * @param languageItemsTest   Language items to be tested.
    */
   public LangPackData (ArrayList<LanguageItem> languageItemsBase, ArrayList<LanguageItem> languageItemsTest)
   {
      this.languageItemsBase = languageItemsBase;
      this.languageItemsTest = languageItemsTest;
   }

   /**
    * Gets the result in binary format. Can return multiple keys with values
    * or only one value. In this case to keep routines simple use &quot;result&quot;
    * string for the key and put one value into HashTable's value.
    *
    * <br><br><b>Examples</b>
    * <pre>
    * // Create desired object.
    * MissingIds missingIds = new MissingIds(langItemsBase, langItemsTest);
    * // Get results.
    * Hashtable<String,ArrayList<LanguageItem>> result = missingIds.getResult();
    * </pre>
    *
    * <b>Get result with key</b>
    * <pre>
    * ArrayList<LanguageItem> resultItems = result.get("result");
    * </pre>
    *
    * <b>Get result without knowing anything about the key</b>
    * <pre>
    * ArrayList<LanguageItem> resultItems2 = result.values().iterator().next();
    * </pre>
    *
    * @note If this method is unimplemented, exception LangPackException is
    *       thrown always.
    *
    * See the code for example to make your own implementation.
    */
   public Hashtable<String,ArrayList<LanguageItem>> getResult () throws LangPackException
   {
      /*
      if (this.binResult != null)
      {
         return this.binResult;
      }

      // Make here your own implementation for toString.
      // ...

      return this.binResult;
      */
      throw new LangPackException("This must be implemented in inherited class!");
   }

   /**
    * Gets the result in text format.
    *
    * @note If this method is unimplemented, exception LangPackException is
    *       thrown always.
    *
    * See the code for example to make your own implementation.
    */
   public String getResultString () throws LangPackException
   {
      /*
      if (this.txtResult != null)
      {
         return this.txtResult;
      }

      // Make sure we have binary results.
      getResult();
      this.txtResult = "";

      // Make here your own implementation for toString.
      // ...

      return this.txtResult;
      */
      throw new LangPackException("This must be implemented in inherited class!");
   }
}
