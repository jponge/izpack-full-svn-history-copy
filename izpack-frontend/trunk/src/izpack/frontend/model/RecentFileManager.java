/*
 * Created on Jul 30, 2004
 * 
 * $Id: RecentFileManager.java Feb 8, 2004 izpack-frontend Copyright (C)
 * 2001-2003 IzPack Development Group
 * 
 * File : RecentFileManager.java Description : TODO Add description Author's
 * email : gumbo@users.berlios.de
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

import java.io.FileNotFoundException;
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
            
            return files;
        }
        catch (XPathException xpe)
        {
            throw new RuntimeException(xpe);
        }
    }
    
    public void addUsedFile(String file)
    {
        files.add(0, file);
    }
    
    public void saveRecentFiles() throws IOException
    {
        int length = files.size() < 5 ? files.size() : 5;
        List choppedFiles = files.subList(0, length);
        Document doc = XML.createDocument();        
        Element root = doc.createElement("recent");
        doc.appendChild(root);
        
        for (Iterator iter = choppedFiles.iterator(); iter.hasNext();)
        {
            String element = (String) iter.next();
            Element elem = doc.createElement("file");
            root.appendChild(elem);
            
            elem.setAttribute("path", element);
        }
        
        XML.writeXML("conf/recent.xml", doc);
    }
    
    private static ArrayList files;
    private static RecentFileManager instance;
}