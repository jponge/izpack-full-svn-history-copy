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

import izpack.frontend.model.OS;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import utils.XML;



/**
 * @author Andy Gombos
 */
public class Executable implements PackElement
{
    public String target = "", execClass = "", type = "", stage = "", failure = "", keep = "", args = "";
    public OS os;

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
        
        os.createXML(doc, executable);
        
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
        NamedNodeMap attributes = elementNode.getAttributes();
        
        if (attributes.getNamedItem("targetfile") != null)
            target = attributes.getNamedItem("targetfile").getNodeValue();
        if (attributes.getNamedItem("class") != null)
            execClass = attributes.getNamedItem("class").getNodeValue();
        if (attributes.getNamedItem("type") != null)
            type  = attributes.getNamedItem("type").getNodeValue();
        if (attributes.getNamedItem("stage") != null)            
            stage = attributes.getNamedItem("stage").getNodeValue();
        if (attributes.getNamedItem("failure") != null)
            failure = attributes.getNamedItem("failure").getNodeValue();
        if (attributes.getNamedItem("keep") != null)
            keep = attributes.getNamedItem("keep").getNodeValue();
        
        
        //TODO handle args
        //args = attributes.getNamedItem("src").getNodeValue();
        
        os = new OS();
        os.initFromXML(elementNode);
    }
}
