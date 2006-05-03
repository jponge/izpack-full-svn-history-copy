/*
 * Created on Oct 13, 2005
 * 
 * $Id: CompileDisplay.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : CompileDisplay.java 
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

import izpack.frontend.controller.GUIController;
import izpack.frontend.view.IzPackFrame;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import utils.UI;

import com.izforge.izpack.compiler.PackagerListener;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

//TODO Check for .JAR on output - if not present, add
//maybe just a validator?
public class CompileDisplay extends JPanel implements ActionListener
{
    public CompileDisplay(boolean dialog, File f)
    {
        showingInDialog = dialog;
        listeners = new ArrayList<CompileListener>(1); 
        
        setLayout(layout = new CardLayout());
        
        settingsPanel = getSettingsPanel();
        console = getConsole();
        
        if (f != null)
        {
            baseDir.setText(f.getParent());
            
            String filename = f.getAbsolutePath();
            output.setText(filename.substring(0, filename.length() - 3) + "jar");
        }
        
        homeDir.setText(GUIController.getInstance().appConfiguration().getIzpackHome());
        
        add(settingsPanel, SETTINGS_PANEL);
        add(console, CONSOLE_PANEL);        
    }
    
    public void next()
    {        
        layout.next(this);
    }
    
    public void dispose()
    {
        Component c = this;
        
        while ( !(c.getParent() instanceof Window) )
        {
            c = c.getParent();
        }       
        

        ( (Window) c.getParent() ).dispose();
    }
    
    /**
     * Provide browse action
     */
    public void actionPerformed(ActionEvent e)
    {
        File file = null;
        
        if (e.getSource().equals(redoCompile))
        {
            fireCompileCanel();
            
            layout.previous(this);
        }
        else if (e.getSource().equals(browseBase))
        {
            File baseDirFile = null;
            
            if (baseDir.getText() != null && baseDir.getText().length() > 0)
                baseDirFile = new File(baseDir.getText());
                
            file = UI.getFile(UI.getApplicationFrame(), "Select a directory...", baseDirFile, true);

            if (file != null)
                {
                    if (baseDir.getText().length() == 0)
                        baseDir.setText(file.getAbsolutePath());
                    
                    if (output.getText().length() == 0)
                    {
                        String filename = file.getName();
                        output.setText(filename.substring(0, filename.length() - 3) + "jar");
                    }
                }
        }
        else if (e.getSource().equals(browseHome))
        {
            File homeDirFile = null;
            
            if (homeDir.getText() != null && homeDir.getText().length() > 0)
                homeDirFile = new File(homeDir.getText());
                
            file = UI.getFile(UI.getApplicationFrame(), "Select a directory...", homeDirFile, true);

            if (file != null)
            {                    
                homeDir.setText(file.getAbsolutePath());                 
            }
        }
        else
        {
            File outputFile = null;
            if (output.getText() != null && output.getText().length() > 0)
                outputFile = new File(output.getText());    
            
            file = UI.getFile(UI.getApplicationFrame(), "Select an output file", outputFile, false);

            if (file != null)
                output.setText(file.getAbsolutePath());
        }
    }    

    /**
     * Create and layout a pane that asks for compiling information
     * 
     * The client must add a listener to this class that monitors for compile start events     
     * 
     * @return JPanel
     */
    public JPanel getSettingsPanel()
    {
        FormLayout layout = new FormLayout(
                        "pref, 3dlu, center:pref, 3dlu, pref",
                        "pref, 5dlu, pref, 5dlu, pref," //Installer type 
                                        + "10dlu, pref," //Base Directory                                        
                                        + "10dlu, pref," //Home Directory
                                        + "10dlu, pref"); //OK/Cancel

        DefaultFormBuilder builder = new DefaultFormBuilder(layout,
                        new JPanel());

        builder.setColumn(3);

        JLabel label;
        builder.add(label = new JLabel(
                        "What kind of installer do you want to generate?"));
        builder.nextLine(2);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);

        //Use ActionCommand because it makes finding the proper string easier
        JRadioButton std = new JRadioButton("Standard");
        std.setActionCommand("standard");
        std.setSelected(true);
        std.setHorizontalAlignment(SwingConstants.CENTER);

        JRadioButton web = new JRadioButton("Web");
        web.setActionCommand("web");
        web.setHorizontalAlignment(SwingConstants.CENTER);
        
        //TODO temporary
        web.setEnabled(false);

        final ButtonGroup bg = new ButtonGroup();
        bg.add(std);
        bg.add(web);

        ButtonStackBuilder bBuilder = new ButtonStackBuilder();

        bBuilder.addGridded(std);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(web);

        builder.setColumn(3);
        builder.add(bBuilder.getPanel());
        builder.nextRow(2);

        builder.setColumn(1);
        builder.addLabel("<html>Base directory<br>for relative paths");
        builder.nextColumn(2);
        builder.add(baseDir = new JTextField(30));
        builder.nextColumn(2);
        builder.add(browseBase = new JButton("Browse"));
        
        builder.nextLine(2);
        builder.setColumn(1);
        builder.addLabel("IzPack Home directory");
        builder.nextColumn(2);
        builder.add(homeDir = new JTextField(30));
        builder.nextColumn(2);
        builder.add(browseHome = new JButton("Browse"));

        builder.nextLine(2);
        builder.setColumn(1);
        builder.addLabel("Output file");
        builder.nextColumn(2);
        builder.add(output = new JTextField(30));
        builder.nextColumn(2);
        builder.add(browseOutput = new JButton("Browse"));

        browseBase.addActionListener(this);
        browseHome.addActionListener(this);
        browseOutput.addActionListener(this);

        builder.nextLine(2);
        JButton OK = new JButton("Compile");
        
        OK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GUIController.getInstance().appConfiguration().setIzpackHome(homeDir.getText());                
                
                fireCompileEvent(new CompileEvent(bg.getSelection().getActionCommand(),
                                baseDir.getText(),
                                output.getText() ));
            }
        });
        
        JButton cancel = null;
        if (showingInDialog)
        {
             cancel = new JButton("Cancel");
             
             cancel.addActionListener(new ActionListener()
                             {
                                public void actionPerformed(ActionEvent e)
                                {
                                    dispose();                                    
                                }                 
                             });
        }

        builder.setColumn(3);
        
        if (showingInDialog)
            builder.add(ButtonBarFactory.buildCenteredBar(OK, cancel));
        else
            builder.add(ButtonBarFactory.buildCenteredBar(OK));

        builder.setDefaultDialogBorder();
        
        return builder.getPanel();
    }
    
    public CompileConsole getConsole()
    {
        if (console == null)
        {
            console = new CompileConsole();
        
            redoCompile = console.getReturnButton();
            
            redoCompile.addActionListener(this);
        }
        
        return console;
    }
    
    public PackagerListener getPackagerListener()
    {
        return console;
    }
    
    public void addCompileListener(CompileListener cl)
    {
        listeners.add(cl);
    }
    
    public void removeCompileListener(CompileListener cl)
    {
        listeners.remove(cl);
    }
    
    private void fireCompileEvent(CompileEvent ce)
    {        
        for (CompileListener cl : listeners)    
        {
            cl.compileRequested(ce);
        }
    }
    
    private void fireCompileCanel()
    {
        for (CompileListener cl : listeners)    
        {
            cl.cancelCompile();
        }
    }

    private JTextField baseDir, homeDir, output;
    private JButton browseBase, browseHome, browseOutput;
    
    private JPanel settingsPanel;
    private CompileConsole console;
    
    private JButton redoCompile;
    
    private final boolean showingInDialog;
    
    private final ArrayList<CompileListener> listeners;
    
    private final CardLayout layout;
    
    public static final String SETTINGS_PANEL = "SETTINGS";
    public static final String CONSOLE_PANEL = "CONSOLE";
}
