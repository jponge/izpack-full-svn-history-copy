/*
 * Created on Apr 12, 2005
 * 
 * $Id: Executable.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Executable.java 
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

package izpack.frontend.model.files;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
    public Element writeXML(Document doc)
    {   
        Element executable = XML.createElement("executable", doc);
        
        executable.setAttribute("targetfile", target);
        
        setOptionalAttribute(executable, "class", execClass);
        setOptionalAttribute(executable, "type", type);
        setOptionalAttribute(executable, "stage", stage);
        setOptionalAttribute(executable, "failure", failure);
        setOptionalAttribute(executable, "keep", keep);
        
        Element argsElem = XML.createElement("args", doc);
        Element arg = XML.createElement("arg", doc);
        arg.setAttribute("value", args);
        
        argsElem.appendChild(arg);
        executable.appendChild(argsElem);
        
        return executable;
    }
    
    private void setOptionalAttribute(Element elem, String attrName, String value)
    {
        if ((value != null) && !value.equals(""))
            elem.setAttribute(attrName, value);
    }

    public void initFromXML(Node elementNode)
    {
        // TODO Auto-generated method stub
        
    }
}
