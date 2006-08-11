/*
 * Created on Aug 9, 2006
 * 
 * $Id: OS.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : OS.java 
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

package izpack.frontend.model;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XML;

import com.jgoodies.binding.beans.Model;

public class OS extends Model
{   
    boolean windows, unix, mac;
    
    public boolean isMac()
    {
        return mac;
    }

    public boolean isUnix()
    {
        return unix;
    }

    public boolean isWindows()
    {
        return windows;
    }

    public void setMac(boolean mac)
    {
        this.mac = mac;
        firePropertyChange("mac", false, mac);
    }

    public void setUnix(boolean unix)
    {
        this.unix = unix;
        firePropertyChange("unix", false, unix);
    }

    public void setWindows(boolean windows)
    {
        this.windows = windows;
        firePropertyChange("windows", false, windows);
    }
    
    @Override
    public String toString()
    {
        return "Windows: " + windows + " Unix: " + unix + " Mac: " + mac;
    }

    public void createXML(Document doc, Element packElement)
    {        
        if (windows)
        {            
            createOSElement(doc, packElement, "windows");
        }            
        if (unix)
        {            
            createOSElement(doc, packElement, "unix");
        }
        if (mac)
        {            
            createOSElement(doc, packElement, "mac");
        }
    }

    private void createOSElement(Document doc, Element packElement, String osFamily)
    {        
        Element root = XML.createElement("os", doc);
        root.setAttribute("family", osFamily);
            
        packElement.appendChild(root);
    }
    
    public void initFromXML(Node node)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try
        {
            initFromXML((NodeList) xpath.evaluate("os", node, XPathConstants.NODESET));
        }
        catch (XPathExpressionException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initFromXML(NodeList os)
    {
        for (int i = 0; i < os.getLength(); i++)
        {
            NamedNodeMap attrs = os.item(i).getAttributes();
            
            if (attrs.getNamedItem("family") != null)
            {
                String family = attrs.getNamedItem("family").getNodeValue();
                
                if (family.equalsIgnoreCase("windows"))
                    windows = true;
                else if (family.equalsIgnoreCase("unix"))
                    unix = true;
                else if (family.equalsIgnoreCase("mac"))
                    mac = true;
            }
        }        
    }
}
