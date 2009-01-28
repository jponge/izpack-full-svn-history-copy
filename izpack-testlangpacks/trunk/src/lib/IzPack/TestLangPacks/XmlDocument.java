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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import javax.xml.parsers.*;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPath;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Capsulates XML document in TestLangPacks.
 *
 * @author Ari Voutilainen
 */
public class XmlDocument
{
   private DocumentBuilder xmlDocBuilder = null;
   private Document xmlDoc = null;
   private DOMConfiguration domConf;
   private LangPackXmlErrorHandler errorHandler = new LangPackXmlErrorHandler();
   private InputSource inputSource = null;

   /**
    * Constructor for XML document class.
    *
    * @param   xmlFile  XML file to load.
    * @throws  IzPack.TestLangPacks.LangPackException
    */
   public XmlDocument(String xmlFile) throws LangPackException
   {
      try
      {
         xmlDocBuilder     = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         xmlDocBuilder.setErrorHandler(errorHandler);
         xmlDoc            = xmlDocBuilder.parse(xmlFile);
      }
      catch (FactoryConfigurationError e)
      {
         throw new LangPackException(e.getMessage());
      }
      catch (ParserConfigurationException e)
      {
         throw new LangPackException(e.getMessage());
      }
      catch (SAXException e)
      {
         throw new LangPackException(e.getMessage());
      }
      catch (IOException e)
      {
         throw new LangPackException(e.getMessage());
      }
      setNormalizeParameters();

      try
      {
         inputSource = new InputSource(new FileInputStream(new File(xmlFile)));
      }
      catch (FileNotFoundException e)
      {
         throw new LangPackException(e.getMessage());
      }
   }

   /**
    * Returns the root name of the XML file.
    */
   public String getRootName()
   {
      // Get the name of the root element.
      Element rootElement  = xmlDoc.getDocumentElement();
      return rootElement.getNodeName();
   }

   /**
    * Checks if XML document is well-formed.
    *
    * @return  true  Documents is wel-formed. false document is not well-formed.
    * @throws  IzPack.TestLangPacks.LangPackException
    */
   public boolean isWellFormed() throws LangPackException
   {
      domConf = xmlDoc.getDomConfig();
      try
      {
         domConf.setParameter("cdata-sections", true);
         domConf.setParameter("comments", true);
         domConf.setParameter("normalize-characters", false);
         domConf.setParameter("well-formed", true);
      }
      catch (DOMException e)
      {
         String msg = null;
         if (e.code == DOMException.NOT_FOUND_ERR)
         {
            msg = "Parameter name is not recognized.";
         }
         else if (e.code == DOMException.NOT_SUPPORTED_ERR)
         {
            msg = "Parameter name is recognized but the requested value cannot be set.";
         }
         else if (e.code == DOMException.TYPE_MISMATCH_ERR)
         {
            msg = "Parameter name is incompatible with the expected value type.";
         }

         CmdLineConsole.printMessage(System.err, "Error in normalize parameters", msg);
         throw new LangPackException();
      }

      try
      {
         xmlDoc.normalizeDocument();
      }
      catch (Exception e)
      {
         return false;
      }
      return true;
   }

   /**
    * This is used when all additionals are first removed before testing language strings.
    */
   private void setNormalizeParameters()
   {
      domConf = xmlDoc.getDomConfig();
      try
      {
         domConf.setParameter("cdata-sections", false);
         domConf.setParameter("comments", false);
         domConf.setParameter("well-formed", true);
      }
      catch (DOMException e)
      {
         String msg = null;
         if (e.code == DOMException.NOT_FOUND_ERR)
         {
            msg = "Parameter name is not recognized.";
         }
         else if (e.code == DOMException.NOT_SUPPORTED_ERR)
         {
            msg = "Parameter name is recognized but the requested value cannot be set.";
         }
         else if (e.code == DOMException.TYPE_MISMATCH_ERR)
         {
            msg = "Parameter name is incompatible with the expected value type.";
         }

         CmdLineConsole.printMessage(System.err, "Normalize parameters", msg);
      }
   }

   /**
    * Returns language strings in XML file.
    * @return  Returns NodeList containing all elements with "str".
    */
   public NodeList getLangStrings()
   {
      return xmlDoc.getElementsByTagName("str");
   }

   /**
    * Returns unknown elements.
    *
    * @throws LangPackException
    */
   public String[] getUnknownElements()
   {
      if (inputSource == null)
      {
         return new String[0];
      }

      ArrayList<String> elems = new ArrayList<String>();
      String nodeName = null;
      String unknownElemString = null;

      // Gets all elements.
      XPath xpath = XPathFactory.newInstance().newXPath();
      NodeList nodes = null;
      try
      {
         nodes = (NodeList) xpath.evaluate("/langpack/*", inputSource, XPathConstants.NODESET);
      }
      catch (XPathExpressionException e)
      {
         return new String[0];
      }

      /* Tried several XPath 1.0 expressions to get "all other except 'str' ".
       * Not succeeded! Only tested and working expression is wellcomed.
       */
      Node node = null;
      int max = nodes.getLength();
      for (int x=0; x < max; x++)
      {
         node = nodes.item(x);
         nodeName = node.getNodeName();
         if (!nodeName.equals("str"))
         {
            NamedNodeMap attributes = node.getAttributes();
            unknownElemString = nodeName + collectAttributes(attributes);
            elems.add(unknownElemString);
         }
      }

      String[] templateArray = new String[0];// Make empty String array. This makes to use "T[] toArray(T[] a)" in ArrayList.
      return elems.toArray(templateArray);   // Return ArrayList items as String[].
   }
   
   /**
    * Collects all attributes of the element and returns them as a String.
    * 
    * @param   attributes  Attributes for the element.
    * @return  Returns all attributes of the element as a String or empty string if not any.
    * @note    Contains '=' if there is any attributes. If not any returns only empty string "".
    */
   private String collectAttributes(NamedNodeMap attributes)
   {
      String[] attrs = getAttributes(attributes, false);
      int len = attrs.length;
      if (len == 0)
      {
         return "";
      }
      
      String returnStr = " ";
      for (int x=0; x < len; x++)
      {
         String str = attrs[x];
         returnStr += str;
         if (x < len-1)
         {
            returnStr += " ";
         }
      }
      return returnStr;
   }

   /**
    * Returns attributes due to filtering information.
    * 
    * @param   attributes  Attributes for the element.
    * @param   filterAttrs true: selects only attributes 'id' AND 'txt'. These are supported attributes
    *                      in IzPack language files.
    *                      false: selects all which can be found.
    * @return  Returns all attributes of the element in String array.
    */
   public String[] getAttributes(NamedNodeMap attributes, boolean filterAttrs)
   {
      ArrayList<String> unknown = new ArrayList<String>();
      int max = attributes.getLength();
      for (int x=0; x < max; x++)
      {
         Node attrNode = attributes.item(x);
         if (filterAttrs==true)
         {
            if (attrNode.getNodeName().compareTo("id".toLowerCase())!=0 && attrNode.getNodeName().compareTo("txt".toLowerCase())!=0)
            {
               unknown.add(attrNode.getNodeName()+"=\""+attrNode.getNodeValue()+"\"");
            }
         }
         else
         {
            unknown.add(attrNode.getNodeName()+"=\""+attrNode.getNodeValue()+"\"");
         }
      }
      String[] tmp = new String[0]; // Make empty String array. This makes to use "T[] toArray(T[] a)" in ArrayList.
      return unknown.toArray(tmp);  // Return ArrayList items in String[].
   }
}
