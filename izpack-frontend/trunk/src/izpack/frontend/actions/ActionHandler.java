/*
 * Created on Oct 13, 2005
 * 
 * $Id: ActionHandler.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ActionHandler.java 
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

package izpack.frontend.actions;

import izpack.frontend.controller.RecentFileManager;
import izpack.frontend.view.GUIConstants;
import izpack.frontend.view.mode.WelcomeScreen;
import izpack.frontend.view.mode.WizardMode;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import utils.UI;
import exceptions.DocumentCreationException;

public class ActionHandler implements ActionListener
{
    public ActionHandler(WizardMode installerUI, WelcomeScreen welcomeUI)
    {
        this.installerUI = installerUI;
        this.welcomeUI = welcomeUI;
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
            //Display the installer
            if (installerUI != null)
            {
                welcomeUI.setVisible(false);
                installerUI.setVisible(true);
            }
        }
        if (name.equals(GUIConstants.BUTTON_OPEN))
        {            
            //TODO for all file choosers, set a *.xml filter
            File f = UI.getFile(welcomeUI, "Open an installer XML file...", false);
            
            if (f != null)
                openFile(f.getAbsolutePath());                
            
        }
        if (name.equals(GUIConstants.BUTTON_COMPILE))
        {
            //TODO for all file choosers, set a *.xml filter
            final File f = UI.getFile(welcomeUI, "Open an installer XML file...", false);
            
            if (f != null)
            {
                final CompileDisplay displayer = new CompileDisplay(true, f);
                
                displayer.addCompileListener(new CompileListener()
                {
                    public void compileRequested(CompileEvent ce)
                    {
                        System.out.println("Compile requested");
                        
                        displayer.next();
                        
                        CompileManager.compile(f.getAbsolutePath(), new String[] {
                            ce.getBaseDir(),
                            ce.getInstallType(),                            
                            ce.getOutputFile()},
                            displayer.getPackagerListener()
                            );
                    }                    
                });
                
                JDialog dialog = new JDialog(UI.getApplicationFrame(), "Set compile settings");
                
                dialog.add(displayer);
                dialog.pack();
                dialog.setVisible(true);
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

                // Shorten the path, so it doesn't cover the entire screen
                if (file.length() > 60)
                {
                    int idx = file.lastIndexOf(System
                                    .getProperty("file.separator"));
                    file = file.substring(0, 15) + "..."
                                    + file.substring(idx, file.length());
                }

                JMenuItem mi = menu.add(file);
                mi.setName(fileBak);
                mi.addActionListener(this);
            }

            Dimension d = button.getSize();
            menu.show(welcomeUI, button.getX() + d.width + 4, button.getY());
        }

        if (name.equals(GUIConstants.BUTTON_WEBSITE))
        {
            final String[] browserArgs = {"http://www.izforge.com/izpack",
                            "nodebug", "dispose"};
            se.bysoft.sureshot.gui.browser.MiniBrowser.main(browserArgs);

        }
        else if (name.equals(GUIConstants.BUTTON_MAILLIST))
        {
            final String[] browserArgs = {
                            "http://lists.berlios.de/pipermail/izpack-users",
                            "nodebug", "dispose"};
            se.bysoft.sureshot.gui.browser.MiniBrowser.main(browserArgs);
        }
    }

    /**
     * @param xmlFile
     */
    private void openFile(String xmlFile)
    {
        try
        {
            installerUI.initializeFromXML(xmlFile);
        
            RecentFileManager.getInstance().addUsedFile(
                            xmlFile);
            
            installerUI.setVisible(true);
        }
        catch (DocumentCreationException dce)                
        {
            //TODO error handling - anything not XML or malformed XML throws an exception
            UI.showError("Error loading file: " + dce.getLocalizedMessage(), "Error!");
        }
    }
    
    private WizardMode installerUI;
    private WelcomeScreen welcomeUI;
}
