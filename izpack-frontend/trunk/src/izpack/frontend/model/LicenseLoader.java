/*
 * Created on Nov 30, 2004
 * 
 * $Id: LicenceLoader.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LicenceLoader.java 
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
package izpack.frontend.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Andy Gombos
 */
public class LicenseLoader
{
    public static ArrayList[] loadLicences()
    {
        ArrayList gplCompat = new ArrayList(4);
        ArrayList gplUnCompat = new ArrayList(2);
        
	    try
	    {
	        //Try using the JAXP stuff with XPath
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File("conf/licenses.xml"));		
			XPath xpath = XPathFactory.newInstance().newXPath();			
			
	        //Get the number of licenses            
	        NodeList nodeSet = (NodeList) xpath.evaluate("//license", document, XPathConstants.NODESET);
	        
	        for (int i = 0; i < nodeSet.getLength(); i++)
	        {            
	            LicenseModel lic = new LicenseModel();	            
	            
	            //Use XPath to grab each value
	            //I don't think this is very efficient, but it is only done once
	            
	            lic.filename = xpath.evaluate("//license[" + (i + 1) + "]/@file", document);            
	            	            
	            lic.name = xpath.evaluate("//license[" + (i + 1) + "]/name", document);
	            
	            lic.gplCompatible = Boolean.parseBoolean(xpath.evaluate("//license[" + (i + 1) + "]/gpl_compatible", document) );
	            	            
	            //If modification elements exist, there are parts to change
	            //Grab those parts            
	            NodeList fields = (NodeList) xpath.evaluate("//license[" + (i + 1) + "]/modifications/field", document, XPathConstants.NODESET);
	            if (fields.getLength() != 0)
	            {	                                
	                lic.modifications_required = true;

	                String fieldNames[] = new String[fields.getLength()];
	                for (int j = 0; j < fields.getLength(); j++)
	                {
	                    fieldNames[j] = fields.item(j).getTextContent();	                 
	                }
	            }
	        
	        	if (lic.gplCompatible)
	                gplCompat.add(lic);
	            else
	                gplUnCompat.add(lic);
	        }
	        
	        return new ArrayList[]{gplCompat, gplUnCompat};
	    }	
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}
