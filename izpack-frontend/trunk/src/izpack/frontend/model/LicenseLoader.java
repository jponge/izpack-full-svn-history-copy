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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;

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
	        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
	        IXMLReader reader = StdXMLReader.fileReader("conf/licenses.xml");
	        parser.setReader(reader);
	        XMLElement xml = (XMLElement) parser.parse();
	
	        //Load the authors array            
	        Vector licenceElem = xml.getChildrenNamed("license");	        
	        for (Iterator iter = licenceElem.iterator(); iter.hasNext();)
	        {
	            XMLElement element = (XMLElement) iter.next();
	            License lic = new License();
	            
	            lic.filename = element.getAttribute("file");
	            lic.name = element.getFirstChildNamed("name").getContent();
	            
	            //Why does only 1.5 have parseBoolean()?
	            //Why is NanoXML retarded?
	            //Processing an element with PCDATA and children is completely different
	            //from having PCDATA alone
	            //Yay for more work, or something
	            
	            XMLElement modElement = element.getFirstChildNamed("modifications");       	            
	            	                
	            //Assume that specifying fields mean modifications are required	            
	            lic.modifications_required = modElement.hasChildren();
	                
	            if (lic.modifications_required)
	            {
	                int i = 0;
	                Vector reqFields = modElement.getChildren();
	                lic.modifiable_fields = new String[modElement.getChildrenCount()];
	                
		            for (Iterator iterator = reqFields.iterator(); iterator.hasNext(); i++)
	                {
	                    XMLElement field = (XMLElement) iterator.next();
	                    lic.modifiable_fields[i] = field.getContent(); 
	                }		            
	            }	            	           
	            
	            lic.gplCompatible = Boolean.valueOf( element.getFirstChildNamed("gpl_compatible").getContent() ).booleanValue();
	            if (lic.gplCompatible)
	                gplCompat.add(lic);
	            else
	                gplUnCompat.add(lic);
	            
	            System.out.println(lic);
	            System.out.println("");
	        }      
	    
	        return new ArrayList[]{gplCompat, gplUnCompat};
	    }
	    catch (ClassNotFoundException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (InstantiationException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (IllegalAccessException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (FileNotFoundException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (IOException e)
	    {
	        throw new RuntimeException(e);
	    }
	    catch (XMLException e)
	    {
	        throw new RuntimeException(e);
	    }
    }
}
