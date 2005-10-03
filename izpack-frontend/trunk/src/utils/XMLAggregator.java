/*
 * Created on Apr 3, 2005
 * 
 * $Id: XMLAggregator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : XMLAggregator.java 
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
