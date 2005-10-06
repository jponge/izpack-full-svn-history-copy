/*
 * Created on Jun 14, 2005
 * 
 * $Id: PackStageModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PackStageModel.java 
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

import izpack.frontend.model.PackModel;

import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.UnhandleableException;

import utils.XML;

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
        
        try
        {
            NodeList packs = (NodeList) xpath.evaluate("//packs/pack", doc, XPathConstants.NODESET);
            NodeList desc  = (NodeList)     xpath.evaluate("//packs/pack/description", doc, XPathConstants.NODESET);
            
            for (int i = 0; i < packs.getLength(); i++)
            {
                PackModel pack = new PackModel();
                pack.initFromXML(i, packs.item(i), desc.item(i));
                
                //Insert a row, but remove the empty one that was present originally
                insertRow(i, new Object[]{pack});
                removeRow(getRowCount() - i - 1);
            }
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }
        
        
    }
}
