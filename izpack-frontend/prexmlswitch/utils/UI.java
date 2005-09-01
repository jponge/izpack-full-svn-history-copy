/*
 * Created on Mar 29, 2005
 * 
 * $Id: UI.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : UI.java 
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
package utils;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * @author Andy Gombos
 */
public class UI
{
    public static File getFile (JComponent parent, String fileType)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open a " + fileType + "...");
        
        if (jfc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            return jfc.getSelectedFile();
        }
        else
        {
            return null;
        }
    }
    
    public static void showError(String message, String title)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static JButton getNavButton(String label, int direction)
    {
        JButton button;
        if (direction == FORWARD)
            button = new JButton(label, new ImageIcon("res/imgs/forward.png"));
        else
            button = new JButton(label, new ImageIcon("res/imgs/back.png"));
        
	    button.setVerticalTextPosition(SwingConstants.BOTTOM);
	    button.setHorizontalTextPosition(SwingConstants.CENTER);
	    
	    return button;
    }
    
    public static String cutMiddleOfString(String longStr)
    {
        if (longStr.length() > 20 && longStr.lastIndexOf(System.getProperty("file.separator")) != -1)
            return longStr.substring(0, 4) + "..." + longStr.substring(longStr.lastIndexOf(System.getProperty("file.separator")));        
        else
            return longStr;
    }
    
    public static final int FORWARD = 1;
    public static final int BACK = 2;
}
