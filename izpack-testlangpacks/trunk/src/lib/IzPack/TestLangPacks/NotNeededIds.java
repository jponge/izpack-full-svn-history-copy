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
 * Finds ID's which are not needed anymore.
 */
public class NotNeededIds extends LangPackData
{
   /**
    * Default constructore of the class.
    */
   public NotNeededIds()
   {
      super();
   }

   /**
    * Constructor of the class with parameter(s).
    *
    * @see LangPackData for parameters.
    */
   public NotNeededIds (ArrayList<LanguageItem> languageItemsBase, ArrayList<LanguageItem> languageItemsTest)
   {
      super(languageItemsBase, languageItemsTest);
   }

   /**
    * Finding ID's which are not needed anymore.
    *
    * @see See also LangPackData.getResult()
    */
   @Override
   public Hashtable<String, ArrayList<LanguageItem>> getResult()
   {
      boolean found = false;
      String key = null;
      ArrayList<LanguageItem> items = new ArrayList<LanguageItem>();

      if (this.binResult != null)
      {
         return this.binResult;
      }

      key = null;
      found = false;
      for (LanguageItem itemToCheck : this.languageItemsTest)
      {
         key = itemToCheck.getKey();
         // If there isn't any ID string,
         if (key == null)
         {
            continue;   // just go to the next.
         }
         for (LanguageItem itemBase : this.languageItemsBase)
         {
            String checkKey = itemBase.getKey();
            String checkValue = itemBase.getValue();
            if (checkKey != null && checkValue != null)
            {
               if (checkKey.equals(key))
               {
                  if (checkValue != null)
                  {
                     found = true;  // Only filled id and txt will procedure true!
                  }
               }
            }
         }

         if (!found)
         {
            items.add(new LanguageItem(key, itemToCheck.getValue()));
         }
         found = false;
      }

      binResult = new Hashtable<String, ArrayList<LanguageItem>>();
      binResult.put("result", items);
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
