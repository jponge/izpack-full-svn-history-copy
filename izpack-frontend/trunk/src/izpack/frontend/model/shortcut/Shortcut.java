/*
 * Created on Nov 7, 2005 $Id: Shortcut.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos File : Shortcut.java Description : TODO Add
 * description Author's email : gumbo@users.berlios.de Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package izpack.frontend.model.shortcut;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import utils.XML;

import com.jgoodies.binding.beans.Model;

import exceptions.DocumentCreationException;

public class Shortcut extends Model implements Cloneable
{
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        // TODO Auto-generated method stub
        return super.clone();
    }
    
    public void propogateChanges()
    {
        fireMulticastPropertyChange();
    }
    
    public Element writeXML(Document doc)
    {           
        Element shortcut = XML.createElement("shorcut", doc);
        
        setOptionalAttribute(shortcut, "name", name);
        setOptionalAttribute(shortcut, "target", target);
        
        setOptionalAttribute(shortcut, "commandLine", commandLine);
        setOptionalAttribute(shortcut, "workingDirectory", workingDirectory);
        setOptionalAttribute(shortcut, "description", description);
        
        setOptionalAttribute(shortcut, "iconFile", iconFile);        
        
        if (iconIndex != -1)
            setOptionalAttribute(shortcut, "iconIndex", Integer.toString(iconIndex));
        
        if (initialState != null)
            setOptionalAttribute(shortcut, "initialState", initialState.toString());        
        
        setOptionalAttribute(shortcut, "programGroup", programGroup);
        setOptionalAttribute(shortcut, "desktop", desktop);
        setOptionalAttribute(shortcut, "applications", applications);
        setOptionalAttribute(shortcut, "startup", startup);
        setOptionalAttribute(shortcut, "startMenu", startMenu);        
        
        if (type != null)
            setOptionalAttribute(shortcut, "type", type.toString());        
        
        setOptionalAttribute(shortcut, "url", url);
        
        setOptionalAttribute(shortcut, "terminal", terminal);
        
        setOptionalAttribute(shortcut, "KdeSubstUID", KdeSubstUID);        
        
        return shortcut;
    }

    public void initFromXML(Node elementNode)
    {
        NamedNodeMap attributes = elementNode.getAttributes();
        
        name = attributes.getNamedItem("name").getNodeValue();        
        target = attributes.getNamedItem("target").getNodeValue();
        
        commandLine = getOptionalAttribute(attributes, "commandLine");
        workingDirectory = getOptionalAttribute(attributes, "workingDirectory");
        description = getOptionalAttribute(attributes, "description");
        
        iconFile = getOptionalAttribute(attributes, "iconFile");
        
        try
        {
            //See if icon index was specified
            if (getOptionalAttribute(attributes, "iconIndex").equals(""))
                iconIndex = -1;
            else                
                iconIndex = Integer.parseInt(getOptionalAttribute(attributes, "iconIndex"));
        }
        catch (NumberFormatException nfe)
        {
            //Catch so the program doesn't crash for a bad input file
            //TODO maybe tell user
            iconIndex = 0;
        }
        
        String initialStateStr = getOptionalAttribute(attributes, "initialState");
        for (INITIAL_STATE state : INITIAL_STATE.values())
        {
            if (state.toString().equalsIgnoreCase(initialStateStr))
                initialState = state;
        }
        
        programGroup = getAttributeAsBoolean(attributes, "programGroup");
        desktop = getAttributeAsBoolean(attributes, "desktop");
        applications = getAttributeAsBoolean(attributes, "applications");
        startup = getAttributeAsBoolean(attributes, "startup");
        startMenu = getAttributeAsBoolean(attributes, "startMenu");
        
        //Determine the type (application or link) of the shortcut, and whether or not this is UNIX
        String typeStr = getOptionalAttribute(attributes, "type");
        if (!typeStr.equals(""))
        {
            modelledOS = OS.Unix;
            
            for (TYPE typeElem : TYPE.values())
            {
                if (typeElem.toString().equalsIgnoreCase(typeStr))                    
                    type = typeElem;
            }   
        }
        else
            modelledOS = OS.Windows;
        
        url = getOptionalAttribute(attributes, "url");
        
        terminal = getAttributeAsBoolean(attributes, "terminal");
        
        KdeSubstUID = getOptionalAttribute(attributes, "KdeSubstUID");
    }
    
    private void setOptionalAttribute(Element elem, String attribute, String value)
    {
        if (value != null && ! value.equals(""))
        {            
            //Make the attribute name have a first lowercase letter
            //Not the most efficient, but it's only going to be called a little bit
            attribute = attribute.substring(0, 1).toLowerCase() + attribute.substring(1);
            
            elem.setAttribute(attribute, value);
        }
    }
    
    private void setOptionalAttribute(Element elem, String attribute, boolean value)
    {
        if (value)
            elem.setAttribute(attribute, "yes");
        else
            elem.setAttribute(attribute, "no");
    }
    
    private String getOptionalAttribute(NamedNodeMap attributes, String attribute)
    {
        if (attributes.getNamedItem(attribute) != null)
            return attributes.getNamedItem(attribute).getNodeValue();
        else
            return "";
    }   
    
    //TODO refactor these into some framework - preferably automatic - for the whole program
    private boolean getAttributeAsBoolean(NamedNodeMap attributes, String attribute)
    {
        String str = getOptionalAttribute(attributes, attribute);
        
        if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("on"))
            return true;
        
        return false;
    }
    
    // General
    private OS modelledOS;
    
    private String name;
    private String target;
    private String commandLine;
    private String workingDirectory;
    private String description;

    // Main functionality on Windows
    private String  iconFile;
    private int     iconIndex = -1;
    private INITIAL_STATE initialState;
    private boolean programGroup;
    private boolean desktop;
    private boolean applications;
    private boolean startMenu;
    private boolean startup;

    // UNIX specific
    private TYPE type;
    private String url;
    private String encoding = "UTF-8"; // Not set by the user
    private boolean terminal;
    private String KdeSubstUID; // Not implemented yet

    public static enum OS {
        Windows, Unix, MacOS
    };

    public static enum TYPE {
        Application, Link
    };

    public static enum INITIAL_STATE {
        noShow, normal, maximized, minimized
    };

    public boolean isApplications()
    {
        return applications;
    }

    public String getCommandLine()
    {
        return commandLine;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isDesktop()
    {
        return desktop;
    }

    public String getIconFile()
    {
        return iconFile;
    }

    public INITIAL_STATE getInitialState()
    {
        return initialState;
    }

    public String getName()
    {
        return name;
    }

    public boolean isProgramGroup()
    {
        return programGroup;
    }

    public boolean isStartMenu()
    {
        return startMenu;
    }

    public boolean isStartup()
    {
        return startup;
    }

    public String getTarget()
    {
        return target;
    }

    public boolean isTerminal()
    {
        return terminal;
    }

    public TYPE getType()
    {
        return type;
    }

    public String getWorkingDirectory()
    {
        return workingDirectory;
    }

    public void setApplications(boolean applications)
    {
        this.applications = applications;
        
        firePropertyChange("applications", null, applications);
    }

    public void setCommandLine(String commandLine)
    {
        this.commandLine = commandLine;
        
        firePropertyChange("commandLine", null, commandLine);
    }

    public void setDescription(String description)
    {
        this.description = description;
        
        firePropertyChange("description", null, description);
    }

    public void setDesktop(boolean desktop)
    {
        this.desktop = desktop;
        
        firePropertyChange("desktop", null, desktop);
    }

    public void setIconFile(String iconFile)
    {
        this.iconFile = iconFile;
        
        firePropertyChange("iconFile", null, iconFile);
    }

    public void setInitialState(INITIAL_STATE newState)
    { 
        initialState = newState;
        
        firePropertyChange("initialState", null, newState);
    }

    public void setName(String name)
    {
        this.name = name;
        
        firePropertyChange("name", null, name);
    }

    public void setProgramGroup(boolean programGroup)
    {
        this.programGroup = programGroup;
        
        firePropertyChange("programGroup", null, programGroup);
    }

    public void setStartMenu(boolean startMenu)
    {
        this.startMenu = startMenu;
     
        firePropertyChange("startMenu", null, startMenu);
    }

    public void setStartup(boolean startup)
    {
        this.startup = startup;
        
        firePropertyChange("startup", null, startup);
    }

    public void setTarget(String target)
    {
        this.target = target;
        
        firePropertyChange("target", null, target);
    }

    public void setTerminal(boolean terminal)
    {
        this.terminal = terminal;
        
        firePropertyChange("terminal", null, terminal);
    }

    public void setType(TYPE type)
    {
        this.type = type;
        
        firePropertyChange("type", null, type);
    }

    public void setWorkingDirectory(String workingDirectory)
    {
        this.workingDirectory = workingDirectory;
        
        firePropertyChange("workingDirectory", null, workingDirectory);
    };    
   
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
        
        firePropertyChange("url", null, url);
    }

    public OS getModelledOS()
    {
        return modelledOS;
    }

    public void setModelledOS(OS modelledOS)
    {
        this.modelledOS = modelledOS;
        
        firePropertyChange("modelledOS", null, modelledOS);
    }

    public int getIconIndex()
    {
        return iconIndex;
    }

    public void setIconIndex(int iconIndex)
    {
        this.iconIndex = iconIndex;
        
        firePropertyChange("iconIndex", null, iconIndex);
    }
}
