/*
 * Created on Jul 30, 2004
 * 
 * $Id: Welcome.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Welcome.java 
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
package izpack.frontend.view.stages;

import izpack.frontend.controller.GUIController;
import izpack.frontend.model.LangResources;
import izpack.frontend.model.RecentFileManager;
import izpack.frontend.view.GUIConstants;
import izpack.frontend.view.mode.WizardMode;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.w3c.dom.Document;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class Welcome extends IzPackStage implements ActionListener
{
    public void initializeStage()
    {
        FormLayout layout = new FormLayout("left:pref, 15dlu, left:pref", "center:pref, 25dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref");
	    DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
	    	    
	    JLabel header = new JLabel("<html> <font size=\"+2\">" + 
	                    langResources.getText("UI.WelcomePage.ELEMENT.HeaderText") + "</font>");
	    
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
            buttons[i] = new JButton(langResources.getText("UI.WelcomePage.ELEMENT." + buttonNames[i]),
                    new ImageIcon("res/imgs/" + buttonNames[i] + ".png"));
            buttons[i].setName(buttonNames[i]);
            buttons[i].addActionListener(this);
            
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
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JMenuItem)
        {
            openFile(((JMenuItem) e.getSource()).getName());            
            return;
        }
        
        JButton button = (JButton) e.getSource();
        String name = button.getName();
        if (name.equals(GUIConstants.BUTTON_NEW))
        {
        	//IzPackFrame.getInstance().displayStage("GeneralInfoStage");
            WizardMode.wizardMode();
        }
        if (name.equals(GUIConstants.BUTTON_OPEN))
        {
            JFileChooser jfc = new JFileChooser();            
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                RecentFileManager.getInstance().addUsedFile(jfc.getSelectedFile().getAbsolutePath());
            }
        }
        if (name.equals(GUIConstants.BUTTON_RECENT))
        {            
            JPopupMenu menu = new JPopupMenu();
            ArrayList recent = RecentFileManager.getInstance().loadRecentList();
            for (Iterator iter = recent.iterator(); iter.hasNext();)
            {
                String file = (String) iter.next();
                String fileBak = file;
                
                //Shorten the path, so it doesn't cover the entire screen
                if (file.length() > 60)
                {
                    int idx = file.lastIndexOf(System.getProperty("file.separator"));
                    file = file.substring(0, 15) + "..." + file.substring(idx, file.length()); 
                }
                
                JMenuItem mi = menu.add(file);
                mi.setName(fileBak);
                mi.addActionListener(this);
            }
            
            Dimension d = button.getSize();
            menu.show(this, button.getX() + d.width + 4, button.getY());
        }
            
        if (name.equals(GUIConstants.BUTTON_WEBSITE))
        {
            final String[] browserArgs = {"http://www.izforge.com/izpack", "nodebug", "dispose"};
            se.bysoft.sureshot.gui.browser.MiniBrowser.main(browserArgs);
            
        }
        else if (name.equals(GUIConstants.BUTTON_MAILLIST))
        {
            final String[] browserArgs = {"http://lists.berlios.de/pipermail/izpack-users", "nodebug", "dispose"};
            se.bysoft.sureshot.gui.browser.MiniBrowser.main(browserArgs);
        }
    }

    /**
     * @param name
     */
    private void openFile(String name)
    {
        // TODO Auto-generated method stub
        
    }
    
    LangResources langResources = GUIController.getInstance().langResources();

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public boolean validateStage()
    {
        // TODO Auto-generated method stub
        return true;
    }
}
