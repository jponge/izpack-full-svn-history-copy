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

import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.PanelInfoManager;
import izpack.frontend.model.SelectListModel;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XML;

/**
 * @author Andy Gombos
 */
public class PanelSelectionModel extends SelectListModel implements StageDataModel
{

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#writeToXML()
     */
    public Element[] writeToXML(Document doc)
    {   
        //Create the interesting XML data
        Element panels = XML.createElement("panels", doc);        
        
        for (int i = 0; i < getSize(); i++)
        {
            PanelInfo panelModel = (PanelInfo) get(i);            
            
            Element panel = XML.createElement("panel", doc);
            panel.setAttribute("classname", panelModel.getClassname());
            
            panels.appendChild(panel);
        }
        
        return new Element[]{panels};
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document doc)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        HashMap<String, PanelInfo> availablePanels = PanelInfoManager.getAvailablePanelMap();
        
        try
        {
            NodeList panels = (NodeList) xpath.evaluate("//panels/panel", doc, XPathConstants.NODESET);
            
            for (int i = 0; i < panels.getLength(); i++)
            {
                Node panel = panels.item(i);
                
                String classname = panel.getAttributes().getNamedItem("classname").getNodeValue();
                
                addElement(availablePanels.get(classname));
            }
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
}
