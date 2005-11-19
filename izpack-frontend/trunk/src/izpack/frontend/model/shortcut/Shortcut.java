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

import com.jgoodies.binding.beans.Model;

public class Shortcut extends Model
{
    public Shortcut()
    {
        modelledOS = OS.Unix;
        type = TYPE.Application;
    }
    
    public void propogateChanges()
    {
        fireMulticastPropertyChange();
    }
    
    //TODO load/save xml representation

    // General
    private OS modelledOS;
    
    private String name;
    private String target;
    private String commandLine;
    private String workingDirectory;
    private String description;

    // Main functionality on Windows
    private String iconFile;
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
}
