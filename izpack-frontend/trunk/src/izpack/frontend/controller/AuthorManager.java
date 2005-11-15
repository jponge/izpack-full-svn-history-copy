/*
 * Created on Jun 29, 2004
 * 
 * $Id: AuthorManager.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : AuthorManager.java Description : TODO Add description Author's email :
 * gumbo@users.berlios.de
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

package izpack.frontend.controller;

import izpack.frontend.model.Author;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XML;
import exceptions.DocumentCreationException;
import exceptions.UnhandleableException;

/**
 * @author Andy Gombos
 * 
 * Manage the persistent list of authors who have been used in the installer 
 */
public class AuthorManager
{
    public static ArrayList loadAuthors()
    {   
                    
        Document document;
        try
        {
            document = XML.createDocument("conf/authors.xml");
        }
        catch (DocumentCreationException e1)
        {
            return null;
        }       
        
        XPath xpath = XPathFactory.newInstance().newXPath();

        //Load the authors array
        NodeList authorElems;
        try
        {
            
            authorElems = (NodeList) xpath.evaluate("//author", document, XPathConstants.NODESET);

            ArrayList<Author> authors = new ArrayList<Author>();

            for (int i = 0; i < authorElems.getLength(); i++)
            {
                String aname = xpath.evaluate("//author[" + (i + 1) + "]/@name", document);
                String email = xpath.evaluate("//author[" + (i + 1) + "]/@email", document);
                Author author = new Author(aname, email);

                if (!authors.contains(author))
                    authors.add(author);
            }

            Collections.sort(authors);            

            persistantAuthors = authors;
            
            return authors;   
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }
    }
    
    public static void writeAuthors() throws IOException
    { 
        if (persistantAuthors == null)
            return;
        
        Document document = XML.createDocument();
        
        //Create the root element (authors)
        Element root = document.createElement("authors");
        document.appendChild(root);
        
        for (Iterator iter = persistantAuthors.iterator(); iter.hasNext();)
        {
            Author auth = (Author) iter.next();
            Element author = document.createElement("author");
            root.appendChild(author);          
            
            author.setAttribute("name", auth.getName());
            author.setAttribute("email", auth.getEmail());
        }
        
        XML.writeXML("conf/authors.xml", document);
    }
    
    public static void updateAuthors(ArrayList authors)
    {
        persistantAuthors = authors;
    }
    
    private static ArrayList persistantAuthors;
}

