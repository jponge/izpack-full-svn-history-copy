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
import java.util.Iterator;

/**
 * Finds unknown attributes in XML file.
 */
public class UnknownAttributes extends LangPackData
{
   /**
    * Default constructore of the class.
    */
   public UnknownAttributes ()
   {
      super();
   }

   /**
    * Constructor of the class with parameter(s).
    *
    * @see LangPackData#LangPackData(java.util.ArrayList languageItemsBase, java.util.ArrayList languageItemsTest)
    */
   public UnknownAttributes (ArrayList<LanguageItem> languageItemsBase, ArrayList<LanguageItem> languageItemsTest)
   {
      super(languageItemsBase, languageItemsTest);
   }

   /**
    * Check unknown attributes.
    *
    * @note This will return LanguageItem objects which will contain only Array of
    *       unknown attributes.
    *
    * @see LangPackData#getResult()
    */
   @Override
   public Hashtable<String,ArrayList<LanguageItem>> getResult ()
   {
      if (this.binResult != null)
      {
         return this.binResult;
      }

      binResult = new Hashtable<String,ArrayList<LanguageItem>>();
      binResult.put("result", getUnknownAttributes());
      return this.binResult;
   }

   /**
    * @see LangPackData#getResultString()
    */
   @Override
   public String getResultString ()
   {
      if (this.txtResult != null)
      {
         return this.txtResult;
      }

      // Make sure we have binray results.
      getResult();

      ArrayList<LanguageItem> resultItems = binResult.values().iterator().next();
      if (!resultItems.isEmpty())
      {
         String msg = "";
         
         Iterator<LanguageItem> iter = resultItems.iterator();
         while (iter.hasNext())
         {
            LanguageItem item = iter.next();
            String[] unknownAttrs = item.getUnknownAttributes();
            if (unknownAttrs != null)
            {
               msg = "   Found unknown attributes in\n";
               msg = msg + Utils.makeKeyValueString(item.getKey(), item.getValue());
               msg = msg + ":" + "\n";
               int max = unknownAttrs.length;
               for (int x = 0; x < max; x++)
               {
                  msg = msg + "      " + unknownAttrs[x] + "\n";
               }
               unknownAttrs = null;
            }
         }
         this.txtResult = msg;
      }
      else
      {
         this.txtResult = "   (none)";
      }

      return this.txtResult;
   }
   
   /**
    * Returns only those language item objects which will have unknown attribute String set.
    */
   private ArrayList<LanguageItem> getUnknownAttributes()
   {
      ArrayList<LanguageItem> unknownAttributes = new ArrayList<LanguageItem>();
      
      Iterator<LanguageItem> iter = this.languageItemsTest.iterator();
      while (iter.hasNext())
      {
         LanguageItem item = iter.next();
         if (item.getUnknownAttributes() != null)
         {
            unknownAttributes.add(item);
         }
      }
      return unknownAttributes;
   }
}
