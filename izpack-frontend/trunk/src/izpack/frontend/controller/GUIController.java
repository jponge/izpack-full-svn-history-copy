/*
 * $Id: GUIController.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : GUIController.java 
 * Description : Provides the views with models and convinient support ;)
 * Author's email : dani@gueggs.net 
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
