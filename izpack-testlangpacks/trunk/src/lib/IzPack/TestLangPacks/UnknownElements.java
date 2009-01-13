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
 * Finds unknown elements in XML file.
 */
public class UnknownElements extends LangPackData
{
   private LangPack langPackTest = null;

   /**
    * Default constructore of the class.
    */
   private UnknownElements()
   {
      super();
   }

   /**
    * Constructor of the class with parameter(s).
    *
    * @param langItemsTest Language items to be tested.
    */
   public UnknownElements(LangPack langPackTest)
   {
      super();

      this.langPackTest = langPackTest;
   }

   /**
    * Returns unknown elements. Actual result is found inside LanguageItem object,
    * which contains "String[] unknownAttributes".
    * @see LangPackData.getResult()
    */
   @Override
   public Hashtable<String, ArrayList<LanguageItem>> getResult()
   {
      if (this.binResult != null)
      {
         return this.binResult;
      }

      // Gets unknown elements as an array of String.
      String[] unknownElements = this.langPackTest.getUnknownElements();

      // Create LanguageItem object for unknown elements.
      LanguageItem item = new LanguageItem("unknown elements", null);
      // Set elements into the object.
      item.setUnknownAttributes(unknownElements);

      // Create ArrayList object.
      ArrayList<LanguageItem> list = new ArrayList<LanguageItem>();
      // Add LanguageItem object into the ArrayList object.
      list.add(item);

      // Create return object which is introduced in method.
      this.binResult = new Hashtable<String, ArrayList<LanguageItem>>();
      // Put the array list into the object.
      this.binResult.put("result", list);

      // => Quite complicated way to return result. See the usage in UnknownElements.getResultString().

      return this.binResult;
   }

   /**
    * @see LangPackData.getResultString()
    */
   @Override
   public String getResultString()
   {
      if (this.txtResult != null)
      {
         return this.txtResult;
      }

      this.txtResult = "";

      getResult();

      /*
       * The usage of the result is coded here as a same way as it should do in
       * calling routine.
       */
      
      // Gets the result.
      ArrayList<LanguageItem>  resultItems = this.binResult.values().iterator().next();
      // Gets the language item.
      LanguageItem item = resultItems.get(0);
      // Gets unknown elements.
      // NOTE: Both unknownAttributes and unknownElements are arrays of String!
      String[] unknownElements = item.getUnknownAttributes();

      if (unknownElements.length == 0)
      {
         this.txtResult = "   (none)";
      }
      else
      {
         for (String elem : unknownElements)
         {
            this.txtResult += "   <" + elem + " ... />";
         }
      }

      return this.txtResult;
   }
}
