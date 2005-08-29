/*
 * Created on Aug 25, 2005
 * 
 * $Id: ConfigurationStageModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ConfigurationStageModel.java 
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
package izpack.frontend.model.stages;

import izpack.frontend.model.PanelInfo;

import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;

import com.jgoodies.binding.beans.Model;

public class ConfigurationStageModel extends Model implements StageDataModel, ListDataListener
{

    public ConfigurationStageModel()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Document writeToXML()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void initFromXML(Document doc)
    {
        // TODO Auto-generated method stub

    }

    public class PanelModel
    {
        public Class representingClass;
        
        public String representingClassName;
        
        public PanelInfo configData;
        
        public boolean valid;        
    }
    
    ArrayList<PanelModel> panels;
    int currentlyActivePanel;
    
    public int getCurrentlyActivePanel()
    {
        return currentlyActivePanel;
    }

    public ArrayList<PanelModel> getPanels()
    {
        return panels;
    }

    public void setCurrentlyActivePanel(int currentlyActivePanel)
    {
        this.currentlyActivePanel = currentlyActivePanel;
    }

    public void setPanels(ArrayList<PanelModel> panels)
    {
        this.panels = panels;
    }

    public void intervalAdded(ListDataEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    public void intervalRemoved(ListDataEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    public void contentsChanged(ListDataEvent e)
    {
        // TODO Auto-generated method stub
        
    }    
}
