/*
 * Created on Jul 30, 2004
 * 
 * $Id: RecentFileManager.java Feb 8, 2004 izpack-frontend Copyright (C)
 * 2005 Andy Gombos
 * 
 * File : RecentFileManager.java Description : TODO Add description Author's
 * email : gumbo@users.berlios.de
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XML;
import exceptions.DocumentCreationException;
import exceptions.UnhandleableException;

/**
 * @author Andy Gombos
 */
public class RecentFileManager
{
    public static RecentFileManager getInstance()
    {
        if (instance == null)
        {
            instance = new RecentFileManager();
            files = new ArrayList();
        }
        return instance;
    }
    
    public ArrayList loadRecentList()
    {
        if (files.size() != 0)
            return files;
        
        try
        {
            Document doc = XML.createDocument("conf/recent.xml");
            XPath xpath = XPathFactory.newInstance().newXPath();

            //Load the authors array
            NodeList fileElems = (NodeList) xpath.evaluate("//file", doc, XPathConstants.NODESET);            
            for (int i = 0; i < fileElems.getLength(); i++)
            {
                Element element = (Element) fileElems.item(i);                
                files.add(element.getAttribute("path"));
                
            }
        }
        catch (XPathException xpe)
        {
            throw new UnhandleableException(xpe);
        }
        catch (DocumentCreationException e)
        {
            //Nothing to do, just no recent files list
        }
        
        return files;
    }
    
    public void addUsedFile(String file)
    {
        files.add(0, file);
    }
    
    public void saveRecentFiles() throws IOException
    {
        int length = files.size() < 5 ? files.size() : 5;
        List choppedFiles = files.subList(0, length);        
                
        Element root = XML.createRootElement("recent");
        Document doc = root.getOwnerDocument();
        
        for (Iterator iter = choppedFiles.iterator(); iter.hasNext();)
        {
            String element = (String) iter.next();
            Element fileElem = XML.createElement("file", doc);
            root.appendChild(fileElem);
            
            fileElem.setAttribute("path", element);
        }
        
        XML.writeXML("conf/recent.xml", doc);
    }
    
    private static ArrayList files;
    private static RecentFileManager instance;
}