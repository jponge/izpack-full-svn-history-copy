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

/**
 * Handles language packs and language pack items.
 */
public class LangPackFileHandling
{
   protected LangPack langPackBase = null;
   protected LangPack langPackTest = null;
   protected ArrayList<LanguageItem> langItemsBase = null;
   protected ArrayList<LanguageItem> langItemsTest = null;

   /**
    * The default contructor of the class.
    */
   public LangPackFileHandling()
   {
   }

   /**
    *
    * @throws IzPack.TestLangPacks.LangPackException
    */
   public void createLangPackBase(String xmlBaseFile) throws IzPack.TestLangPacks.LangPackException
   {
      if (xmlBaseFile != null)
      {
         langPackBase = new LangPack(xmlBaseFile);
         langItemsBase = langPackBase.getLangItems();
      }
   }

   /**
    * 
    * @throws IzPack.TestLangPacks.LangPackException
    */
   public void createLangPackTest(String xmlTestFile) throws IzPack.TestLangPacks.LangPackException
   {
      if (xmlTestFile != null)
      {
         langPackTest = new LangPack(xmlTestFile);
         langItemsTest = langPackTest.getLangItems();
      }
   }
}
