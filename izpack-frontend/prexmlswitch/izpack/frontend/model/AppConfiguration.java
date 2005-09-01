/*
 * $Id: AppConfiguration.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : AppConfiguration.java 
 * Description : Model to hold the complete application configuration.
 * application. 
 * Author's email : dani@gueggs.net 
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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XML;

/**
 * Model to hold configuration for the application. Loads the configuration via
 * <code>loadConfiguration()</code> which typically parses in a configuration xml file.
 * 
 * @author Daniel Guggi
 */
public class AppConfiguration {
	// Tags (names) used within the config file
	public final static String T_I18N = "i18n";
	public final static String T_PANEL = "panel";
	public final static String T_STAGE = "stage";
	public final static String T_UI = "user-interface";
	public final static String T_APP_VERSION = "app-version";
	public final static String T_APP_NAME = "app-name";
	
	// Attributes used within the config file.
	public final static String A_CLASS = "class";
	public final static String A_LANG_CODE = "lang-code";
	public final static String A_BUNDLE_LOCATION = "bundle-location";
	public final static String A_NAME = "name";
	
	/** The config file name. */
	private static final String CONFIG_FILE = "conf/app-config.xml";

	/** The xml element. */
	private Document document;
	
	/** XPath stuff */
	XPath xpath = XPathFactory.newInstance().newXPath();

	/**
	 * Constructor calls <code>loadConfiguration()</code>.
	 *
	 */
	public AppConfiguration() {
		loadConfiguration();
	}
	
	/**
	 * Get the application version number.
	 * @return The version number.
	 */
	public float getVersion() {
		Element elem = getElement(T_APP_VERSION);
		return Float.parseFloat(elem.getTextContent());
	}
	
	/**
	 * Get the location of i18n bundle files.
	 * @return The location.
	 */
	public String getI18NBundleLocation() {
		Element elem = getElement(T_I18N);		
		return elem.getAttribute(A_BUNDLE_LOCATION);
	}
	
	/**
	 * Get the lang-code to use for i18n.
	 * @return String the lang-code or null if not specified.
	 */
	public String getI18NLangCode() {
		Element elem = getElement(T_I18N);		
		return elem.getAttribute(A_LANG_CODE);
	}
	
	/**
	 * Get the application name.
	 * @param includeVersion Flag to indicate wheter or not to include the version number.
	 * @return The application name.
	 */
	public String getAppName(boolean includeVersion) {
		Element elem = getElement(T_APP_NAME);		
		if (includeVersion) {		    
			return elem.getTextContent()+" "+getVersion();
		} else {		    
			return elem.getTextContent();
		}
	}
	
	/**
	 * Get the value of the <code>A_CLASS</code> attribute of the <code>XMLElement</code>
	 * with the given name.
	 * @param name
	 * @return
	 */
	public String getClass4Page(String name)
	{		
	    NodeList pages = null;
        try
        {
            pages = (NodeList) xpath.evaluate("//" + T_UI + "/" + T_PANEL, document, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (int i = 0; i < pages.getLength(); i++)
		{
			Element elem = (Element) pages.item(i);
			
			String pageName = elem.getAttribute(A_NAME);
			String className = elem.getAttribute(A_CLASS);
			if (pageName.equalsIgnoreCase(name) && className != null) {
				// found the className			    
				return className;
			}
		}
		// nothing found	    
		return null;
	}
	
	/**
	 * Load the configuration.
	 *@throws RuntimeException If error occours.
	 */
	private void loadConfiguration()
	{
	    document = XML.createDocument(CONFIG_FILE);
	    System.out.println("configuration loaded");
	}

    public String getClass4Stage(String name)
    {
        NodeList stages = null;
        try
        {
            stages = (NodeList) xpath.evaluate("//" + T_UI + "/" + T_STAGE, document, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (int i = 0; i < stages.getLength(); i++)
		{
			Element elem = (Element) stages.item(i);
			
			String stageName = elem.getAttribute(A_NAME);
			String className = elem.getAttribute(A_CLASS);
			if (stageName.equalsIgnoreCase(name) && className != null) {
				// found the className			    
				return className;
			}
		}
		// nothing found	 
		return null;        
    }

    private Element getElement(String name)
    {
        try
        {
            return (Element) xpath.evaluate("//" + name, document, XPathConstants.NODE);
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}
