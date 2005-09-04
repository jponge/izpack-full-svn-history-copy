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
import izpack.frontend.view.components.ItemProgressPanel;
import izpack.frontend.view.renderers.ImageLabel;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.configure.panels.ConfigurePanel;
import izpack.frontend.view.stages.configure.panels.NoEditingNecessary;
import izpack.frontend.view.stages.configure.panels.NoEditorCreated;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;

import com.jgoodies.binding.beans.PropertyConnector;
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
        
        setLayout(layout);
        
        availablePanels = PanelInfoManager.getAvailablePages();
        
        model = new ConfigurationStageModel();
        
        progressPanel = new ItemProgressPanel(model);
        
        new PropertyConnector(progressPanel.getSelectionHolder(), "value",  this, "panelOnDisplay").updateProperty2();
    }

    @Override
    public Document createInstallerData()
    {
        System.out.println("panel config xml");
        return model.writeToXML();
    }

    @Override
    public ValidationResult validateStage()
    {
        return new ValidationResult();
    }

    @Override
    public StageDataModel getDataModel()
    {
        return model;
    }
    
    public void initializeStage()
    {    
        ArrayList<PanelModel> panels = model.getPanels();
        editors = new ArrayList<ConfigurePanel>(panels.size());
        
        for (PanelModel panelModel : panels)
        {
            try
            {
                Class editorClass = Class.forName(panelModel.configData.getEditorClassname());
                Object editorInstance = editorClass.newInstance();
                
                editors.add((ConfigurePanel) editorInstance);
                
                this.add( (JComponent) editorInstance, panelModel.configData.getEditorClassname());
            }
            catch (ClassNotFoundException e)
            {
                this.add( new NoEditorCreated(), panelModel.configData.getEditorClassname());
            }
            catch (InstantiationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        model.setEditors(editors);
    }

    public JPanel getLeftNavBar()
    {        
        return progressPanel;
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
    
    /*
     * 
     * Editor changing stuff - binding interface
     */
    
    public Object getPanelOnDisplay()
    {
        return panelOnDisplay;
    }

    public void setPanelOnDisplay(Object panelOnDisplay)
    {
        System.out.println("Changing panel on display");
        
        if (panelOnDisplay == null)
            return;
        
        if (panelOnDisplay instanceof PanelModel)
            this.panelOnDisplay = (PanelModel) panelOnDisplay;
        
        layout.show(this, this.panelOnDisplay.configData.getEditorClassname());
    }
    
    private ArrayList<PanelInfo> availablePanels;
    private ArrayList<ConfigurePanel> editors;
    private static ConfigurationStageModel model;
    private static ItemProgressPanel progressPanel;
    
    private PanelModel panelOnDisplay;
    
    private CardLayout layout = new CardLayout();
    
    /**
     *  Flag to see if we've recieved two contents changed events
     *  Occurs when moving elements up or down
     */
    private boolean secondContentsChangedEvent = false;
    private int index = -1;
}
