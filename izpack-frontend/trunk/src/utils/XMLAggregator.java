/*
 * Created on Apr 3, 2005 $Id: XMLAggregator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos File : XMLAggregator.java Description : TODO
 * Add description Author's email : gumbo@users.berlios.de Licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Andy Gombos
 */
public class XMLAggregator
{
    public XMLAggregator(Document doc)
    {
        Node root = doc.getDocumentElement();
        
        ArrayList<MergeableElements> duplicates = findCombinableNodes(root);
        
        System.out.println(duplicates);
        
        for (MergeableElements elements : duplicates)
        {
            elements.merge();
        }
        
        XML.printXML(doc);        
    }

    private ArrayList<MergeableElements> findCombinableNodes(Node root)
    {        
        
        ArrayList<MergeableElements> duplicates = new ArrayList<MergeableElements>();
        
        NodeList children = root.getChildNodes();
        
        for (int i = 0; i < children.getLength(); i++)
        {   
            for (int j = 0; j < children.getLength(); j++)
            {   
                if (i != j && children.item(i).getNodeType() == Node.ELEMENT_NODE && children.item(j).getNodeType() == Node.ELEMENT_NODE &&
                                areNodesEqual(children.item(i), children.item(j)))
                {   
                    MergeableElements me = new MergeableElements(children.item(i), children.item(j));
                    
                    if (!duplicates.contains(me))
                        duplicates.add(me);
                }
            }
        }
        
        for (int i = 0; i < children.getLength(); i++)
        {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
            {   
                duplicates.addAll(findCombinableNodes(children.item(i)));
            }
        }
        
        return duplicates;
    }
    
    private boolean areNodesEqual(Node n1, Node n2)
    {
        if (! n1.getNodeName().equals(n2.getNodeName()))
            return false;
        
        NamedNodeMap attr1 = n1.getAttributes();
        NamedNodeMap attr2 = n2.getAttributes();
        
        if (attr1 != null && attr2 != null)
        {
            if (attr1.getLength() == attr2.getLength())
            {
                for (int i = 0; i < attr1.getLength(); i++)
                {
                    Node attrNode1 = attr1.item(i);
                    Node attrNode2 = attr2.getNamedItem(attrNode1.getNodeName());
                    
                    if (attrNode2 == null)
                        return false;
                    
                    if (! attrNode1.getNodeValue().equals(attrNode2.getNodeValue()) )
                        return false;
                }
            }
        }
        else if ( (attr1 != null && attr2 == null) || (attr1 == null && attr2 != null))
            return false;
        
        if (n1.getNodeValue() != null && !n1.getNodeValue().equals(n2.getNodeValue()))
            return false;
        
        if (! n1.getOwnerDocument().equals(n2.getOwnerDocument()))
            return false;
        
        return true;
    }
    
    private class MergeableElements
    {
        public MergeableElements(Node n1, Node n2)
        {
            this.n1 = n1;
            this.n2 = n2;
        }
        
        public void merge()
        {
            Node source = n1;
            Node dest = n2;                    
            
            NodeList sourceChildren = source.getChildNodes();
            
            for (int k = 0; k < sourceChildren.getLength(); k++)
            {
                if (sourceChildren.item(k).getNodeType() == Node.ELEMENT_NODE)
                {   
                    dest.appendChild(sourceChildren.item(k));
                }
            }               
        
            if (source.getParentNode() != null)
                source.getParentNode().removeChild(source);
        }
        
        @Override
        public boolean equals(Object obj)
        {
            MergeableElements me = (MergeableElements) obj;
            
            return ( (n1.isSameNode(me.n1) && n2.isSameNode(me.n2)) || (n1.isSameNode(me.n2) && n2.isSameNode(me.n1)) );
        }
        
        @Override
        public String toString()
        {
            return "{" + n1 + "=>" + n2 + "}";
        }
        
        public Node n1;
        public Node n2;
    }
}
