/*
 * Created on Sep 2, 2005 $Id: DTDBasedOrderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos File : DTDBasedOrderer.java
 * Description : TODO Add description Author's email : gumbo@users.berlios.de
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
            //TODO Give ablility to search for it?
            UI.showError("Unable to find the DTD for the installer. Perhaps the installtion is corrupted.", "Unable to locate DTD");
        }
        catch (IOException e)
        {
            UI.showError(e.getLocalizedMessage(), "IOException");
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
