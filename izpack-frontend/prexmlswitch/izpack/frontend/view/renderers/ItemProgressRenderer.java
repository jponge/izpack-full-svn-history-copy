/*
 * Created on Aug 26, 2005
 * 
 * $Id: ItemProgressRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ItemProgressRenderer.java 
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
package izpack.frontend.view.renderers;

import izpack.frontend.model.stages.ConfigurationStageModel.PanelModel;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ItemProgressRenderer implements ListCellRenderer
{

    public ItemProgressRenderer()
    {
        super();
    }

    public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus)
    {
        PanelModel panelModel = (PanelModel) value;
        
        JPanel renderer = new JPanel();
        
        renderer.setLayout(new FlowLayout());
        ((FlowLayout) renderer.getLayout()).setAlignment(FlowLayout.LEFT);
        
        String labelText = "<html>" +
                        "<b>&nbsp;" + panelModel.configData.getName() + "</b>" +
                        "<p> <font color=#969696>&nbsp;" + panelModel.configData.getShortDesc() + "</font>";        
                
        JLabel lText = new JLabel(labelText);
        
        if (isSelected)
        {
            renderer.add(selectedIcon);
            //renderer.setBackground(renderer.getBackground().brighter());
        }
                
        if (panelModel.valid)
            renderer.add(validIcon);
        else
            renderer.add(invalidIcon);
        
        renderer.add(lText);
        
        return renderer;
    }   
    

    private static JLabel validIcon = new JLabel( new ImageIcon("res/imgs/valid.png") );
    private static JLabel invalidIcon = new JLabel( new ImageIcon("res/imgs/invalid.png") );
    
    private static JLabel selectedIcon = new JLabel( new ImageIcon("res/imgs/greyArrow.png") );
}
