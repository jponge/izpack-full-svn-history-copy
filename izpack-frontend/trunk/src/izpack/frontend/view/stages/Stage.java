/*
 * Created on Jul 7, 2004
 * 
 * $Id: Stage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Stage.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
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
package izpack.frontend.view.stages;

import org.w3c.dom.Document;

/**
 * @author Andy Gombos
 */
public interface Stage
{
    //Initialization functions
    
    /**
     * Initialize the stage by creating any necessary components, pages, etc.
     */
    public void initializeStage(); 
    
    /**
     * Allow the stage to save its state, presumably so it can be
     * used to reinstate the panel.
     * 
     * @return An XML tree containing save data
     */
    public Document createXMLSaveData();
    
    /**
     * Reinitialize the panel given the XML tree
     * This is provided by the <code>StageManager</code>
     * 
     * @param data The XML save data
     */
    public void readXMLSaveData(Document data);
    
    /**
     *  Create the IzPack XML file for the compiler, or
     * null if this stage does not support being compiled.
     * 
     * @return The installer data, or null
     */
    public Document createInstallerData();
    
    /**
     * Validate all <code>Page</code>s this <code>Stage</code>
     * uses, or true if the stage requires no validation.
     *  
     * @return true if the Stage is valid
     */
    public boolean validateStage();    
    
    
    /**
     * Allow stages to communicate (call methods) easier.
     * Sort of a getInstance() for non-Singletons. 
     * 
     * @param stage The instance to register
     */
    public void registerStage(Object stage);
    
    /**
     * Return a stage instance given the type.
     * 
     * This could really use generics 
     * 
     * @param stage The stage type to find
     * @return The stage object
     */
    public IzPackStage getStage(Class stage);   
    
    public void setName(String name);
    
    public String getName();
}
