/*
 * Created on Sep 2, 2005 $Id: DTDBasedOrderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group File : DTDBasedOrderer.java
 * Description : TODO Add description Author's email : gumbo@users.berlios.de
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.XML;
import utils.XMLDocFlattener;
import utils.XMLElementList;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

public class DTDBasedOrderer implements DOMErrorHandler
{
    public DTDBasedOrderer(String dtdFilename)
    {        
        dtdModel = new DefaultTreeModel(new DefaultMutableTreeNode("DTD"));

        root = (DefaultMutableTreeNode) dtdModel.getRoot();
        
        try
        {
            FileReader reader = new FileReader(dtdFilename);

            DTDParser parser = new DTDParser(reader);

            DTD dtd = parser.parse(false);

            items = dtd.items;

            dtdElemTable = dtd.elements;

            for (int i = 0; i < items.size(); i++)
            {
                Object obj = items.elementAt(i);
                if (obj != null && obj instanceof DTDElement)
                {
                    DTDElement element = (DTDElement) obj;

                    addElement(root, element);
                }

            }

            TreeNode newRoot = root.getFirstChild();
            dtdModel.setRoot(newRoot);
            root = (DefaultMutableTreeNode) newRoot;

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

    private void addElement(DefaultMutableTreeNode parent, DTDElement element)
    {
        // Has child elements
        items.remove(element);
        if (element.content instanceof DTDSequence)
        {
            DTDSequence seq = (DTDSequence) element.content;

            Vector v = seq.getItemsVec();

            DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(
                            element.name);
            parent.add(curNode);

            for (Object obj : v)
            {
                DTDName name = (DTDName) obj;

                DTDElement elem = (DTDElement) dtdElemTable.get(name.value);

                if (elem == null) continue;

                addElement(curNode, elem);
            }
        }
        else
        {
            DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(
                            element.name);
            parent.add(curNode);
        }

    }

    public Document orderDocument(Document doc)
    {
        elements = XMLDocFlattener.flattenDocument(doc);

        Document sortedDoc = XML.createDocument();

        Node xmlRoot = elements.searchInList("installation").get(0);

        xmlRoot = sortedDoc.importNode(xmlRoot, false);
        sortedDoc.appendChild(xmlRoot);
        
        addNodes(xmlRoot, root);    
        
        sortedDoc.getDomConfig().setParameter("comments", false);
        sortedDoc.getDomConfig().setParameter("validate", true);
        sortedDoc.getDomConfig().setParameter("error-handler", this);        
        
        sortedDoc.normalizeDocument();

        return sortedDoc;
    }

    private void addNodes(Node parent, DefaultMutableTreeNode insertLocation)
    {
        for (int i = 0; i < insertLocation.getChildCount(); i++)
        {
            DefaultMutableTreeNode location = (DefaultMutableTreeNode) insertLocation
                            .getChildAt(i);

            XMLElementList xmlElements = elements
                            .searchInList((String) location.getUserObject());
            
            // Unable to find an appropriate element
            if (xmlElements.size() == 0)            
                continue;            

            for (Node node : xmlElements)
            {                   
                Node adopted = parent.getOwnerDocument().importNode(node, true);
                parent.appendChild(adopted);

                //If there are more children in the DTD we can add, do so
                if (location.getChildCount() != 0)
                    addNodes(node, location);
            }
        }
    }

    //DTD tree stuff    
    private DefaultTreeModel dtdModel;
    private DefaultMutableTreeNode root;
    private Hashtable dtdElemTable;
    private Vector items;

    //XML tree/storage stuff
    private XMLElementList elements;

    public boolean handleError(DOMError error)
    {
        System.out.println(error.getMessage());
        return false;
    }
}
