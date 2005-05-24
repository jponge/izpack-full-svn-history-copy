/*
 * $Id: Page.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : Page.java 
 * Description : Interface that specifies how a Page has to look like
 * applications. Author's email : dani@gueggs.net
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

import java.util.Observer;

import com.jgoodies.validation.ValidationResult;


/**
 * Defines how pages have to look like.
 * 
 * @author Daniel Guggi
 */
public interface IzPanel extends Observer {
	/**
	 * Add a element to the private elements collection.
	 * @param name The name of the element.
	 * @param elem The element itself.
	 * @return The element.
	 */
    public Object addElement(String name, Object elem);

    
    /**
     * Get an element by its name.
     * @param name The name of the Element.
     * @return The Element.
     */
    public Object getElement(String name);

    /**
     * Initialize the page.
     */
    public void initComponents();

    /**
     * Set the name for this page.
     * @param name The name.
     */
    public void setName(String name);
    
    /**
     * Retrieve the name for this page.
     * @return The name of the page.
     */
    public String getName();    
}





