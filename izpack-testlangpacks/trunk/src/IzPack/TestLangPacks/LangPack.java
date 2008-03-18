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

import java.util.ArrayList;

import org.w3c.dom.*;

/**
 * Load one language XML file into memory.
 *
 * @author Ari Voutilainen
 */
public class LangPack
{
   private XmlDocument xmlDoc;
   private ArrayList<LanguageItem> langArrayList = new ArrayList<LanguageItem>();
   private NodeList langStrings = null;

   /**
    * Constructor of the class.
    *
    * @param xmlFile XML file to be loaded.
    * @throws        IzPack.TestLangPacks.LangPackException.
    */
   public LangPack(String xmlFile) throws LangPackException
   {
      String rootElementStr = null;
      try
      {
         xmlDoc = new XmlDocument(xmlFile);
      }
      catch (Exception e)
      {
         throw new LangPackException(e.getMessage());
      }

      if (xmlDoc.IsWellFormed())
      {
         Main.PrintMessage(System.out, null, "XML file is well-formed.");
      }
      else
      {
         throw new LangPackException("XML file is NOT well-formed!");
      }

      rootElementStr = xmlDoc.GetRootName();

      // If root element is not "langpack",
      if (!rootElementStr.equals("langpack"))
      {
         // give an error.
         throw new LangPackException("This is not valid IzPack language file!");
      }

      langStrings = xmlDoc.GetLangStrings();

      int max = langStrings.getLength();
      NamedNodeMap attributes;
      String key, value;
      Node node;
      boolean notFound = false;
      for (int x=0; x < max; x++)
      {
         node = langStrings.item(x);
         attributes = node.getAttributes();
         try
         {
            key   = attributes.getNamedItem("id").getNodeValue();
         }
         catch (Exception e)
         {
            notFound = true;
            key = null;
         }

         try
         {
            value = attributes.getNamedItem("txt").getNodeValue();
         }
         catch (Exception e)
         {
            notFound = true;
            value = null;
         }

         LanguageItem langItem = new LanguageItem(key, value);
         langArrayList.add(langItem);
         if (notFound)
         {
            langItem.SetUnknownAttributes(GetUnknownAttributes(attributes));
         }
      }
   }

   /**
    * Returns all language items. Contains also those items which have missing keys,
    * values or unknown attributes.
    */
   public ArrayList<LanguageItem> GetLangItems()
   {
      return langArrayList;
   }

   /**
    * Returns unknown elements (meaning elements which are not supported in
    * IzPack language file.
    */
   public String[] GetUnknownElements()
   {
      return xmlDoc.GetUnknownElements();

//      NodeList nodeList = null;
//      try
//      {
//         nodeList = xmlDoc.GetUnknownElements();
//      }
//      catch (LangPackException e)
//      {
//         return new String[0];
//      }
//      
//      int max = nodeList.getLength();
//      if (max == 0)
//         return new String[0];
//      else
//      {
//         String[] elements = new String[max];
//         for (int x=0; x < max; x++)
//         {
//            elements[x] = nodeList.item(x).getNodeName();
//         }
//         return elements;
//      }
   }

   /**
    * Gets unknown attributes fo the current element.
    * @param node Node whose attributes are to be scanned.
    * @return     Returns array table containing unknown attributes.
    *             String[].length will be 0 if there isn't any elements.
    */
   private String[] GetUnknownAttributes(NamedNodeMap attributes)
   {
      ArrayList<String> unknown = new ArrayList<String>();
      int max = attributes.getLength();
      for (int x=0; x < max; x++)
      {
         Node attrNode = attributes.item(x);
         if (attrNode.getNodeName().compareTo("id".toLowerCase())!=0 && attrNode.getNodeName().compareTo("txt".toLowerCase())!=0)
            unknown.add(attrNode.getNodeName()+"=\""+attrNode.getNodeValue()+"\"");
      }
      String[] tmp = new String[0]; // Make empty String array. This makes to use "T[] toArray(T[] a)" in ArrayList.
      return unknown.toArray(tmp);  // Return ArrayList items in String[].
   }
}
