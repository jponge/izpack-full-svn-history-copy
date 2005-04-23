/*
 * $Id: IzPackPage.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : IzPackPage.java 
 * Description : Defines the basic, but still abstract <code>Page</code> implementation for swing applications.
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
package izpack.frontend.view.stages.panels;

import izpack.frontend.controller.GUIController;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;

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
	
	/**
	 * Constructor. Observers <code>LangResources</code>.
	 *
	 */
	public IzPackPanel() {
		super();
		// i18n
		langResources().addObserver(this);
		// set up the arraylist
		elements = new ArrayList();
		// init all components
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
	 * Update the static text for this page. This method gets invoked if the <code>LangResources</code> object
	 * invokes the <code>update(Observable o, Object arg)</code> method.
	 */
	protected void updateStaticText() {
		System.out.println("update static text invoked!");
	}
	
	/**
	 * Gets invoked if the <code>LangResources</code> needs to update
	 * the page. To provide this page with different <code>Observable</code> objects,
	 * someone has to override this method to take appropriate actions when updated
	 * by an observable object.
	 */
	public void update(Observable o, Object arg) {
		// update language stuff
		if (o instanceof LangResources) {
			updateStaticText();
		}
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



