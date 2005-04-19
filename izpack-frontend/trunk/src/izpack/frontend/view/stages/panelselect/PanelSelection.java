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
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.geninfo.GeneralInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;
import utils.XML;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class PanelSelection extends IzPackStage
{
	
	public void initializeStage() 
	{	
	    panelSelect = new PanelSelect();
	    
		add(panelSelect);
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
        // TODO Auto-generated method stub
        return null;
    }
    
    PanelSelect panelSelect;

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getLeftNavBar()
     */
    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getTopNavBar()
     */
    public JPanel getTopNavBar()
    {
        JPanel base = new JPanel();
        JButton previous = UI.getNavButton("General Information", UI.BACK);
        JButton next = UI.getNavButton("Create Packs", UI.FORWARD);
        
        previous.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                fireStageChangeEvent(new StageChangeEvent(GeneralInformation.class));
            }
        });
        
        next.setPreferredSize(previous.getPreferredSize());
        
        base.add(previous);
        base.add(next);
        
        return base;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
