/*
 * Created on Oct 13, 2005
 * 
 * $Id: CompileDisplay.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : CompileDisplay.java 
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
package izpack.frontend.actions;

import java.awt.CardLayout;
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
            baseDir.setText(f.getParent());
        
        add(settingsPanel, SETTINGS_PANEL);
        add(console, CONSOLE_PANEL);        
    }
    
    public void next()
    {
        System.out.println("next");
        layout.next(this);
    }
    
    /**
     * Provide browse action
     */
    public void actionPerformed(ActionEvent e)
    {
        File file = null;

        if (e.getSource().equals(browseBase))
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
        builder.addLabel("Output file");
        builder.nextColumn(2);
        builder.add(output = new JTextField(30));
        builder.nextColumn(2);
        builder.add(browseOutput = new JButton("Browse"));

        browseBase.addActionListener(this);
        browseOutput.addActionListener(this);

        builder.nextLine(2);
        JButton OK = new JButton("Compile");
        
        OK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fireCompileEvent(new CompileEvent(bg.getSelection().getActionCommand(),
                                baseDir.getText(),
                                output.getText() ));                
            }
        });
        
        JButton cancel = null;
        if (showingInDialog)
             cancel = new JButton("Cancel");

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
            console = new CompileConsole();
        
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

    private JTextField baseDir, output;
    private JButton browseBase, browseOutput;
    
    private JPanel settingsPanel;
    private CompileConsole console;
    
    private final boolean showingInDialog;
    
    private final ArrayList<CompileListener> listeners;
    
    private final CardLayout layout;
    
    public static final String SETTINGS_PANEL = "SETTINGS";
    public static final String CONSOLE_PANEL = "CONSOLE";
}
