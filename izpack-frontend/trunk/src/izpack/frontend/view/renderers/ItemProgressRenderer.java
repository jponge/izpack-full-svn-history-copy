/*
 * Created on Aug 26, 2005
 * 
 * $Id: ItemProgressRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ItemProgressRenderer.java 
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

package izpack.frontend.view.renderers;

import izpack.frontend.model.stages.PanelModel;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

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
        
        //Configure renderer
        JPanel renderer = new JPanel();
        
        renderer.setBorder(new CompoundBorder(new EmptyBorder(0, 2, 2, 2), new EtchedBorder(EtchedBorder.LOWERED)));
                
        renderer.setLayout(new FlowLayout());
        ((FlowLayout) renderer.getLayout()).setAlignment(FlowLayout.LEFT);
        
        
        //End renderer configuration
        
        String labelText;
        
        if (panelModel == null  || panelModel.configData == null)
            labelText = "<html>" +
                "<b>&nbsp; Unknown Element</b>" +
                "<p> <font color=#969696>&nbsp; Unknown element in XML</font>";
        else
            labelText = "<html>" +
                        "<b>&nbsp;" + panelModel.configData.getName() + "</b>" +
                        "<p> <font color=#969696>&nbsp;" + panelModel.configData.getShortDesc() + "</font>";        
                
        JLabel lText = new JLabel(labelText);
        
        if (isSelected)
        {
            renderer.add(selectedIcon);
            renderer.setBackground(renderer.getBackground().brighter());
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
