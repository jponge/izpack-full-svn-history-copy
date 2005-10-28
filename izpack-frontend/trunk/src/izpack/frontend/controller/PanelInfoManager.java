/*
 * Created on Jun 3, 2004
 * 
 * $Id: PageInfoManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PageInfoManager.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
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
import izpack.frontend.model.PanelInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
 * Provides a common class to query about <code>Page</code>s.  
 * 
 * Reads the XML data files which specify data for each panel, and creates a <code>PageInfo</code>
 * object out of that data
 */

public class PanelInfoManager
{
    private static PanelInfo loadPage(String filename)
    {	        
        try
        {
			Document doc = XML.createDocument(filename);			
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			String classname = ( (Element) xpath.evaluate("/izpack-panel", doc, XPathConstants.NODE) ).getAttribute("classname");
            
            String editingClass = ( (Element) xpath.evaluate("/izpack-panel", doc, XPathConstants.NODE) ).getAttribute("editingClass");
            
			String name = ( (Element) xpath.evaluate("/izpack-panel", doc, XPathConstants.NODE) ).getAttribute("name");			
						
			//Load panel descriptions			
			String shortDesc = xpath.evaluate("//panel-desc-short", doc);
			String longDesc  = xpath.evaluate("//panel-desc-long", doc);
			
			//Load the authors array
			NodeList authorElems = (NodeList) xpath.evaluate("//authors/author", doc, XPathConstants.NODESET);
			ArrayList<Author> authors = new ArrayList<Author>();
			for (int i = 0; i < authorElems.getLength(); i++)
            {
			    Element element = (Element) authorElems.item(i);
                String aname = element.getAttribute("name");
                String email = element.getAttribute("email");
                Author author = new Author(aname, email);
                authors.add(author);
            }
			
			Element resourcesElem = (Element) xpath.evaluate("//resources", doc, XPathConstants.NODE);			
			ArrayList<PanelInfo.Resource> resources = new ArrayList<PanelInfo.Resource>();
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
	                    type = PanelInfo.Resource.TXT;
	                else if (sType.equalsIgnoreCase("img"))
	                    type = PanelInfo.Resource.IMG;
	                else if (sType.equalsIgnoreCase("xml"))
	                    type = PanelInfo.Resource.XML;
	                else if (sType.equalsIgnoreCase("html"))
	                    type = PanelInfo.Resource.HTML;
	                else
	                    type = PanelInfo.Resource.UNKNOWN;
	                
	                PanelInfo.Resource resource = new PanelInfo.Resource(id, required, type);
	                resources.add(resource);                              
	        	}
			}			
			
			return new PanelInfo(classname, editingClass, name, shortDesc, longDesc, (Author[]) authors.toArray(new Author[0]),
			                (PanelInfo.Resource[]) resources.toArray(new PanelInfo.Resource[0]));					
        }
        catch (XPathExpressionException e)
        {
           throw new UnhandleableException(e);
        }
        catch (DocumentCreationException e)
        {            
            return null;
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
    
    /**
     * Loads the available panels at the start of the application, and provides
     * a static list throughout execution
     * 
     * @return ArrayList of the available panels
     */
    public static ArrayList<PanelInfo> getAvailablePanels()
    {
        if (panels == null)
        {
            String[] confFiles = getConfigFiles();
            panels = new ArrayList<PanelInfo>(confFiles.length);            
            
            for (int i = 0; i < confFiles.length; i++)
            {                       
                panels.add(loadPage(CONFIG_PATH + confFiles[i]));            
        	}        
                   
            PanelInfo[] sortedPages = panels.toArray(new PanelInfo[0]);
            Arrays.sort(sortedPages);
            
            panels = new ArrayList<PanelInfo>(Arrays.asList(sortedPages));
    
            return panels;
        }
        else
        {
            return panels;
        }
    }
    
    public static HashMap<String, PanelInfo> getAvailablePanelMap()
    {
        if (panelMap == null)
        {
            String[] confFiles = getConfigFiles();
            panelMap = new HashMap<String, PanelInfo>(confFiles.length);            
            
            for (int i = 0; i < confFiles.length; i++)
            {                       
                PanelInfo panelInfo = loadPage(CONFIG_PATH + confFiles[i]);
                
                panelMap.put(panelInfo.getClassname(), panelInfo);            
            }
    
            return panelMap;
        }
        else
        {
            return panelMap;
        }
    }
    
    //Determine the correct path by requesting the language code of the application
    private static String CONFIG_PATH = "conf/pages/" + GUIController.getInstance().appConfiguration().getI18NLangCode() + "/";
    private static ArrayList<PanelInfo> panels = null;
    private static HashMap<String, PanelInfo> panelMap = null;    
}
