/*
 * Created on Aug 26, 2005
 * 
 * $Id: ValidationDocumentationPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ValidationDocumentationPanel.java 
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

import izpack.frontend.model.stages.PanelModel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ValidationDocumentationPanel extends JPanel
{   
    public ValidationDocumentationPanel()
    {
        label = new JLabel("No class chosen");
        label.setForeground(Color.BLUE);
        add(label);
    }
    
    public Object getPanelOnDisplay()
    {
        return panelOnDisplay;
    }

    public void setPanelOnDisplay(Object panelOnDisplay)
    {
        if (panelOnDisplay == null)
            return;
        
        if (panelOnDisplay instanceof PanelModel)
            this.panelOnDisplay = (PanelModel) panelOnDisplay;        
        
        label.setText(this.panelOnDisplay.configData.getClassname());
    } 
    
    private PanelModel panelOnDisplay;
    private JLabel label;
}   
