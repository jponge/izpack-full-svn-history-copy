/*
 * Created on Mar 23, 2005
 * 
 * $Id: ConfigurePage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ConfigurePage.java 
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

package izpack.frontend.view.stages.configure.panels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Andy Gombos
 */
public interface ConfigurePanel
{
    /** 
     *  Create an XML segment for the install file to be compiled.  Follows the DTD.
     * @param doc TODO
     * 
     *  @return An Element that forms the current XML tree
     */
    public Element createXML(Document doc);
    
    /**
     *	Initialize panel values from a ready-to-compile installer file.  The panel must
     *	extract the relevant XML structure, and parse it.
     *  
     * @param xmlFile The entire XML file document
     */
    public void initFromXML(Document xmlFile);
    
    /**
     * 	Validate the page.  This process determines whether or not required fields are filled, dependencies are specified, etc.
     * 	Also handle chaging colors, values, etc. of components to notify the user of invalid sections.
     * 
     * @return true if the panel information is valid 
     */
    //public ValidationResult validatePanel();    
}
