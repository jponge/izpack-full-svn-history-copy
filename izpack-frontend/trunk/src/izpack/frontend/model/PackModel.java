/*
 * Created on Apr 10, 2005
 * 
 * $Id: Pack.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Pack.java 
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

import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.FileOrderComparator;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.model.files.PackElement;
import izpack.frontend.model.files.Parsable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.UI;
import utils.XML;
import exceptions.UnhandleableException;

/**
 * @author Andy Gombos
 */
public class PackModel implements ElementModel
{
    public PackModel()
    {
        model = new DefaultTableModel(15, 1);
    }
    
    private String name = "", desc = "", id = "";
    private OS oses = new OS();
    private boolean required = true, preselected = true, loose = false;
    
    /**
     * @return Returns the desc.
     */
    public String getDesc()
    {
        return desc;
    }
    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }
    /**
     * @return Returns the loose.
     */
    public boolean isLoose()
    {
        return loose;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return Returns the os.
     */
    public OS getOS()
    {        
        return oses;
    }
    /**
     * @return Returns the preselected.
     */
    public boolean isPreselected()
    {
        return preselected;
    }
    /**
     * @return Returns the required.
     */
    public boolean isRequired()
    {
        return required;
    }
    /**
     * @param desc The desc to set.
     */
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * @param loose The loose to set.
     */
    public void setLoose(boolean loose)
    {
        this.loose = loose;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param os The os to set.
     */
    public void setOSes(OS os)
    {     
        this.oses = os;
    }
    
    /**
     * @param preselected The preselected to set.
     */
    public void setPreselected(boolean preselected)
    {
        this.preselected = preselected;
    }
    /**
     * @param required The required to set.
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }
    
    public Element writePack(Document doc)
    {
        Element pack = XML.createElement("pack", doc);        
        
        pack.setAttribute("name", name);
        pack.setAttribute("required", required ? "yes" : "no");
        pack.setAttribute("preselected", preselected ? "yes" : "no");
        pack.setAttribute("loose", loose ? "true" : "false");        
                
        if (!id.equals(""))        
            pack.setAttribute("id", id);
        
        Element descElem = XML.createElement("description", doc);
        descElem.setTextContent(desc);
        
        pack.appendChild(descElem);        
     
        oses.createXML(doc, pack);
        
        //Convert the model into a list so it can be sorted
        ArrayList elements = new ArrayList();
        for(int i = 0; i < model.getRowCount(); i++)
        {
            Object data = model.getValueAt(i, 0);
            
            if (data != null)
                elements.add(data);
        }
        
        Collections.sort(elements, new FileOrderComparator());
        
        for (Iterator iter = elements.iterator(); iter.hasNext();)
        {
            PackElement pe = (PackElement) iter.next();            

            Element packElem = pe.writeXML(doc);            

            pack.appendChild(packElem);

        }
        
        return pack;
    }
    
    public DefaultTableModel getFilesModel()
    {
        return model;
    }
    
    public void initFromXML(int packIndex, Node packNode, Node descNode)
    {    
        NamedNodeMap attributes = packNode.getAttributes();
        
        setName(getAttribute(attributes, "name"));
        
        setRequired(stringToBoolean(getAttribute(attributes, "required")));
        
        setPreselected(stringToBoolean(getAttribute(attributes, "preselected")));
        
        setLoose(stringToBoolean(getAttribute(attributes, "loose")));
        
        String id = getAttribute(attributes, "id");
        if (id != null && !id.equals(""))
            setId(id);
        
        setDesc(descNode.getTextContent());
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        //Get lists of all the different types of pack pieces we can have
        try
        {            
            String packLoc = "//pack[" + (packIndex + 1) + "]/";
            
            NodeList os         = (NodeList) xpath.evaluate(packLoc + "os", packNode, XPathConstants.NODESET);
            
            NodeList dirs       = (NodeList) xpath.evaluate(packLoc + "file", packNode, XPathConstants.NODESET);
            NodeList fileset    = (NodeList) xpath.evaluate(packLoc + "fileset", packNode, XPathConstants.NODESET);
            NodeList files      = (NodeList) xpath.evaluate(packLoc + "singlefile", packNode, XPathConstants.NODESET);
            NodeList execs      = (NodeList) xpath.evaluate(packLoc + "executable", packNode, XPathConstants.NODESET);
            NodeList parsables  = (NodeList) xpath.evaluate(packLoc + "parsable", packNode, XPathConstants.NODESET);
            
            oses.initFromXML(os);
            createChildParts(dirs, DirectoryModel.class);
            createChildParts(files, FileModel.class);
            createChildParts(fileset, FileSet.class);
            createChildParts(execs, Executable.class);
            createChildParts(parsables, Parsable.class);
        }
        catch (XPathExpressionException e)
        {
            //I don't think this one will occur, but maybe
            UI.showError("Error initializing a pack from the XML file. \n" + e.getLocalizedMessage(), "XML parsing failure");
            
            //Throw an exception for the automated handler to get
            throw new UnhandleableException(e);
        }
    }
    
    private void createChildParts(NodeList parts, Class peModel)
    {   
        for (int i = 0; i < parts.getLength(); i++)
        {
            try
            {
                PackElement pe = (PackElement) peModel.newInstance();
                
                pe.initFromXML(parts.item(i));
                
                model.insertRow(i, new Object[]{pe});                
                
                if (model.getValueAt(model.getRowCount() - i - 1, 0) == null)
                    model.removeRow(model.getRowCount() - i - 1);
            }
            catch (InstantiationException e)
            {
                UI.showError("Unable to initialize pack data model" + 
                                "\n" + e.getLocalizedMessage(), "Failure creating pack model");
            }
            catch (IllegalAccessException e)
            {
                //Shouldn't ever happen
                throw new UnhandleableException(e);
            }
        }
    }
    
    private String getAttribute(NamedNodeMap attributes, String attributeName)
    {
        Node attr = attributes.getNamedItem(attributeName);
        
        if (attr != null)
            return attr.getNodeValue();        
        else
            return null;
    }
    
    private boolean stringToBoolean(String str)
    {
        if (str == null)
            return false;
        
        if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("true"))
            return true;
        
        return false;
    }
    
    private DefaultTableModel model;
    private ArrayList elements = new ArrayList();
}
