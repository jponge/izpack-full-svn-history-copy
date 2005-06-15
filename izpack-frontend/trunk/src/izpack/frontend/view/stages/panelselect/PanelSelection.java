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

import izpack.frontend.controller.StageChangeEvent;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.StageOrder.StageContainer;
import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.packs.Pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;
import utils.XML;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;

/**
 * @author Andy Gombos
 */
public class PanelSelection extends IzPackStage
{
    public PanelSelection()
    {
        super();
        
        panelSelect = new PanelSelectList();
	    
		add(panelSelect);
    }
	
	public void initializeStage() 
	{		    
	}

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {   
        Element root = XML.createRootElement("installation");
        Document rootDoc = root.getOwnerDocument();
        
        root.setAttribute("version", "1.0");                
        root.appendChild(rootDoc.importNode(panelSelect.createXML(), true));
        
        return rootDoc;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = new ValidationResult();
        
        if (panelSelect.dest.getNumElements() < 0)
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have at least one panel selected to use",
                            panelSelect.dest,
                            "Panels",
                            "selected"
                            ));
        }
        
        return vr;
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
        // TODO Auto-generated method stub
        return null;
    }
}
