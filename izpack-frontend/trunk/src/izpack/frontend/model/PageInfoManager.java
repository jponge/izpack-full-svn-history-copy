/*
 * Created on Jun 3, 2004
 * 
 * $Id: PageInfoManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PageInfoManager.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XML;

/**
 * @author Andy Gombos
 * 
 * Provides a common class to query about <code>Page</code>s.  
 * 
 * Reads the XML data files which specify data for each panel, and creates a <code>PageInfo</code>
 * object out of that data
 */

public class PageInfoManager
{
    private static PageInfo loadPage(String filename)
    {		
        try
        {
			Document doc = XML.createDocument(filename);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			String name = ( (Element) xpath.evaluate("/", doc, XPathConstants.NODE) ).getAttribute("name");			
			System.out.println("Name: " + name);
			
			//Load panel descriptions			
			String shortDesc = xpath.evaluate("//panel-desc-short", doc);
			String longDesc  = xpath.evaluate("//panel-desc-long", doc);
			
			//Load the authors array
			NodeList authorElems = (NodeList) xpath.evaluate("//authors/author", doc, XPathConstants.NODESET);
			ArrayList authors = new ArrayList();
			for (int i = 0; i < authorElems.getLength(); i++)
            {
			    Element element = (Element) authorElems.item(i);
                String aname = element.getAttribute("name");
                String email = element.getAttribute("email");
                Author author = new Author(aname, email);
                authors.add(author);
            }
			
			Element resourcesElem = (Element) xpath.evaluate("//resources", doc, XPathConstants.NODE);			
			ArrayList resources = new ArrayList();
			if (resourcesElem != null)
			{			
			    NodeList resourceElem = (NodeList) xpath.evaluate("//resources/res", doc, XPathConstants.NODESET);				
				for (int i = 0; i < resourceElem.getLength(); i++)
	            {
	                Element element = (Element) resourceElem.item(i);
	                String id = element.getAttribute("id");
	                String req = element.getAttribute("required");
	                String sType = element.getAttribute("type");
	                
	                boolean required = req.equalsIgnoreCase("true");
	                int type;
	                
	                if (sType.equalsIgnoreCase("txt"))
	                    type = PageInfo.Resource.TXT;
	                else if (sType.equalsIgnoreCase("img"))
	                    type = PageInfo.Resource.IMG;
	                else if (sType.equalsIgnoreCase("xml"))
	                    type = PageInfo.Resource.XML;
	                else if (sType.equalsIgnoreCase("html"))
	                    type = PageInfo.Resource.HTML;
	                else
	                    type = PageInfo.Resource.UNKNOWN;
	                
	                PageInfo.Resource resource = new PageInfo.Resource(id, required, type);
	                resources.add(resource);                              
	        	}
			}
			
			System.out.println("panel configuration loaded: " + filename);			
			
			return new PageInfo(name, shortDesc, longDesc, (Author[]) authors.toArray(new Author[0]), (PageInfo.Resource[]) resources.toArray(new PageInfo.Resource[0]));					
        }
        catch (XPathExpressionException e)
        {
           throw new RuntimeException(e);
        }
    }    
    
    private static String[] getConfigFiles()
    {
        return new File(CONFIG_PATH).list(new FilenameFilter() 
	        {
	            public boolean accept(File dir, String name)
	            {
	                return name.endsWith("xml");
	            }            
	        });
    }
    
    public static ArrayList getAvailablePages()
    {
        String[] confFiles = getConfigFiles();
        for (int i = 0; i < confFiles.length; i++)
        {
            pages.add(loadPage(CONFIG_PATH + confFiles[i]));
    	}

        return pages;
    }
    
    private static final String CONFIG_PATH = "conf/pages/";
    private static ArrayList pages = new ArrayList();
}
