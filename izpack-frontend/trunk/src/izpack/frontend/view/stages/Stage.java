/*
 * Created on Jul 7, 2004
 * 
 * $Id: Stage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Stage.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
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

package izpack.frontend.view.stages;

import izpack.frontend.controller.StageChangeListener;

import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
     * @param doc TODO
     * 
     * @return The installer data, or null
     */
    public Element[] createInstallerData(Document doc);
    
    /**
     * Initialize the stage from a saved XML document
     * 
     * @param doc The loaded document to read from
     */
    public void initializeStageFromXML(Document doc);
    
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
