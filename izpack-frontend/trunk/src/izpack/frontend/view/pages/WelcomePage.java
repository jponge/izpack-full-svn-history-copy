/*
 * $Id: WelcomePage.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : WelcomePage.java 
 * Description : Quick implementation of the WelcomePage.
 * Author's email : dani@gueggs.net
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

import izpack.frontend.controller.DisplayPageAction;
import izpack.frontend.view.*;
import izpack.frontend.view.GUIConstants;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author dguggi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class WelcomePage extends IzPackPage {
	
	/**
	 * Quick implementation of the help page, to test framework.
	 */
	public void initComponents() {
	    
	    FormLayout layout = new FormLayout("left:pref, 15dlu, left:pref", "center:pref, 25dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref");
	    DefaultFormBuilder builder = new DefaultFormBuilder(new FormDebugPanel(), layout);
	    	    
	    JLabel header = new JLabel("<html> <font size=\"+2\">What would you like to do?</font>");
	    header.setAlignmentY(CENTER_ALIGNMENT);
	    builder.add(header, new CellConstraints().xyw(1, 1, 3));
	    builder.nextRow();
	    builder.nextRow();
	    
	    //Columns first, off the prototype image
		String buttonNames[] = new String[]{GUIConstants.BUTTON_NEW, GUIConstants.BUTTON_COMPILE, GUIConstants.BUTTON_WEBSITE,
		        GUIConstants.BUTTON_RECENT, GUIConstants.BUTTON_OPEN, GUIConstants.BUTTON_HELP, GUIConstants.BUTTON_MAILLIST};
		JButton buttons[] = new JButton[7];
		
		//Create the buttons, and add them to the layout
		for (int i = 0; i < buttonNames.length; i++)
        {
            buttons[i] = new JButton(langResources().getText("UI.WelcomePage.ELEMENT." + buttonNames[i]),
                    new ImageIcon("res/imgs/" + buttonNames[i] + ".png"));
            
            buttons[i].setBorder(null);            
            
            builder.add(buttons[i]);
            builder.nextRow();
            builder.nextRow();
            
            if (i == 3)
            {
                builder.setRow(3);
                builder.setColumn(3);
            }
        }
		
		add(builder.getPanel());		
	}
}
