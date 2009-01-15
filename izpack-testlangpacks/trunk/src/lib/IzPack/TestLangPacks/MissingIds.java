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
 * Finds ID's which should be added.
 */
public class MissingIds extends LangPackData
{
   /**
    * Default constructore of the class.
    */
   public MissingIds ()
   {
      super();
   }

   /**
    * Constructor of the class with parameter(s).
    *
    * @see LangPackData#LangPackData(java.util.ArrayList languageItemsBase, java.util.ArrayList languageItemsTest)
    */
   public MissingIds (ArrayList<LanguageItem> languageItemsBase, ArrayList<LanguageItem> languageItemsTest)
   {
      super(languageItemsBase, languageItemsTest);
   }

   /**
    * Returns ID's which should be added.
    * @see LangPackData#getResult().
    */
   @Override
   public Hashtable<String,ArrayList<LanguageItem>> getResult ()
   {
      if (this.binResult != null)
      {
         return this.binResult;
      }

      boolean found = false;
      String key = null;
      ArrayList<LanguageItem> items = new ArrayList<LanguageItem>();

      for (LanguageItem itemBase : this.languageItemsBase)
      {
         key = itemBase.getKey();
         // If there isn't any ID string,
         if (key==null)
         {
            continue;   // just go to next.
         }
         for (LanguageItem itemToCheck : this.languageItemsTest)
         {
            String checkKey = itemToCheck.getKey();
            if (checkKey!=null)
            {
               if (key.equals(checkKey))
               {
                  if (itemToCheck.getValue() != null)
                  {
                     found = true;  // Only filled id and txt will procedure true!
                  }
               }
            }
         }

         if (!found)
         {
            items.add(new LanguageItem(key, itemBase.getValue()));
         }
         found = false;
      }

      binResult = new Hashtable<String,ArrayList<LanguageItem>>();
      binResult.put("result", items);
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
         this.txtResult = "";
         Iterator<LanguageItem> iter = resultItems.iterator();
         while (iter.hasNext())
         {
            LanguageItem item = iter.next();
            String msg = Utils.makeKeyValueString(item.getKey(), item.getValue());
            this.txtResult += msg + "\n";
         }
      }
      else
      {
         this.txtResult = "   (none)";
      }

      return this.txtResult;
   }
}
