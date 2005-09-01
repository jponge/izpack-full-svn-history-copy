/*
 * Created on Jun 14, 2005
 * 
 * $Id: PanelSelectionModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelectionModel.java 
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
package izpack.frontend.model.stages;

import javax.swing.ListModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import izpack.frontend.model.SelectListModel;
import izpack.frontend.view.renderers.ImageLabel;

/**
 * @author Andy Gombos
 */
public class PanelSelectionModel extends SelectListModel implements StageDataModel
{

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#writeToXML()
     */
    public Document writeToXML()
    {
        Element root = XML.createRootElement("installation");
        Document rootDoc = root.getOwnerDocument();
        
        root.setAttribute("version", "1.0");
        
        //Create the interesting XML data
        Element panels = XML.createRootElement("panels");
        Document panelsDoc = panels.getOwnerDocument();
        
        for (int i = 0; i < getSize(); i++)
        {
            ImageLabel il = (ImageLabel) getElementAt(i);
            String classname = il.getClassname();
            
            Element panel = XML.createElement("panel", panelsDoc);
            panel.setAttribute("classname", classname);
            
            panels.appendChild(panel);
        }
        
        root.appendChild(rootDoc.importNode(panels, true));
        
        return rootDoc;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document doc)
    {
        // TODO Auto-generated method stub
        
    }

}
