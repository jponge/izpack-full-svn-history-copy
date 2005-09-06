package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDocFlattener {
	public static XMLElementList flattenDocument(Document doc)
	{
		XMLElementList elements = new XMLElementList();
		
		Element root = doc.getDocumentElement();
		
		processSubtree(root, elements);
		
		return elements;
	}
	
	private static void processSubtree(Node start, XMLElementList elements)
	{
		//Add this node to the list
		if (start.getNodeType() == Node.ELEMENT_NODE)
		{
			elements.add(start);
			
			//Get the child nodes
			NodeList children = start.getChildNodes();
			
			//If there are children, process them
			if (children != null)
			{
				for (int childIndex = 0; childIndex < children.getLength(); childIndex++)
				{
					Node child = children.item(childIndex);
					
					processSubtree(child, elements);
				}
			}
		}        
	}
}
