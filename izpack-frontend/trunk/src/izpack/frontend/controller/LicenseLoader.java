/*
 * Created on Nov 30, 2004
 * 
 * $Id: LicenceLoader.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LicenceLoader.java 
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

package izpack.frontend.controller;

import izpack.frontend.model.LicenseModel;

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

import utils.UI;
import exceptions.UnhandleableException;

/**
 * @author Andy Gombos
 */
public class LicenseLoader
{
    public static ArrayList<LicenseModel> loadLicences()
    {        
        ArrayList<LicenseModel> licenses = new ArrayList<LicenseModel>(6);
        
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
	            
	            lic.filename = xpath.evaluate("//license[" + (i + 1) + "]/@fileBase", document);
	            	            
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
	        	
                licenses.add(lic);
	        }
	        
	        return licenses;
	    }	
        catch (ParserConfigurationException e)
        {
            throw new UnhandleableException(e);
        }
        catch (SAXException e)
        {
            throw new UnhandleableException(e);
        }
        catch (IOException e)
        {
            UI.showError("Error loading licenses: " + e.getLocalizedMessage(), "Error loading license");
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }
        
        return null;
    }
}
