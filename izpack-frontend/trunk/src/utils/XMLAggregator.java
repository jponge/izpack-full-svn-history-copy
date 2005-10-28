/*
 * Created on Apr 3, 2005
 * 
 * $Id: XMLAggregator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : XMLAggregator.java 
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

package utils;

import java.util.ArrayList;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Andy Gombos
 */
public class XMLAggregator
{
    public static Element mergeElements (Element top)
    {        
        storeParents(top);        
     
        for (int i = 0; i < parentNodes.size(); i++)
        {
            Element node = (Element) parentNodes.get(i);            
            
            int index = 0;
            if ( (index = containsNotElement(node, i) ) != -1)
            {   
                Element donor = (Element) parentNodes.get(index);
                Element source = node;
                Document sourceDoc = source.getOwnerDocument();
                
                NodeList donorChildren = donor.getChildNodes();
                for (int j = 0; j < donorChildren.getLength(); j++)
                {
                    //Copy and remove the donor node's children
                    Node importedDonor = sourceDoc.importNode(donorChildren.item(j), true);
                    donor.removeChild(donorChildren.item(j));
                    
                    source.appendChild(importedDonor);
                }
                
                //Remove the donor node
                parentNodes.remove(index);
                donor.getParentNode().removeChild(donor);
            }
            
        }
        
        //Remove the fake top node that was just to combine the different documents
        //For some reason, the document contains random text nodes
        //Search for the actual child node                        
        Node realTop = top.getFirstChild();
        
        Document doc = XML.getNewDocument();
        Node imported = doc.importNode(realTop, true);   
        doc.appendChild(imported);
        
        return (Element) imported;
    }
    
    private static void storeParents(Element start)
    {
        NodeList children = start.getChildNodes();
        
        if (children == null)
            return;
        
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (child.hasChildNodes())
            {                
                parentNodes.add(child);
            	storeParents( (Element) child );
            }
        }
    }
    
    private static int containsNotElement(Element node, int index)
    {
        for (ListIterator iter = parentNodes.listIterator(); iter.hasNext();)
        {
            Element element = (Element) iter.next();
            
            if (element.getNodeName().equals(node.getNodeName()))
            {                
                int idx = iter.nextIndex() - 1;
                
                if (idx != index)
                    return idx;
            }
        }
        
        return -1;
    }
    
    private static ArrayList parentNodes = new ArrayList();
}
