/*
 * Created on Apr 12, 2005
 * 
 * $Id: Parseable.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Parseable.java 
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
public class Parsable implements PackElement
{
    public String targetfile, type = "plain", encoding;
    public OS os;    

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.PackElement#writeXML()
     */
    public Element writeXML(Document doc)
    {   
        Element parsable = XML.createElement("parsable", doc);
        parsable.setAttribute("targetfile", targetfile);
        parsable.setAttribute("type", type);
        
        if (encoding != null && !encoding.equals(""))
            parsable.setAttribute("encoding", encoding);
        
        os.createXML(doc, parsable);
        
        return parsable;
    }

    public void initFromXML(Node elementNode)
    {
        NamedNodeMap attributes = elementNode.getAttributes();
        
        //TODO handle error about missing required attributes
        targetfile = attributes.getNamedItem("targetfile").getNodeValue();
        
        if (attributes.getNamedItem("encoding") != null)
            encoding = attributes.getNamedItem("encoding").getNodeValue();
        
        os = new OS();
        os.initFromXML(elementNode);        
    }
}
