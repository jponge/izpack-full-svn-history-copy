/*
 * Created on Apr 11, 2005
 * 
 * $Id: FileModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FileModel.java 
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
public class FileModel extends PackFileModel
{

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.PackElement#writeXML()
     */
    public Element writeXML(Document doc)
    {           
        Element file = XML.createElement("singlefile", doc);
        file.setAttribute("src", source);
        file.setAttribute("target", target);
        
        if (!override.equals(""))
            file.setAttribute("override", override);
        
        os.createXML(doc, file);
        
        return file;
    }

    public void initFromXML(Node elementNode)
    {
        NamedNodeMap attributes = elementNode.getAttributes();
        
        source = attributes.getNamedItem("src").getNodeValue();
        
        target = attributes.getNamedItem("target").getNodeValue();
        
        os = new OS();
        os.initFromXML(elementNode);
        
        if (attributes.getNamedItem("override") != null)
            override = attributes.getNamedItem("override").getNodeValue();
        
    }

}
