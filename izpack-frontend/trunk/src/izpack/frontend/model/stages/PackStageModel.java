/*
 * Created on Jun 14, 2005
 * 
 * $Id: PackStageModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackStageModel.java 
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

import izpack.frontend.model.PackModel;

import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.XML;
import exceptions.UnhandleableException;

/**
 * @author Andy Gombos
 */
public class PackStageModel extends DefaultTableModel implements StageDataModel
{

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#writeToXML()
     */
    public Element[] writeToXML(Document doc)
    {                
        //Create the interesting XML data
        Element packs = XML.createElement("packs", doc);        
        
        for (int row = 0; row < getRowCount(); row++)
        {
            Object rowObj = getValueAt(row, 0);
            
            //If we have a valid pack, write it's XML representation out
            if (rowObj != null)
            {
                Element pack = ( (PackModel) rowObj).writePack(doc);
                
                packs.appendChild(pack);
            }
        }
        
        return new Element[]{packs};
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document doc)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();        
        
        
        //Remove the spacer rows
        for (int i = getRowCount() - 1; i >= 0; i--)
            removeRow(i);
        
        try
        {
            NodeList packs = (NodeList) xpath.evaluate("//packs/pack", doc, XPathConstants.NODESET);
            NodeList desc  = (NodeList) xpath.evaluate("//packs/pack/description", doc, XPathConstants.NODESET);
            
            for (int i = 0; i < packs.getLength(); i++)
            {                
                PackModel pack = new PackModel();
                pack.initFromXML(i, packs.item(i), desc.item(i));
                
                insertRow(i, new Object[]{pack});                
            }
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }
        
        
    }
}
