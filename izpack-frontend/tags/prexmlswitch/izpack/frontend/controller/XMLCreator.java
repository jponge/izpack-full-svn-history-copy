/*
 * Created on Aug 31, 2005
 * 
 * $Id: XMLCreator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : XMLCreator.java 
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
package izpack.frontend.controller;

import izpack.frontend.view.stages.IzPackStage;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XML;

public class XMLCreator
{
    /**
     * Create an aggregator that can create a valid XML install file based on the data from
     * stages. Manages order, asking the stages to create thier XML, merging all the elements together, etc.
     * 
     * @param stageList List of stages to ask to generate XML
     */
    public XMLCreator(ArrayList<IzPackStage> stageList)
    {
        xmlChunks = new ArrayList<Document>();
        this.stageList = stageList;        
    }
    
    public Document createInstallXML()
    {
        loadXMLChunksFromStages();
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        Element root = XML.createRootElement("installation");
        Document rootDoc = root.getOwnerDocument();
        
        root.setAttribute("version", "1.0");
                
        for (Document doc : xmlChunks)
        {            
            try
            {
                Node node = (Node) xpath.evaluate("//installation", doc, XPathConstants.NODE);
                node = node.getFirstChild();
                
                Node importedNode = rootDoc.importNode(node, true);
                root.appendChild(importedNode);
            }
            catch (XPathExpressionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        System.err.println("**********************************");
        System.out.flush();
        System.err.flush();
        
        XML.printXML(rootDoc);
        System.err.println("**********************************");
        
        return null;
    }
    
    /**
     * Make each stage create it's required XML chunk, and store it for processing
     */
    private void loadXMLChunksFromStages()
    {
        for (IzPackStage stage : stageList)
        {   
            Document doc = stage.createInstallerData();
            
            if (doc != null)
                xmlChunks.add(doc);
        }
    }
        
    private ArrayList<Document> xmlChunks;
    private ArrayList<IzPackStage> stageList;
}
