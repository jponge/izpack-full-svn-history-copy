/*
 * Created on Jun 26, 2004
 * 
 * $Id: PanelSelectPage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelectPage.java 
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
package izpack.frontend.view.pages;

import izpack.frontend.view.*;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class PanelSelectPage extends IzPackPage
{
    public PanelSelectPage()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void initComponents()
    {
        // TODO Auto-generated method stub
        FormLayout layout = new FormLayout(
                "pref:grow, 3dlu, pref, 3dlu, pref:grow",	//Columns
                "100dlu, pref, 3dlu, pref, 20dlu, pref, 3dlu, pref, 100dlu"); //Rows
        layout.setColumnGroups(new int[][] {{1,5}});
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        
        LabelPanel srcPanel = new LabelPanel(false);
        LabelPanel destPanel = new LabelPanel(true);        
        
        CellConstraints cc = new CellConstraints();
        
        JButton buttons[] = createButtons();
        builder.add(buttons[0], cc.xy(3, 2));        
        builder.add(buttons[1], cc.xy(3, 4));
        builder.add(buttons[2], cc.xy(3, 6));
        builder.add(buttons[3], cc.xy(3, 8));
        
        builder.add(srcPanel.getPanel(), cc.xywh(1, 1, 1, 9));
        builder.add(destPanel.getPanel(), cc.xywh(5, 1, 1, 9));
        
        JPanel builtPanel = builder.getPanel();
        builtPanel.setBackground(Color.WHITE);      
        
        add(builtPanel);
        
        setBackground(Color.WHITE);
    }   
    
    public JButton[] createButtons()
    {
        String names[] = {"right", "left", "up", "down"};
        JButton buttons[] = new JButton[names.length];
        
        for (int i = 0; i < names.length; i++)
        {
            JButton button = new JButton(new ImageIcon("res/imgs/"+ names[i] + "Arrow.png"));
            //button.setBorder(null);
            button.setBackground(Color.decode("0xEDF0F9"));
            button.setPreferredSize(new Dimension(50, 22));
            
            buttons[i] = button;           
        }
        
        return buttons;
    }
}
