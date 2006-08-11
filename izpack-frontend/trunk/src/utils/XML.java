/*
 * Created on Dec 8, 2004
 * 
 * $Id: XML.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : XML.java 
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import exceptions.DocumentCreationException;
import exceptions.UnhandleableException;


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
        catch (ParserConfigurationException pce)
        {
            throw new UnhandleableException(pce);
        }        
        
        doc = builder.newDocument();
        
        return doc;
    }
    
    //TODO Exception handling
    public static Document createDocument(String filename) throws DocumentCreationException
    {
        DocumentBuilder builder;        
        Document document = null;
        
        try
        {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(new File(filename));            
            
            String URI = new File(filename).getParent();
                        
            if (URI == null)
                URI = "";
            else
                URI = URI + System.getProperty("file.separator");
            
            document.setDocumentURI(URI);
        }        
        catch (ParserConfigurationException pce)
        {
            throw new UnhandleableException(pce);
        }
        catch (SAXException e)
        {
            throw new UnhandleableException(e);
        }
        catch (FileNotFoundException fnfe)
        {   
            if (!filename.endsWith("authors.xml") && !filename.endsWith("recent.xml"))
                UI.showError(fnfe.getLocalizedMessage(), "File not found");
            
            throw new DocumentCreationException(fnfe);
        }
        catch (IOException e)
        {
            throw new UnhandleableException(e);
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
            //String systemValue = new File("dtd/installation.dtd").getPath();            
            //xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);
            
            //xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            // Write to a file
            xformer.transform(source, result);
        }
        catch (TransformerConfigurationException tce)
        {
            //TODO Make this a better handler - it's not really a fatal error
            //But, we can't save anything without a working transformer. So, maybe it is
            //UI.showError("An error occurred configuring the transformer for XML output", )
            throw new UnhandleableException(tce);
        }
        catch (TransformerException te)
        {
            //TODO Same as above
            throw new UnhandleableException(te);
        }
    }
    
    public static void writeXML(StreamResult stream, Node node, String xslFile)
    {
        try
        {   
            // Prepare the DOM document for writing
            Source source = new DOMSource(node);

            // Prepare the output file            
            Result result = stream;

            //Load the XSL file to make everything pretty
            StreamSource xsl = new StreamSource(new File(xslFile));
            // Write the DOM document to the file
            // Get Transformer
            //This won't properly indent - research indicates a 1.5 bug (so 1.3 JAXP?)            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer xformer = tFactory.newTransformer(xsl);
            
            //Set the output doctype
            //String systemValue = new File("dtd/installation.dtd").getPath();            
            //xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);
            
            //xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            // Write to a file
            xformer.transform(source, result);
        }
        catch (TransformerConfigurationException tce)
        {
            //TODO Make this a better handler - it's not really a fatal error
            //But, we can't save anything without a working transformer. So, maybe it is
            //UI.showError("An error occurred configuring the transformer for XML output", )
            throw new UnhandleableException(tce);
        }
        catch (TransformerException te)
        {
            //TODO Same as above
            throw new UnhandleableException(te);
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
        try
        {            
            //Use XPath to return the resource value by searching with it            
            return xpath.evaluate("//res[@id='" + id + "']/@src", document);            
        }
        catch (XPathExpressionException xpee)
        {
            //Chances are, this is because the resource is missing
            //Therefore, just give a null return value
        } 
        
        return null;
    }
    
    public static String getResourceValueAsPath(Document document, String id)
    {
        //TODO make this better - just to catch myself if I create a document badly
        if (document.getDocumentURI() == null)
            throw new RuntimeException("Error: Document URI does not exist");
        
        String resource = getResourceValue(document, id);
        
        if (resource != null)
        {
            resource = document.getDocumentURI() + resource;
        }
        
        return resource;
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
    
    /**
     * Structure:
     * <variables>
     *  <variable name="" value="">
     * </variables>
     *
     * Parse a full install xml file to load a specified variable. Does not handle multiple variables with
     * the same name
     * 
     * @param document The XML document
     * @param id The variable name to look for
     * @return Value of the src attribute 
     */
    public static String getVariable(Document document, String id) 
    {        
        try
        {   
            //Find the variable and return it's value
            return xpath.evaluate("//variable[@name='" + id + "']/@value", document);
        }
        catch (XPathExpressionException xpee)
        {
            //Chances are, this is because the resource is missing
            //Therefore, just give a null return value
        } 
        
        return null;
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
    private static XPath xpath = XPathFactory.newInstance().newXPath();
}
