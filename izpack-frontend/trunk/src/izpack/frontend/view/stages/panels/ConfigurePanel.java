/*
 * Created on Mar 23, 2005
 * 
 * $Id: ConfigurePage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ConfigurePage.java 
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
package izpack.frontend.view.stages.panels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Andy Gombos
 */
public interface ConfigurePanel
{
    /** 
     *  Create an XML segment for the install file to be compiled.  Follows the DTD.
     * 
     *  @return An Element that forms the current XML tree
     */
    public Element createXML();
    
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
    public boolean validatePage();    
}
