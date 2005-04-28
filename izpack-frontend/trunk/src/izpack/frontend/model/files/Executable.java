/*
 * Created on Apr 12, 2005
 * 
 * $Id: Executable.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Executable.java 
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
package izpack.frontend.model.files;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;



/**
 * @author Andy Gombos
 */
public class Executable implements PackElement
{
    public String target = "", execClass = "", type = "", stage = "", failure = "", keep = "", args = "";

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.ElementModel#writeXML()
     */
    public Document writeXML()
    {
        Document doc = XML.getNewDocument();
        
        Element executable = XML.createElement("executable", doc);
        
        executable.setAttribute("targetfile", target);
        
        setOptionalAttribute(executable, "class", execClass);
        setOptionalAttribute(executable, "type", type);
        setOptionalAttribute(executable, "stage", stage);
        setOptionalAttribute(executable, "failure", failure);
        setOptionalAttribute(executable, "keep", keep);
        
        Element argsElem = XML.createElement("args", doc);
        Element arg = XML.createElement("arg", doc);
        arg.setAttribute("arg", args);
        
        argsElem.appendChild(arg);
        executable.appendChild(argsElem);
        
        doc.appendChild(executable);
        return doc;
    }
    
    private void setOptionalAttribute(Element elem, String attrName, String value)
    {
        if ((value != null) && !value.equals(""))
            elem.setAttribute(attrName, value);
    }
}
