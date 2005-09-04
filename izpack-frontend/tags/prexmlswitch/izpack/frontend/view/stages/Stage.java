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

import izpack.frontend.controller.StageChangeListener;
import izpack.frontend.controller.validators.StageValidator;

import javax.swing.JPanel;

import org.w3c.dom.Document;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public interface Stage
{
    //Initialization functions
    /**
     * Initialize the stage by creating any necessary components, panels, etc.
     * not created at construction time
     * 
     * Executed just before display, so it should run relatively quickly
     */
    public void initializeStage(); 
          
    /**
     *  Create the IzPack XML file for the compiler, or
     * null if this stage does not support being compiled.
     * 
     * @return The installer data, or null
     */
    public Document createInstallerData();
    
    /**
     * Validate all <code>Panels</code>s this <code>Stage</code>
     * uses, or true if the stage requires no validation.
     *  
     * @return true if the Stage is valid
     */
    public ValidationResult validateStage();
    
    /**
     * Set the name of the stage used when needing to display what the stage is (buttons, etc)
     * 
     * @param name The stage name
     */   
    public void setName(String name);
    
    /**
     * Get the name of the stage used when needing to display what the stage is (buttons, etc)
     * @return The stage name
     */
    public String getName();
    
    public JPanel getLeftNavBar();
    
    public JPanel getTopNavBar();
    
    public JPanel getBottomInfoBar();
    
    /*
     * 
     * StageChange event stuff
     * 
     */
    
    public void addStageChangeListener(StageChangeListener stl);    
    public void removeStageChangeListener(StageChangeListener stl);
}
