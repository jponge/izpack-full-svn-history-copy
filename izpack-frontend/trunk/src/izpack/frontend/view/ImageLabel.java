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

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Andy Gombos
 * 
 * Provide a component that displays an icon and text, but with consistent alignment
 */
public class ImageLabel extends JPanel
{
    public ImageLabel(String text, String imgFile)
    {
        setLayout(new FlowLayout());
        ((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);        
        
        icon = new JLabel(new ImageIcon(imgFile));        
        lText = new JLabel(text);        
                      
        add(icon);
        add(lText);
    }
    
    private JLabel icon, lText;
}
