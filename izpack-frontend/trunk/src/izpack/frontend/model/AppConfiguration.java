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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;

/**
 * Model to hold configuration for the application. Loads the configuration via
 * <code>loadConfiguration()</code> which typically parses in a configuration xml file.
 * 
 * @author Daniel Guggi
 */
public class AppConfiguration {
	// Tags (names) used within the config file
	public final static String T_I18N = "i18n";
	public final static String T_PAGE = "page";
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
	private XMLElement xml = null;

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
		Vector v = xml.getChildrenNamed(T_APP_VERSION);
		XMLElement elem = (XMLElement)v.get(0);
		return Float.parseFloat(elem.getContent());
	}
	
	/**
	 * Get the location of i18n bundle files.
	 * @return The location.
	 */
	public String getI18NBundleLocation() {
		Vector v = xml.getChildrenNamed(T_I18N);
		XMLElement elem = (XMLElement)v.get(0);
		return elem.getAttribute(A_BUNDLE_LOCATION);
	}
	
	/**
	 * Get the lang-code to use for i18n.
	 * @return String the lang-code or null if not specified.
	 */
	public String getI18NLangCode() {
		Vector v = xml.getChildrenNamed(T_I18N);
		XMLElement elem = (XMLElement)v.get(0);
		return elem.getAttribute(A_LANG_CODE);
	}
	
	/**
	 * Get the application name.
	 * @param includeVersion Flag to indicate wheter or not to include the version number.
	 * @return The application name.
	 */
	public String getAppName(boolean includeVersion) {
		Vector v = xml.getChildrenNamed(T_APP_NAME);
		XMLElement elem = (XMLElement)v.get(0);
		if (includeVersion) {
			return elem.getContent()+" "+getVersion();
		} else {
			return elem.getContent();
		}
	}
	
	/**
	 * Get the value of the <code>A_CLASS</code> attribute of the <code>XMLElement</code>
	 * with the given name.
	 * @param name
	 * @return
	 */
	public String getClass4Page(String name) {
		Vector v = xml.getChildrenNamed(T_UI);
		XMLElement uiElem = (XMLElement)v.get(0);
		Vector pageElems= uiElem.getChildrenNamed(T_PAGE);
		Iterator it = pageElems.iterator();
		XMLElement elem = null;
		String className =null;
		String pageName = null;
		while (it.hasNext()) {
			elem = (XMLElement)it.next();
			pageName = elem.getAttribute(A_NAME);
			className = elem.getAttribute(A_CLASS);
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
	private void loadConfiguration() {
		try {
			IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
			IXMLReader reader = StdXMLReader.fileReader(CONFIG_FILE);
			parser.setReader(reader);
			xml = (XMLElement)parser.parse();
			System.out.println("configuration loaded");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (XMLException e) {
			throw new RuntimeException(e);
		}

	}

}
