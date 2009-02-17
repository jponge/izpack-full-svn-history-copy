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

package IzPack.TestLangPacks.tests;

import IzPack.TestLangPacks.LangPackFileHandling;

/**
 *
 * @author Ari Voutilainen, 2008-2009
 */
public class TestBase extends LangPackFileHandling
{
   protected final String xmlTestFile = "./src/tests/IzPack/TestLangPacks/tests/test1.xml";
   protected final String xmlTestFile3 = "./src/tests/IzPack/TestLangPacks/tests/test3.xml";
   protected final String xmlTestFile4 = "./src/tests/IzPack/TestLangPacks/tests/test4.xml";

   protected final String path = "./src/tests/IzPack/TestLangPacks/tests/";

   protected final String fileXmlBase           = path + "base.xml";

   protected final String fileMissingIds        = path + "MissingIds.xml";
   protected final String fileMultipleIds       = path + "MultipleIds.xml";
   protected final String fileNotNeededIds      = path + "NotNeededIds.xml";
   protected final String fileSameStrings       = path + "SameStrings.xml";
   protected final String fileUnknownAttributes = path + "UnknownAttributes.xml";
   protected final String fileUnknownElements   = path + "UnknownElements.xml";

   /**
    * The default contructor of the class.
    */
   public TestBase()
   {
      super();
   }
}
