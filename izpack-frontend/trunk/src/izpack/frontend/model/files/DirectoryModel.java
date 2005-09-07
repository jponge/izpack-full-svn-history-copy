/*
 * Created on Apr 11, 2005
 * 
 * $Id: FileModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileModel.java 
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import utils.XML;

/**
 * @author Andy Gombos
 */
public class DirectoryModel extends PackFileModel
{

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.PackElement#writeXML()
     */
    public Element writeXML(Document doc)
    {   
        Element dir = XML.createElement("file", doc);
        dir.setAttribute("src", source);
        dir.setAttribute("targetdir", target);
        
        if (!os.equals(""))
            dir.setAttribute("os", os);
        
        if (!override.equals(""))
            dir.setAttribute("override", override);
        
        return dir;
    }

    public void initFromXML(Node elementNode)
    {  
       NamedNodeMap attributes = elementNode.getAttributes();
       
       source = attributes.getNamedItem("src").getNodeValue();
       
       target = attributes.getNamedItem("targetdir").getNodeValue();
       
       if (attributes.getNamedItem("os") != null)
           os = attributes.getNamedItem("os").getNodeValue();       
       
       if (attributes.getNamedItem("override") != null)
           override = attributes.getNamedItem("override").getNodeValue();
    }
}
