/*
 * Created on Mar 29, 2005
 * 
 * $Id: UI.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : UI.java 
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

package utils;

import java.awt.Container;
import java.awt.Frame;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * @author Andy Gombos
 */
public class UI
{
    public static File getFile (Container parent, String fileType)
    {        
        return getFile(parent, "Open a " + fileType + "...", null, false);
    }
    
    public static File getFile (Container parent, String title, boolean directoriesOnly)
    {
        return getFile(parent, title, null, directoriesOnly);
    }
    
    public static File getFile (Container parent, String title, File start, boolean directoriesOnly)
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(title);
        
        if (start != null)
            jfc.setSelectedFile(start);
        
        if (directoriesOnly)
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        else
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
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
        System.out.println(UI.getApplicationFrame());
        JOptionPane.showMessageDialog(UI.getApplicationFrame(), message, title, JOptionPane.ERROR_MESSAGE);
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
    
    public static Frame getApplicationFrame()
    {
        Frame[] frames = JFrame.getFrames();
        
        for (int i = 0; i < frames.length; i++)
        {
            if (frames[i].isShowing())
                return frames[i];
        }
        
        return null;
    }
    
    public static final int FORWARD = 1;
    public static final int BACK = 2;
}
