/*
 * $Id: AppBase.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : AppBase.java 
 * Description : The interface for a frame of the
 * application. 
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

package izpack.frontend.view;

import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.stages.Stage;
import izpack.frontend.view.stages.configure.panels.IzPanel;

import java.util.Observer;

/**
 * Defines how the base for the application has to look like. This interface
 * has to be implemented by specific application classes. <code>IzPackFrame</code>
 * implements this interface and provides the starting point for an application
 * that uses Swing to render the GUI.
 * 
 * Extends the <code>Observer</code> interface to support the base with i18n and 
 * other models that my need to update the frame.
 * 
 * @author Daniel Guggi
 */
public interface AppBase extends Observer {
	/**
	 * Init the components for the base.
	 */
	public void initComponents();

	/**
	 * Configure the user interface.
	 */
	public void configureUI();

	/**
	 * Get a page by a name. The createFlag specifies whether or not to create
	 * the page if it does not exist already.
	 * 
	 * @param name The name of the page.
	 * @param createFlag The flag that specifies wheter or not to create the
	 *                    page if it does not exist.
	 * @return The page associated with the given name or null if the page does
	 *               not exist and the createFlag was false.
	 */
	public IzPanel getPanel(String name, boolean createFlag);
	
	/**
	 * Get a stage by a name. The createFlag specifies whether or not to create
	 * the stage if it does not exist already.
	 * 
	 * @param name The name of the stage.
	 * @param createFlag The flag that specifies wheter or not to create the
	 *                    stage if it does not exist.
	 * @return The stage associated with the given name or null if the stage does
	 *               not exist and the createFlag was false.
	 */
	public Stage getStage(String name, boolean createFlag);

	
	/**
	 * Display the page with the given name.
	 * 
	 * @param name
	 */
	public void displayStage(String name);

	/**
	 * Get the languge resource model to support i18n.
	 * 
	 * @return The language resource model.
	 */
	abstract LangResources langResources();

	/**
	 * Get the application configuration model.
	 * 
	 * @return The app configuration model.
	 */
	public abstract AppConfiguration appConfiguration();
}
