/*
 * Created on Jun 28, 2004
 * 
 * $Id: ImageLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ImageLabel.java 
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
package izpack.frontend.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * @author Andy Gombos
 * 
 * Provide a component that displays an icon and text, but with consistent alignment
 */
public class ImageLabel extends JButton implements ActionListener
{    
    public ImageLabel(String name, String shortDesc, String imgFile)
    {
        configure("<html>" +
                		"<b>&nbsp;" + name + "</b>" +
                		"<p> <font color=#969696>&nbsp;" + shortDesc + "</font>", imgFile);
    }
    
    public void configure(String text, String imgFile)
    {
        setLayout(new FlowLayout());
        ((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);        
        
        icon = new JLabel(new ImageIcon(imgFile));        
        lText = new JLabel(text);        
                      
        add(icon);
        add(lText);
        
        addActionListener(this);
    }
    
    private JLabel icon, lText;

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    //TODO Update when new LAF comes out
    public void actionPerformed(ActionEvent e)
    {
        if (isSelected())
        {
            setBackground(Color.WHITE);
            setSelected(false);
        }
        else
        {
            setBackground(Color.decode("0xCCCCCC"));
            setSelected(true);
        }
        
    }
}
