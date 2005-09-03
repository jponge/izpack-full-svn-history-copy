/*
 * Created on Sep 2, 2005
 * 
 * $Id: DTDBasedOrderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : DTDBasedOrderer.java 
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

public class DTDBasedOrderer
{
    public DTDBasedOrderer(String dtdFilename)
    {
        try
        {
            FileReader reader = new FileReader(dtdFilename);
            
            DTDParser parser = new DTDParser(reader);
            
            DTD dtd = parser.parse(true);
            
            Vector items = dtd.items;
            
            dtdModel = new DefaultTreeModel(new DefaultMutableTreeNode("DTD"));
            root = (DefaultMutableTreeNode) dtdModel.getRoot();
            
            for (Iterator iter = items.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                
                if (element instanceof DTDElement)
                {
                    DTDElement dtdElem = (DTDElement) element;
                    DTDItem dtdContent = dtdElem.content;
                    
                    //DTDSequence gives the ordered list of elements in the DTD
                    if (dtdContent instanceof DTDSequence)
                    {
                        DefaultMutableTreeNode dtdNode = new DefaultMutableTreeNode(dtdElem.name);
                        dtdElements.put(dtdElem.name, dtdNode);
                        root.add(dtdNode);
                        
                        DTDSequence seq = (DTDSequence) dtdContent;
                        
                        DTDItem dtdItems[] = seq.getItems();
                        
                        for (int i = 0; i < dtdItems.length; i++)
                        {                            
                            dtdNode.add(new DefaultMutableTreeNode(
                                            ( (DTDName) dtdItems[i] ).value
                                                               ));
                        }
                    }                        
                }
            }            
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Document orderDocument(Document doc)
    {
        elements = getXMLElements(doc);
        Document sortedDoc = XML.createDocument();
        
        //Find the installation node, since it's the root        
        Node root = sortedDoc.appendChild(
                        sortedDoc.adoptNode(elements.get("installation"))
                        );
        
        
        
        return sortedDoc;
    }
    
    private void addNodes(String name, Node root)
    {
        DefaultMutableTreeNode dtdNode = dtdElements.get(name);
        
        //Find the right node to add this new node to
        String parentName = (String) ((DefaultMutableTreeNode) dtdNode.getParent() ).getUserObject();
        
        int index = dtdNode.getParent().getIndex(dtdNode);        
        
        try
        {
            Node parent = (Node) xpath.evaluate("//" + parentName, root, XPathConstants.NODE);
            
            
            
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private HashMap<String, Node> getXMLElements(Node node)
    {
        HashMap<String, Node> docElements = new HashMap<String, Node>();
        
        NodeList nodeList = node.getChildNodes();
        
        if (nodeList != null)
        {
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                docElements.put(node.getNodeName(), node.cloneNode(false));
            }
            
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                    docElements.putAll(getXMLElements(nodeList.item(i)));
            }
        }
        
        return docElements;        
    }
    
    private XPath xpath = XPathFactory.newInstance().newXPath();
    private HashMap<String, Node> elements;
    private HashMap<String, DefaultMutableTreeNode> dtdElements;
    private DefaultTreeModel dtdModel;    
    private DefaultMutableTreeNode root;
}
