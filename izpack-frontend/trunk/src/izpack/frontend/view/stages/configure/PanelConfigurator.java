/*
 * Created on Aug 25, 2005
 * 
 * $Id: PanelConfigurator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelConfigurator.java 
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
package izpack.frontend.view.stages.configure;

import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.PanelInfoManager;
import izpack.frontend.model.stages.ConfigurationStageModel;
import izpack.frontend.model.stages.PanelSelectionModel;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.model.stages.ConfigurationStageModel.PanelModel;
import izpack.frontend.view.renderers.ImageLabel;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;

import com.jgoodies.validation.ValidationResult;

public class PanelConfigurator extends IzPackStage implements ListDataListener
{

    public PanelConfigurator()
    {
        super();
        
        PanelSelection panelSelectionInstance = (PanelSelection) getStage(PanelSelection.class);
        
        if (panelSelectionInstance != null)
        {
            PanelSelectionModel panelSelectionModel = (PanelSelectionModel) panelSelectionInstance.getDataModel();
            
            if (panelSelectionModel != null)
            {
                panelSelectionModel.addListDataListener(this);
            }
            else
            {
                System.err.println("Panel selection stage did not initialize properly. Exiting");
                System.exit(1);
            }
        }
        else
        {
            System.err.println("Application did not initialize properly. Exiting");
            System.exit(1);
        }
        
        availablePanels = PanelInfoManager.getAvailablePages();
        
        model = new ConfigurationStageModel();
    }

    @Override
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValidationResult validateStage()
    {
        return new ValidationResult();
    }

    @Override
    public StageDataModel getDataModel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void initializeStage()
    {
        // TODO Auto-generated method stub

    }

    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void intervalAdded(ListDataEvent e)
    {        
        PanelSelectionModel psm = (PanelSelectionModel) e.getSource();
        ImageLabel renderer = (ImageLabel) psm.get(e.getIndex0());
        
        PanelInfo addedObject = availablePanels.get(renderer.getPanelArrayIndex());
        
        PanelModel newAddedObject = model.new PanelModel();
        newAddedObject.configData = addedObject;
        newAddedObject.valid = false;
        
        model.getPanels().add(e.getIndex0(), newAddedObject);
    }

    public void intervalRemoved(ListDataEvent e)
    {   
        model.getPanels().remove(e.getIndex0());        
    }

    public void contentsChanged(ListDataEvent e)
    {
        if (!secondContentsChangedEvent)
        {
            index = e.getIndex0();
            secondContentsChangedEvent = true;
        }        
        else
        {   
            int delta = index - e.getIndex0();
            
            //Swap elements. This is different than the normal swap elements
            //because i calculated the delta and index differently (off a different element)
            PanelModel original = model.getPanels().get(index - delta);
            PanelModel replaced = model.getPanels().get(index);
            model.getPanels().set(index, original);
            model.getPanels().set(index - delta, replaced);
            
            secondContentsChangedEvent = false;
        }            
    }
    
    private ArrayList<PanelInfo> availablePanels;
    private static ConfigurationStageModel model;
    
    /**
     *  Flag to see if we've recieved two contents changed events
     *  Occurs when moving elements up or down
     */
    private boolean secondContentsChangedEvent = false;
    private int index = -1;
}
