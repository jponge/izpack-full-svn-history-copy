/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2008 Ari Voutilainen
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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

/**
 * Main routine for language pack handling.
 *
 * @author Ari Voutilainen
 */
public class Main
{
   /* */
   private static final String APP_VERSION = "0.2";

   /**
    * Main method to run.
    *
    * @param args  The command line arguments.
    */
   public static void main(String[] args)
   {
      System.out.println("\nTestLangPacks for IzPack    version " + APP_VERSION);
      System.out.println("");

      // We need at least one argument.
      if (args.length==0)
      {
         System.out.println("No any IzPack language file given to be checked!\n");
         printHelp();
         return;
      }

      if (args[0].equals("-h") || args[0].equals("-?")) // For help.
      {
         printHelp();
         return;
      }

      String xmlBaseFile = "eng.xml";
      String xmlTestFile = null;
      boolean checkOnlyBaseFile = false;
      boolean printSameStrings = false;

      // Handle command line arguments.
      for (int x=0; x < args.length; x++)
      {
         if (args[x].equals("-b")) // Path and file name for base file.
         {
            if (x < args.length)
            {
               x++;
               xmlBaseFile = args[x];
            }
         }
         else if (args[x].equals("-cb"))
         {
            checkOnlyBaseFile = true;  // Check only base file.
         }
         else if (args[x].equals("-s"))
         {
            printSameStrings = true;  // Check same strings in XML file.
         }
         else if (args[x].contains("-"))
         {
            System.err.println("Command line contains invalid argument: \""+args[x]+"\"!\n");
            printHelp();
            return;
         }
         else
         {
            xmlTestFile = args[x];  // Lang file to be tested.
         }
      }

      LangPack langPackBase = null;
      LangPack langPackTest = null;
      ArrayList<LanguageItem> langItemsBase = null;
      ArrayList<LanguageItem> langItemsTest = null;

      // Load and parse language XML base file.
      System.out.println("Loading and checking base file: " + xmlBaseFile);
      try
      {
         langPackBase = new LangPack(xmlBaseFile);
         langItemsBase = langPackBase.GetLangItems();

      }
      catch (Exception e)
      {
         printMessage(System.err, "Error in base file", e.getMessage());
         return;
      }
      
      if (checkOnlyBaseFile)
      {
         return;
      }

      // Load and parse language XML file to be tested.
      System.out.println("\nLoading and checking file to check: " + xmlTestFile);
      try
      {
         langPackTest = new LangPack(xmlTestFile);
         langItemsTest = langPackTest.GetLangItems();
      }
      catch (Exception e)
      {
         printMessage(System.err, "Error in file to be tested", e.getMessage());
         return;
      }

      // Finding missing string ID's.
      // ---------------------------
      System.out.println("\nFinding ID's which should be added ("+xmlBaseFile+" => "+xmlTestFile+"):");

      boolean found = false;
      String key = null;
      for (LanguageItem itemBase : langItemsBase)
      {
         key = itemBase.GetKey();
         // If there isn't any ID string,
         if (key==null)
         {
            continue;   // just go to next.
         }
         for (LanguageItem itemToCheck : langItemsTest)
         {
            String checkKey = itemToCheck.GetKey();
            if (checkKey!=null)
            {
               if (key.equals(checkKey))
               {
                  if (itemToCheck.GetValue() != null)
                  {
                     found = true;  // Only filled id and txt will procedure true!
                  }
               }
            }
         }

         if (!found)
         {
            System.out.println(makeKeyValueString(key, itemBase.GetValue()));
         }
         found = false;
      }

      if (key==null)
      {
         System.out.println("   (none)");
      }

      // Finding ID's which are not needed anymore.
      // -----------------------------------------
      System.out.println("\nFinding ID's which are not needed anymore in "+xmlTestFile+":");

      key = null;
      found = false;
      boolean totalFoundState = false;
      for (LanguageItem itemToCheck : langItemsTest)
      {
         key = itemToCheck.GetKey();
         // If there isn't any ID string,
         if (key==null)
         {
            continue;   // just go to the next.
         }
         for (LanguageItem itemBase : langItemsBase)
         {
            String checkKey = itemBase.GetKey();
            String checkValue = itemBase.GetValue();
            if (checkKey!=null && checkValue!=null)
            {
               if (checkKey.equals(key))
               {
                  if (checkValue != null)
                  {
                     found = true;  // Only filled id and txt will procedure true!
                     totalFoundState = true;
                  }
               }
            }
         }

         if (!found)
         {
            System.out.println(makeKeyValueString(key, itemToCheck.GetValue()));
         }
         found = false;
      }

      if (key==null && totalFoundState==false)
      {
         System.out.println("   (none)");
      }
      
      // Check unknown attributes.
      // ------------------------
      System.out.println("\nFinding unknown attributes in "+xmlTestFile+":");

      found = false;
      for (LanguageItem itemToCheck : langItemsTest)
      {
         String[] unknownAttrs = itemToCheck.GetUnknownAttributes();
         if (unknownAttrs != null)
         {
            String msg = "   Found unknown attributes in";
            msg = msg + makeKeyValueString(itemToCheck.GetKey(), itemToCheck.GetValue());
            System.out.println(msg + ":");
            int max = unknownAttrs.length;
            for (int x=0; x < max; x++)
            {
               System.out.println("      "+unknownAttrs[x]);
            }
            unknownAttrs = null;
            found = true;
         }
      }
      
      if (!found)
      {
         System.out.println("   (none)");
      }

      // Finding unknown elements.
      // ------------------------
      System.out.println("\nFinding unknown elements in "+xmlTestFile+":");
      System.out.println("(means elements which are not supported.)");

      String[] unknownElements = langPackTest.GetUnknownElements();
      if (unknownElements.length==0)
      {
         System.out.println("   (none)");
      }
      else
      {
         for (String elem : unknownElements)
         {
            System.out.println("   <"+elem+" ... />");
         }
      }
      
      if (printSameStrings)
      {
         // Finding possible same strings (finding with last word in the ID which
         // consists of two words or more).
         // ---------------------------------------------------------------------
         System.out.println("\nFinding possible same strings in "+xmlTestFile+":");
         System.out.println("   (Check that translations are congruent (that the strings in same context");
         System.out.println("    have been translated at the same way). This also produces incorrect results!)");
         System.out.println("    Do NOT change at once! Check by hand in unclear cases.");

         // Collect same strings.
         Hashtable<String, ArrayList<LanguageItem>> sameStrings = getSameStrings(langItemsTest);
         if (sameStrings.size() > 0)
         {
            Set<Map.Entry<String, ArrayList<LanguageItem>>> keyValuePairs = sameStrings.entrySet();
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
                  System.out.println("\n   \"" + sameKey + "\":");
                  // 'for' handles all str elements for the "heading".
                  for (int i=0; i < numValues; i++)
                  {
                     String itemKey = "      \"";
                     String itemValue = "          \"";
                     itemKey += values.get(i).GetKey() + "\"";
                     itemValue += values.get(i).GetValue() + "\"";
                     System.out.println(itemKey + ":\n" + itemValue);
                  }
               }
            }
         }
         else
         {
            System.out.println("   (none)");
         }
      }
   }

   /**
    * Returns the same strings.
    * 
    * @note Because this generates a lot of results and always, this feture must be activated manually.
    *
    * @param langItems  Language items to check.
    */
   public static Hashtable<String, ArrayList<LanguageItem>> getSameStrings(ArrayList<LanguageItem> langItems)
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
    * 
    * @param sameStringArray        The array list where to search strings.
    * @param sameStringsHastTable   This hash table will contain all found strings. Key will contain one word and
    *                               value part will contain array list of all id's found with key's content. 
    * @return  Returns the number of the items in 'sameStringArray'.
    */
   private static int collectStrings(ArrayList<LanguageItem> sameStringArray, Hashtable<String, ArrayList<LanguageItem>> sameStringsHastTable)
   {  // Vähentää listasta löydetyt eli pienentää listan pituutta.
      // Palauttaa uuden listan.
      // Ikiluuppi, koska lista pienenee koko ajan
      
      // Get first id word.
      // Remove it from the list.
      // Loop for the rest id words in the list. If the same id word is found save it and remove it from the list.
      // After looping return reduced list.
      
      // Get first word and save it.
      String keyWord = getLastWord(sameStringArray.get(0).GetKey());
      // Remove first item.
      sameStringArray.remove(0);
      // Go through the rest items and find same strings
      java.util.Iterator<LanguageItem> iter = sameStringArray.iterator();
      // Array list where to save found words.
      ArrayList<LanguageItem> words = new ArrayList<LanguageItem>();
      while (iter.hasNext())
      {
         LanguageItem temp = iter.next();
         String currentKey = getLastWord(temp.GetKey());
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

  /**
   * Gets last word in ID string.
   *
   * @param idString   ID string where to get last word. Words must be separated by
   *                   full stop '.' .
   * @return           Returns last word in ID string. Returns null if 'idString'
   *                   is empty or 'idString' contains only one word. ID strings
   *                   containing only one word must always be different so it has
   *                   no use to compare them. There must more than 1 word in
   *                   ID string. 
   */
  private static String getLastWord(String idString)
  {
     String strReturn = null;
     String[] sArr = idString.split("\\x2e"); // "."
     int size = sArr.length;
     if (size > 1)
        strReturn = sArr[size-1];

     return strReturn;
  }

   /**
    * Prints the message on the console screen.
    *
    * @param outStream  Output stream. System.out or System.err.
    * @param caption    The caption of the message.
    * @param msg        The message.
    */
   public static void printMessage(PrintStream outStream, String caption, String msg)
   {
      String totMsg = "";
      if (caption!=null && caption.contains(""))
      {
         totMsg = "\n" + caption + ":\n   ";
      }
      totMsg = totMsg +  msg;
      outStream.println(totMsg);
   }

   /**
    * Returns the string made with id and txt.
    *
    * @param key     ID found from 'str' element.
    * @param value   Text value found from 'str' element.
    */
   private static String makeKeyValueString(String key, String value)
   {
      String msg = "   str ";
      //"   str id=\"" + key + "\" txt=\"" + itemBase.GetValue() + "\"");
      if (key==null || key.equals(""))
      {
         msg = msg + "(id missing!) ";
      }
      else
      {
         msg = msg + "id=\"" + key + "\" ";
      }

      if (value==null || value.equals(""))
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
    * Prints the help on the System.out.
    */
   private static void printHelp()
   {
      String helpMsg =
      "Usage: java -jar TestLangPacks.jar {langfile} [-b {basefile}] [-cb] [-s]\n"+
      "where:\n\n"+
      "{langfile} is the IzPack language file to be tested. File name can contain path\n"+
      "           to the file.\n\n"+
      "{basefile} is the IzPack language file which is used as a base file to the\n"+
      "           comparisons. If -b exists there must be space between -b and\n"+
      "           {basefile}. The name can contain also a path. If you want to give\n"+
      "           the path to default base file you must give both path and file.\n"+
      "           This file is optional. Default base file name is eng.xml.\n"+
      "-cb        Checks only base file (no other xml files needed)\n\n"+
      "-s         Prints same strings found in XML file. Comparing is done with\n"+
      "           the last word found in 'id' string in 'str' element.\n"+
      "           NOTE: Because this produces always something results and also\n"+
      "                 strings which are not related with each other this feature\n"+
      "                 must be activated by hand.\n"+
      "\n"+
      "To have results into the file put '>filename.ext' end of the line.\n\n"+
      "If you give -? or -h you will get this help."+
      "\n";
      System.out.print(helpMsg);
   }
}
