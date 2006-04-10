/*
 * Created on Aug 31, 2005
 * 
 * $Id: XMLCreator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : XMLCreator.java 
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

package izpack.frontend.controller;

import izpack.frontend.view.stages.IzPackStage;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;
import utils.XMLAggregator;

public class XMLCreator
{
    /**
     * Create an aggregator that can create a valid XML install file based on the data from
     * stages. Manages order, asking the stages to create their XML, merging all the elements together, etc.
     * 
     * @param stageList List of stages to ask to generate XML
     */
    public XMLCreator(ArrayList<IzPackStage> stageList)
    {
        xmlChunks = new ArrayList<Element>();
        this.stageList = stageList;        
    }
    
    public Document createInstallXML()
    {
        Document doc = XML.createDocument();
        
        
        loadXMLChunksFromStages(doc);        
        
        Element root = XML.createElement("installation", doc);        
        root.setAttribute("version", "1.0");
        
        doc.appendChild(root);
        
        for (Element elem : xmlChunks)
        {            
            root.appendChild(elem);
        }
        
        return XMLAggregator.aggregateDocument(doc);
    }
    
    /**
     * Make each stage create it's required XML chunk, and store it for processing
     */
    private void loadXMLChunksFromStages(Document doc)
    {   
        for (IzPackStage stage : stageList)
        {   
            Element[] elem = stage.createInstallerData(doc);
            
            if (elem != null)
            {
                for (int i = 0; i < elem.length; i++)
                {
                    xmlChunks.add(elem[i]);
                }                
            }
        }
    }
        
    private ArrayList<Element> xmlChunks;
    private ArrayList<IzPackStage> stageList;    
}
