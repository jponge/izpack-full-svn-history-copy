/*
 * Created on Jun 29, 2004
 * 
 * $Id: AuthorManager.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : AuthorManager.java Description : TODO Add description Author's email :
 * gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.XML;

/**
 * @author Andy Gombos
 * 
 * Manage the persistent list of authors who have been used in the installer 
 */
public class AuthorManager
{
    public static ArrayList loadAuthors()
    {   
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File("conf/authors.xml"));		
			XPath xpath = XPathFactory.newInstance().newXPath();		

            //Load the authors array            
            NodeList authorElems = (NodeList) xpath.evaluate("//author", document, XPathConstants.NODESET);
            ArrayList authors = new ArrayList();
            
            for (int i = 0; i < authorElems.getLength(); i++)
            {                
                String aname = xpath.evaluate("//author[" + (i + 1) + "]/@name", document);                
                String email = xpath.evaluate("//author[" + (i + 1) + "]/@email", document);                   
                Author author = new Author(aname, email);
        
                authors.add(author);
            }

            Collections.sort(authors);
            
            return authors;
        }
   
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static void writeAuthors(ArrayList authors) throws IOException
    {   
        Document document = XML.createDocument();
        
        //Create the root element (authors)
        Element root = document.createElement("authors");
        document.appendChild(root);
        
        for (Iterator iter = authors.iterator(); iter.hasNext();)
        {
            Author auth = (Author) iter.next();
            Element author = document.createElement("author");
            root.appendChild(author);          
            
            author.setAttribute("name", auth.getName());
            author.setAttribute("email", auth.getEmail());
        }
        
        XML.writeXML("conf/author3.xml", document);
    }
}

