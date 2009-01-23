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
import java.util.Map;
import java.util.Set;

/**
 * Finds possible same strings (finding with last word in the ID which
 * consists of two words or more).
 */
public class SameStrings extends LangPackData
{

   private ArrayList<LanguageItem> langItemsTest = null;

   /**
    * Default constructor of the class.
    */
   @SuppressWarnings("unused")
   private SameStrings()
   {
      super();
   }

   /**
    * Constructor of the class with parameter(s).
    *
    * @param langItemsTest Language items to be tested.
    */
   public SameStrings(ArrayList<LanguageItem> langItemsTest)
   {
      super();

      this.langItemsTest = langItemsTest;
   }

   /**
    * Returns possible same strings.
    * @see LangPackData.getResult()
    */
   @Override
   public Hashtable<String, ArrayList<LanguageItem>> getResult()
   {
      if (this.binResult != null)
      {
         return this.binResult;
      }

      this.binResult = getSameStrings(langItemsTest);

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
      
      // Collect same strings.
      if (this.binResult.size() > 0)
      {
         Set<Map.Entry<String, ArrayList<LanguageItem>>> keyValuePairs = this.binResult.entrySet();
         Iterator<Map.Entry<String, ArrayList<LanguageItem>>> iter = keyValuePairs.iterator();

         // 'while' handles the "heading" for the same strings.
         while (iter.hasNext())
         {
            java.util.Map.Entry<String, ArrayList<LanguageItem>> keyValuePair = iter.next();
            String sameKey = keyValuePair.getKey();
            ArrayList<LanguageItem> values = keyValuePair.getValue();
            int numValues = values.size();
            if (numValues > 1)
            {
               this.txtResult += "\n   \"" + sameKey + "\":";
               // 'for' handles all str elements for the "heading".
               for (int i = 0; i < numValues; i++)
               {
                  String itemKey = "      \"";
                  String itemValue = "          \"";
                  itemKey += values.get(i).getKey() + "\"";
                  itemValue += values.get(i).getValue() + "\"";
                  this.txtResult += itemKey + ":\n" + itemValue;
               }
            }
         }
      }
      else
      {
         this.txtResult = "   (none)";
      }

      return this.txtResult;
   }

   /**
    * Returns the same strings.
    * @note Because this generates a lot of results and always, this feture must be activated manually.
    * @param langItems  Language items to check.
    */
   private static Hashtable<String, ArrayList<LanguageItem>> getSameStrings(ArrayList<LanguageItem> langItems)
   {
      // Make a copy of language items.
      ArrayList<LanguageItem> copyLangItemArray = new ArrayList<LanguageItem>(langItems);
      // Hashtable which will contain id's with same txt strings collected from 'copyLangItem'.
      Hashtable<String, ArrayList<LanguageItem>> sameStringsHastTable = new Hashtable<String, ArrayList<LanguageItem>>();

      int amount = copyLangItemArray.size();
      while (amount > 0)
      {
         amount = collectStrings(copyLangItemArray, sameStringsHastTable);
      }
      return sameStringsHastTable;
   }

   /**
    * Collects the same strings for the current 'id'.
    * @param sameStringArray        The array list where to search strings.
    * @param sameStringsHastTable   This hash table will contain all found strings. Key will contain one word and
    *                               value part will contain array list of all id's found with key's content.
    * @return  Returns the number of the items in 'sameStringArray'.
    */
   private static int collectStrings(ArrayList<LanguageItem> sameStringArray, Hashtable<String, ArrayList<LanguageItem>> sameStringsHastTable)
   {
      // Get first id word.
      // Remove it from the list.
      // Loop for the rest id words in the list. If the same id word is found save it and remove it from the list.
      // After looping return reduced list.

      // Get first word and save it.
      String keyWord = Utils.getLastWord(sameStringArray.get(0).getKey());
      // Remove first item.
      sameStringArray.remove(0);
      // Go through the rest items and find same strings
      java.util.Iterator<LanguageItem> iter = sameStringArray.iterator();
      // Array list where to save found words.
      ArrayList<LanguageItem> words = new ArrayList<LanguageItem>();
      while (iter.hasNext())
      {
         LanguageItem temp = iter.next();
         String currentKey = Utils.getLastWord(temp.getKey());
         // If found,
         if (currentKey.equalsIgnoreCase(keyWord))
         {
            // ...save it.
            words.add(temp);
            // Remove found object too.
            sameStringArray.remove(temp);
            // Because array list has been changed update iterator.
            iter = sameStringArray.iterator();
         }
      }
      // Add found words into same string hash table. We take this only when there are more
      // than 1 words in array list.
      if (words.size() > 1)
      {
         sameStringsHastTable.put(keyWord, words);
      }
      // Return number of items in sameStringArray.
      return sameStringArray.size();
   }
}
