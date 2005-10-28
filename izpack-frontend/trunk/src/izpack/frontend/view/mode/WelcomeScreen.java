/*
 * Created on Oct 7, 2005
 * 
 * $Id: WelcomeScreen.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : WelcomeScreen.java 
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
        //Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        
        Runtime.getRuntime().addShutdownHook(new PersistanceShutdownHook());
        
        new WelcomeScreen().setVisible(true);
    }

    public WelcomeScreen()
    {        
        installerUI = new WizardMode(this);
        
        actionHandler = new ActionHandler(installerUI, this);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
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
