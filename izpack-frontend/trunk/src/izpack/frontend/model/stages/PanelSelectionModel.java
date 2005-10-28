/*
 * Created on Jun 14, 2005
 * 
 * $Id: PanelSelectionModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PanelSelectionModel.java 
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

package izpack.frontend.model.stages;

import izpack.frontend.controller.PanelInfoManager;
import izpack.frontend.model.PanelInfo;
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
import exceptions.UnhandleableException;

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
            PanelModel panelModel = (PanelModel) get(i);            
            
            Element panel = XML.createElement("panel", doc);
            panel.setAttribute("classname", panelModel.configData.getClassname());
            
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
                
                PanelModel model = new PanelModel();
                model.configData = availablePanels.get(classname);
                
                if (model.configData == null)
                    System.out.println(panel.getAttributes().getNamedItem("classname"));
                
                model.valid = false;
                
                addElement(model);
            }
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }        
    }
}
