/*
 * Created on Nov 7, 2005
 * 
 * $Id: ShortcutSet.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ShortcutSet.java 
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

package izpack.frontend.model.shortcut;

import izpack.frontend.model.shortcut.Shortcut.INITIAL_STATE;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.bcel.internal.generic.LCONST;

import utils.XML;

/**
 * Provides a container to store Shortcut instances, and perform operations on them.
 * 
 * @author Andy Gombos
 */
public class ShortcutSet extends DefaultTableModel
{
    public ShortcutSet()
    {   
        pcs = new PropertyChangeSupport(this);        
        
        //TODO make format same as renderer in pseudo columns
        this.addColumn("OS       Name                                Target");
    }
    
    //XML input/output stuff
    public Document writeXML()
    {        
        Document doc = XML.createDocument();
        
        Element shortcutsRoot = XML.createElement("shortcuts", doc);
        doc.appendChild(shortcutsRoot);
        
        //Add a node if we should skip when we can't create a shortcut
        if (skipIfNotSupported)
        {
           shortcutsRoot.appendChild(XML.createElement("skipIfNotSupported", doc));
        }
        
        Element programGroup = XML.createElement("programGroup", doc);
        
        if (defaultName != null)
            programGroup.setAttribute("defaultName", defaultName);
        
        if (location != null)
            programGroup.setAttribute("location", location.toString());
        
        shortcutsRoot.appendChild(programGroup);
        
        //Add each shortcut in the set to the file
        ArrayList<Shortcut> shortcuts = getShortcuts();
        
        for (Shortcut shortcut : shortcuts)
        {
            shortcutsRoot.appendChild(shortcut.writeXML(doc));
        }
        
        return doc;
    }
    
    public void initXML(Document doc)
    {
        if (doc.getElementsByTagName("skipIfNotSupported").getLength() != 0)
            skipIfNotSupported = true;
        
        NodeList programGroup = doc.getElementsByTagName("programGroup");        
        if (programGroup.getLength() != 0)
        {
            Node programGroupElem = programGroup.item(0);
            
            NamedNodeMap attributes = programGroupElem.getAttributes();
            
            defaultName = attributes.getNamedItem("defaultName").getNodeValue();
            
            String locationStr = attributes.getNamedItem("location").getNodeValue();
            
            for (LOCATION loc : LOCATION.values())
            {
                if (loc.toString().equalsIgnoreCase(locationStr))
                    location = loc;
            }
        }
        
        NodeList shortcuts = doc.getElementsByTagName("shortcut");
        for (int i = 0; i < shortcuts.getLength(); i++)
        {            
            Shortcut s = new Shortcut();
            s.initFromXML(shortcuts.item(i));
            
            addShortcut(s);            
        }
    }
    

    public ArrayList<Shortcut> getShortcuts()
    {
        ArrayList<Shortcut> shortcuts = new ArrayList<Shortcut>(5);
        
        for (int i = 0; i < getRowCount(); i++)
        {
            shortcuts.add((Shortcut) getValueAt(i, 0));
        }
        
        return shortcuts;
    }    
    
    public void addShortcut(Shortcut shortcut)
    {        
        this.addRow(new Object[]{shortcut});
    }
    
    public void duplicateShortcut(Shortcut shortcut)
    {
        try
        {
            addShortcut((Shortcut) shortcut.clone());
        }
        catch (CloneNotSupportedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static enum LOCATION {Applications, StartMenu};

    private String defaultName;
    private LOCATION location;
    private boolean skipIfNotSupported;
    
    public String getDefaultName()
    {
        return defaultName;
    }

    public LOCATION getLocation()
    {
        return location;
    }

    public boolean isSkipIfNotSupported()
    {
        return skipIfNotSupported;
    }

    public void setDefaultName(String defaultName)
    {
        this.defaultName = defaultName;
        pcs.firePropertyChange("defaultName", null, defaultName);
    }

    public void setLocation(LOCATION location)
    {
        this.location = location;
        pcs.firePropertyChange("location", null, location.toString());
    }

    public void setSkipIfNotSupported(boolean skipIfNotSupported)
    {
        this.skipIfNotSupported = skipIfNotSupported;
        pcs.firePropertyChange("skipIfNotSupported", null, skipIfNotSupported);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        pcs.addPropertyChangeListener(pcl);
    }
    
    public void addPropertyChangeListener(String property, PropertyChangeListener pcl)
    {
        pcs.addPropertyChangeListener(property, pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        pcs.removePropertyChangeListener(pcl);        
    }
    
    public void removePropertyChangeListener(String property, PropertyChangeListener pcl)
    {        
        pcs.removePropertyChangeListener(property, pcl);        
    }
    
    private PropertyChangeSupport pcs;
}
