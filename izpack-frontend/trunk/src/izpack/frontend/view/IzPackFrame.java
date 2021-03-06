/*
 * $Id: IzPackFrame.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : IzPackFrame.java Description : Main entry point for swing
 * applications. Author's email : dani@gueggs.net
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

import izpack.frontend.controller.FrameListener;
import izpack.frontend.controller.GUIController;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.Stage;
import izpack.frontend.view.stages.configure.panels.IzPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFrame;

/**
 * The entry point for applications using swing to render the GUI. This class
 * implements the Singleton design pattern. So you get an instance via a call
 * to <code>IzPackFrame.getInstance()</code>.
 * 
 * @author Daniel Guggi
 */
public class IzPackFrame extends JFrame implements AppBase {
	/** The only reference of the frame. */
	private static IzPackFrame instance = null;

	/** Contains elements (Components) of the page. */
	private ArrayList elements = new ArrayList();

	/**
	 * Private constructor. Singleton.
	 */
	private IzPackFrame() {
		super();
		this.setTitle(
			GUIController.getInstance().appConfiguration().getAppName(true));		
		// configure
		this.configureUI();
		// init
		this.initComponents();
		// display welcome page
		//this.displayStage(GUIConstants.STAGE_WELCOME);		
	}

	/**
	 * Get a reference to the instance.
	 * 
	 * @return The instance.
	 */
	public static IzPackFrame getInstance() {
		if (instance == null) {
			instance = new IzPackFrame();
		}
		return instance;
	}

	/**
	 * Get a page by its name.
	 * 
	 * @see AppBase#getPage()
	 */
	public IzPanel getPanel(String name, boolean createFlag) {
		Container cp = getContentPane();
		Component[] pages = cp.getComponents();
		for (int i = 0; i < pages.length; i++) {
			if (pages[i].getName().equalsIgnoreCase(name)) {
				// we found the page with the given name
				return (IzPanel)pages[i];
			}
		}
		// we didnt find the page
		if (createFlag) {		    
			String pageClass =
				GUIController.getInstance().appConfiguration().getClass4Page(
					name);		
			
			// create the page
			IzPanel page = null;
			try {
				page = (IzPanel)Class.forName(pageClass).newInstance();
				page.setName(name);
			} catch (InstantiationException e) {
				throw new RuntimeException(
					"Error while creating Page '" + name + "'!",
					e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
					"Error while creating Page '" + name + "'!",
					e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
					"Error while creating Page '" + name + "'!",
					e);
			}
			
            return page;
		} else {
			// we should not create the page, so return null
			return null;
		}
	}
	
	public Stage getStage(String name, boolean createFlag)
    {
        Container cp = getContentPane();
        Component[] stages = cp.getComponents();
        for (int i = 0; i < stages.length; i++)
        {
            if (stages[i].getName().equalsIgnoreCase(name)) {
            // we found the page with the given name
            return (Stage) stages[i]; }
        }
        // we didnt find the page
        if (createFlag)
        {            
            String stageClass = GUIController.getInstance().appConfiguration()
                    .getClass4Stage(name);
            // create the page
            Stage stage = null;
            try
            {
                stage = (Stage) Class.forName(stageClass).newInstance();
                stage.setName(name);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException("Error while creating Page '" + name
                        + "'!", e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Error while creating Page '" + name
                        + "'!", e);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException("Error while creating Page '" + name
                        + "'!", e);
            }
            
            return stage;
        }
        else
        {
            // we should not create the page, so return null
            return null;
        }
    }   

	/**
	 * Display the stage associated with the given name. If a stage with the
	 * given name does not exists it will by loaded automatically by <code>getStage(String name, boolean createFlag)</code>
	 * 
	 * @param name The name of the stage to display.
	 */
	public void displayStage(String name) {
		Container cp = getContentPane();
		//CardLayout layout = (CardLayout)cp.getLayout();
		//BorderLayout layout = (BorderLayout)cp.getLayout();

		IzPackStage stage = (IzPackStage)this.getStage(name, true);
		stage.initializeStage();
		stage.setSize(500, 500);

		//Hide the old stages
		Component existingPanels[] = cp.getComponents();
		for (int i = 0; i < existingPanels.length; i++) {
			if (existingPanels[i] instanceof IzPackStage)
				existingPanels[i].setVisible(false);
		}
		
		// add the stage to the container		
		cp.add(stage, BorderLayout.CENTER);
		
		// display the stage
		//layout.show(cp, stage.getName());
		pack();
		setVisible(true);
	}

	/**
	 * Initialize the frame. <code>CardLayout</code> is used as the main display manager, because
	 * its easy to display the single pages. Also adds the <code>izpack.frontend.FrameListener</code>.
	 */
	public void initComponents() {
		//CardLayout layout = new CardLayout();
	    //BorderLayout layout = new BorderLayout();
		//getContentPane().setLayout(layout);
		this.addWindowListener(new FrameListener());
	}

	/**
	 * Configure the user interface. This method is meant to setup the Look and Feel and
	 * similar things...
	 */
	public void configureUI() {
	    
	}	

	/**
	 * Get the application configuration.
	 * @return the application configuration model.
	 */
	public AppConfiguration appConfiguration() {
		return GUIController.getInstance().appConfiguration();
	}
	
	/**
	 * Get the lang-resources object.
	 * @return The language resource model.
	 */
	public LangResources langResources() {
		return GUIController.getInstance().langResources();
	}

	
	/**
	 * Application Startup
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		IzPackFrame frame = IzPackFrame.getInstance();
		System.out.println(frame.getLayout());		
		frame.pack();		
		frame.setSize(500,500);
		frame.setVisible(true);
	}    
}
