/*
 * $Id: AppBase.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : AppBase.java 
 * Description : The interface for a frame of the
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
package izpack.frontend.view;

import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.stages.Stage;
import izpack.frontend.view.stages.panels.IzPanel;

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
