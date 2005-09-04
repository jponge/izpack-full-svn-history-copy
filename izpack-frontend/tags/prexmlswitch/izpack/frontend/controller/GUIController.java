/*
 * $Id: GUIController.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : GUIController.java 
 * Description : Provides the views with models and convinient support ;)
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
package izpack.frontend.controller;

import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;

/**
 * Provides views with models.
 * This class implements the Singleton design pattern. So you get an instance via a call
 * to <code>GUIController.getInstance()</code>.
 * 
 * @author Daniel Guggi
 */
public class GUIController  {
	/** The language resource model.*/
	private LangResources activeLangResources = null;
	/** The application configuration model.*/
	private AppConfiguration appConfiguration = null;
	/** Singleton instance. */
	private static GUIController instance = null;
	
	/**
	 * Constructor. 
	 */
	private GUIController() {
		appConfiguration = new AppConfiguration();
		String lc = appConfiguration.getI18NLangCode();
		if (lc == null) {
			this.activeLangResources = new LangResources(appConfiguration.getI18NBundleLocation());
		} else {
			this.activeLangResources = new LangResources(appConfiguration.getI18NBundleLocation(), lc);
		}
	}

	/**
	 * Get a reference to the controller.
	 * @return The reference.
	 */
	public static GUIController getInstance() {
		if (instance == null) {
			instance = new GUIController();
		}
		return instance;
	}

	/**
	 * Get the language resource model.
	 * @return the language resource model.
	 */
	public LangResources langResources() {
		return this.activeLangResources;
	}

	/**
	 * Get the application resource model.
	 * @return the application resource model.
	 */
	public AppConfiguration appConfiguration() {
		return this.appConfiguration;
	}

}
