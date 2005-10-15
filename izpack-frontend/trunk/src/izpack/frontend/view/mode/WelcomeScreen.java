/*
 * Created on Oct 7, 2005
 * 
 * $Id: WelcomeScreen.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : WelcomeScreen.java 
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
package izpack.frontend.view.mode;

import izpack.frontend.actions.ActionHandler;
import izpack.frontend.controller.GUIController;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.GUIConstants;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reporting.CrashHandler;
import utils.PersistanceShutdownHook;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class WelcomeScreen extends JFrame
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {        
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        
        Runtime.getRuntime().addShutdownHook(new PersistanceShutdownHook());
        
        new WelcomeScreen().setVisible(true);
    }

    public WelcomeScreen()
    {        
        installerUI = new WizardMode();
        
        actionHandler = new ActionHandler(installerUI, this);
        
        
        FormLayout layout = new FormLayout("left:pref, 15dlu, left:pref",
                        "center:pref, 25dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());

        JLabel header = new JLabel(
                        "<html> <font size=\"+2\">"
                                        + langResources
                                                        .getText("UI.WelcomePage.ELEMENT.HeaderText")
                                        + "</font>");

        header.setAlignmentY(CENTER_ALIGNMENT);
        builder.add(header, new CellConstraints().xyw(1, 1, 3));
        builder.nextRow();
        builder.nextRow();

        // Columns first, off the prototype image
        String buttonNames[] = new String[]{GUIConstants.BUTTON_NEW,
                        GUIConstants.BUTTON_COMPILE,
                        GUIConstants.BUTTON_WEBSITE,
                        GUIConstants.BUTTON_RECENT, GUIConstants.BUTTON_OPEN,
                        GUIConstants.BUTTON_HELP, GUIConstants.BUTTON_MAILLIST};
        JButton buttons[] = new JButton[7];

        // Create the buttons, and add them to the layout
        for (int i = 0; i < buttonNames.length; i++)
        {
            buttons[i] = new JButton(
                            langResources.getText("UI.WelcomePage.ELEMENT."
                                            + buttonNames[i]), new ImageIcon(
                                            "res/imgs/" + buttonNames[i]
                                                            + ".png"));
            buttons[i].setName(buttonNames[i]);
            buttons[i].addActionListener(actionHandler);

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

        setContentPane(builder.getPanel());
        pack();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private LangResources langResources = GUIController.getInstance()
                    .langResources();    
    
    private WizardMode installerUI = null;
    private ActionHandler actionHandler = null;
}
