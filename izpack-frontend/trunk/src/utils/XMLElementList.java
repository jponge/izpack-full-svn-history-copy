package utils;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class XMLElementList extends ArrayList<Node> {
	public XMLElementList(int i) {
		super(i);
	}

	public XMLElementList() {
		super();
	}

	public XMLElementList searchInList(String name) {
		XMLElementList matches = new XMLElementList(2);

		for (Node node : this) {
			if (node.getNodeName().equals(name))
				matches.add(node);
		}

		
		return matches;
	}
}
