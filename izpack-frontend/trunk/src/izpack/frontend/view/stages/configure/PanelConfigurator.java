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

import izpack.frontend.model.stages.ConfigurationStageModel;
import izpack.frontend.model.stages.PanelModel;
import izpack.frontend.model.stages.PanelSelectionModel;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.components.ItemProgressPanel;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.configure.panels.ConfigurePanel;
import izpack.frontend.view.stages.configure.panels.NoEditorCreated;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;

import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.validation.ValidationResult;

import exceptions.UnhandleableException;

public class PanelConfigurator extends IzPackStage implements ListDataListener
{

    public PanelConfigurator()
    {
        super();
        
        PanelSelection panelSelectionInstance = (PanelSelection) getStage(PanelSelection.class);
        
        PanelSelectionModel panelSelectionModel = null;
        
        if (panelSelectionInstance != null)
        {
            panelSelectionModel = (PanelSelectionModel) panelSelectionInstance.getDataModel();
            
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
            System.err.println("Application did not initialize properly. Exiting.");
            System.exit(1);
        }
        
        setLayout(layout);
        
        //availablePanels = PanelInfoManager.getAvailablePanels();
        
        model = new ConfigurationStageModel(panelSelectionModel);
        
        progressPanel = new ItemProgressPanel(model);
        
        new PropertyConnector(progressPanel.getSelectionHolder(), "value",  this, "panelOnDisplay").updateProperty2();
        new PropertyConnector(progressPanel.getListModel(), "value", this.model, "panels").updateProperty1();
    }

    @Override
    public Element[] createInstallerData(Document doc)
    {        
        return model.writeToXML(doc);
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
        ListModel panels = model.getPanels();
        ArrayList<ConfigurePanel> editors = model.getEditors();
        
        
        for (int i = 0; i < panels.getSize(); i++)        
        {            
            PanelModel panelModel = (PanelModel) panels.getElementAt(i);
            try
            {
                Class editorClass = Class.forName(panelModel.configData.getEditorClassname());                
                Object editorInstance = editorClass.newInstance();
                
                if (xmlDoc != null)
                    ((ConfigurePanel) editorInstance).initFromXML(xmlDoc);
                
                editors.add((ConfigurePanel) editorInstance);
                
                this.add( (JComponent) editorInstance, panelModel.configData.getEditorClassname());
            }
            catch (ClassNotFoundException e)
            {
                this.add( new NoEditorCreated(), panelModel.configData.getEditorClassname());
            }
            catch (InstantiationException e)
            {
                UI.showError("Unable to initialize editor " + panelModel.configData.getEditorClassname() + 
                                "\n" + e.getLocalizedMessage(), "Failure creating editor");
            }
            catch (IllegalAccessException e)
            {
                //Shouldn't ever happen
                throw new UnhandleableException(e);            
            }
        }
        
        model.setEditors(editors);
        progressPanel.calculatePreferredSize();
    }

    public JPanel getLeftNavBar()
    {   
        return progressPanel;
    }

    public void intervalAdded(ListDataEvent e)
    {
        progressPanel.calculatePreferredSize();
    }

    public void intervalRemoved(ListDataEvent e)
    {   
        progressPanel.calculatePreferredSize();
    }

    public void contentsChanged(ListDataEvent e)
    {
        progressPanel.calculatePreferredSize();
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
        System.out.println("Changing panel on display: " + panelOnDisplay);
        
        if (panelOnDisplay == null)
            return;
        
        if (panelOnDisplay instanceof PanelModel)
            this.panelOnDisplay = (PanelModel) panelOnDisplay;
        
        layout.show(this, this.panelOnDisplay.configData.getEditorClassname());
    }
    
    //private ArrayList<PanelInfo> availablePanels;    
    private static ConfigurationStageModel model;
    private static ItemProgressPanel progressPanel;
    
    private PanelModel panelOnDisplay;
    
    private CardLayout layout = new CardLayout();
    
    /**
     *  Flag to see if we've recieved two contents changed events
     *  Occurs when moving elements up or down
     */
    private boolean initializedFromXML = false;
    private int index = -1;
    
    private Document xmlDoc;
    
    public void initializeStageFromXML(Document doc)
    {
        xmlDoc = doc;        
    }
}
