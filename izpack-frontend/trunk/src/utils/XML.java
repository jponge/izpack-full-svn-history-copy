/*
 * Created on Dec 8, 2004
 * 
 * $Id: XML.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : XML.java 
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Andy Gombos
 */
public class XML
{
    public static Document createDocument()
    {
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try
        {
            builder = factory.newDocumentBuilder();            
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        
        doc = builder.newDocument();
        
        return doc;
    }
    
    //TODO Exception handling
    public static Document createDocument(String filename)
    {
        DocumentBuilder builder;        
        Document document = null;
        
        try
        {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(new File(filename));
        }        
        catch (ParserConfigurationException e)
        {
        }
        catch (SAXException e)
        {
        }
        catch (FileNotFoundException fnfe)
        {            
        }
        catch (IOException e)
        {
        }
        
        return document;
    }
    
    public static void writeXML(String filename, Node node)
    {       
        writeXML(new StreamResult(filename), node);
    }
    
    public static void printXML(Node node)
    {
        writeXML(new StreamResult(System.out), node);
    }
    
    public static void writeXML(StreamResult stream, Node node)
    {
        try
        {   
            // Prepare the DOM document for writing
            Source source = new DOMSource(node);

            // Prepare the output file            
            Result result = stream;

            //Load the XSL file to make everything pretty
            StreamSource xsl = new StreamSource(new File("dtd/format.xsl"));
            // Write the DOM document to the file
            // Get Transformer
            //This won't properly indent - research indicates a 1.5 bug (so 1.3 JAXP?)            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer xformer = tFactory.newTransformer(xsl);
            
            //Set the output doctype
            String systemValue = new File("dtd/installation.dtd").getPath();            
            xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);
            
            //xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            // Write to a file
            xformer.transform(source, result);
        }
        catch (TransformerConfigurationException tce)
        {
            System.out.println("TransformerConfigurationException: " + tce);
        }
        catch (TransformerException te)
        {
            System.out.println("TransformerException: " + te);
        }
    }
    
    public static Document merge(Document doc1, Document doc2)
    {
        Element head1 = doc1.getDocumentElement();
        Element head2 = doc2.getDocumentElement();
        
        Document both = XML.getNewDocument();
        both.adoptNode(head1);
        both.adoptNode(head2);
        
        
        return both;
    }
    
    /**
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     *
	 * Parse a full install xml file to load a specified resource. Does not handle multiple resource entries with
	 * the same name
	 * 
     * @param document The XML document
     * @param id The resource ID to look for
     * @return Value of the src attribute
     */
    public static String getResourceValue(Document document, String id)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try
        {            
            //Search only for the <res> element we are interested in
            NodeList resource = (NodeList) xpath.evaluate("//res[@id='" + id + "']", document, XPathConstants.NODESET);
            
            if (resource.getLength() == 1)
            {
                String filename = xpath.evaluate("//res[1]/@src", resource.item(0));               
             
                return filename;
            }
        }
        catch (XPathExpressionException xpee)
        {
            UI.showError("Error while searching for resource id: " + id, "Error restoring state");
            xpee.printStackTrace();
        } 
        
        return null;
    }
    
    /**
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     * 
     * Create a resource tree to be integrated into the complete install file
     * 
     * @param id Resource id
     * @param src Resource value (src attribute)
     * @return The root (head) element
     */
    public static Element createResourceTree(String id, String src, Document doc)
    {
        //Create the head node
	    Element root = doc.createElement("resources");	    
	    
	    //Create the child node (the one we really want)
	    Element resource = doc.createElement("res");
	    resource.setAttribute("id", id);
	    resource.setAttribute("src", src);
	    root.appendChild(resource);
	    
	    return root;
    }
    
    public static Element createRootElement(String name)
    {
        Document doc = getNewDocument();
        Element elem = doc.createElement(name);
        doc.appendChild(elem);
        
        return elem; 
    }
    
    public static Element createElement(String name, Document d)
    {
        return d.createElement(name);
    }
    
    
    public static Document getDocument()
    {
        if (doc == null)
            doc = createDocument();
        
        return doc;
    }
    
    public static Document getNewDocument()
    {
        return createDocument();    
    }
    
    private static Document doc;
}
