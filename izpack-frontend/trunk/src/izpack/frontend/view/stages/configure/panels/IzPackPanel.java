/*
 * $Id: IzPackPage.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : IzPackPage.java 
 * Description : Defines the basic, but still abstract <code>Page</code> implementation for swing applications.
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

package izpack.frontend.view.stages.configure.panels;

import izpack.frontend.controller.GUIController;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.stages.IzPackStage;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.JPanel;

/**
 * Defines a basic implementation for swing applications.
 * 
 * Implements the <code>Observer</code> interface to support the page with i18n and 
 * other models that my need to update the page.
 * 
 * @author Daniel Guggi
 */
public abstract class IzPackPanel extends JPanel implements IzPanel {
	/** Collection contains components/element of this page. */
	private ArrayList elements = null;
    protected IzPackStage stage;
	
    public IzPackPanel()
    {
        this(null);
    }
    
	/**
	 * Constructor. Observers <code>LangResources</code>.
	 *
	 */
	public IzPackPanel(IzPackStage parentStage) {
		super();
		
		// set up the arraylist
		elements = new ArrayList();
		// init all components
	    stage = parentStage;
		initComponents();				
	}
	
	/**
	 * Add an element to the page.
	 */
	public Object addElement(String name, Object o) {
		Component c = (Component)o;
		c.setName(name);
		elements.add(c);
		return c;
	}
	
	/**
	 * Retrieve an element that belongs to this page.
	 */
	public Object getElement(String name) {
		Iterator it = elements.iterator();
		Component c = null;
		while (it.hasNext()) {
			c = (Component)it.next();
			if (c.getName().equalsIgnoreCase(name)) {
				// component found
				return c;
			}
		}
		// nothing found
		return null;
	}
	
	/**
	 * Get the lang-resources object.
	 * @return The language resource model.
	 */
	public LangResources langResources() {
		return GUIController.getInstance().langResources();
	}
	
	/**
	 * Get the application configuration.
	 * @return the application configuration model.
	 */
	public AppConfiguration appConfiguration() {
		return GUIController.getInstance().appConfiguration();
	}
 } 



