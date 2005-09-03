/*
 * Created on Jul 3, 2004
 * 
 * $Id: PanelSelection.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelection.java 
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
package izpack.frontend.view.stages.panelselect;

import izpack.frontend.controller.validators.PanelSelectionValidator;
import izpack.frontend.model.stages.PanelSelectionModel;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;

import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class PanelSelection extends IzPackStage implements ListDataListener
{
    public PanelSelection()
    {
        super();
        
        panelSelect = new PanelSelectList();
        
        model = panelSelect.getDestModel();
        
        model.addListDataListener(this);
        
        validator = new PanelSelectionValidator(model);
	    
		add(panelSelect);
    }
	
	public void initializeStage() 
	{		    
	}

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Element[] createInstallerData(Document doc)
    {           
        return model.writeToXML(doc);
    }    
        
    PanelSelectList panelSelect;

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getLeftNavBar()
     */
    public JPanel getLeftNavBar()
    {        
        return new JPanel();
    }    

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        return super.getBottomInfoBar();
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#getDataModel()
     */
    public StageDataModel getDataModel()
    {
        return model;
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    // TODO warnings for common forgotten panels
    public ValidationResult validateStage()
    {
        ValidationResult vr = validator.validate();
        validationModel.setResult(vr);
        
        return vr; 
    }

    
    private static PanelSelectionModel model;
    private static PanelSelectionValidator validator;

    /*
     * Validation stuff  
     * Should probably be refactored into a nicer, more unified interface
     */
    
    public void intervalAdded(ListDataEvent e)
    {
        validateStage();        
    }

    public void intervalRemoved(ListDataEvent e)
    {
        validateStage();
    }

    public void contentsChanged(ListDataEvent e)
    {
        validateStage();        
    }
}
