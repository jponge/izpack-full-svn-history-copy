/*
 * Created on Jul 3, 2004
 * 
 * $Id: PanelSelection.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PanelSelection.java 
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

    public void initializeStageFromXML(Document doc)
    {
        model.initFromXML(doc);        
    }

    @Override
    public void resetStage()
    {
        model.clear();
    }
}
