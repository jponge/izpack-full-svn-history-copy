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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import utils.UI;

import com.izforge.izpack.compiler.PackagerListener;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

public class CompileDisplay implements ActionListener
{
    public CompileDisplay(boolean inDialog)
    {
        display = new JPanel();
        console = new JTextArea(25, 50);
        consoleDocument = console.getDocument();        
        
        if (inDialog)
        {
            frame = new JDialog(UI.getApplicationFrame(), "Installer Compiler", true);
            frame.add(display);
        }
    }    
    
    public String[] getInstallationSettings()
    {
        final StringBuffer installType = new StringBuffer();        
        
        FormLayout layout = new FormLayout("pref, 3dlu, center:pref, 3dlu, pref", "pref, 5dlu, pref, 5dlu, pref,"   //Installer type 
                                                         +"10dlu, pref,"                    //Base Directory
                                                         +"10dlu, pref,"                    //Output
                                                         +"10dlu, pref");                   //OK/Cancel
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, display);
        
        builder.setColumn(3);
        
        JLabel label;
        builder.add(label = new JLabel("What kind of installer do you want to generate?"));
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
        
        builder.setDefaultDialogBorder();
        
        
        JButton OK = new JButton("OK");
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                installType.append(bg.getSelection().getActionCommand());
                
                frame.setVisible(false);
            }            
        });
        
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                installType.append("");
                
                frame.setVisible(false);
            }            
        });
        
        builder.setColumn(3);
        builder.add(ButtonBarFactory.buildCenteredBar(OK, cancel));
        
        frame.pack();
        
        if (!frame.isVisible())
            frame.setVisible(true);
        
        return new String[]{installType.toString(), baseDir.getText(), output.getText()};
    }
    
    public void showCompileStatus()
    {
        JScrollPane scroll = new JScrollPane(console);
        
        console.setLineWrap(false);
        
        JDialog d = new JDialog((Frame) null, "Progress", false);
        d.add(scroll);
        
        d.pack();
        d.setVisible(true);        
    }
    
    class CompileListener implements PackagerListener
    {
        public void packagerMsg(String arg0)
        {
            packagerMsg(arg0, PackagerListener.MSG_INFO);
        }

        public void packagerMsg(String arg0, int arg1)
        {
            System.out.println("msg" + arg0);
            
            final String prefix;
            switch (arg1)
            {
                case MSG_DEBUG:
                    prefix = "[ DEBUG ] ";
                    break;
                case MSG_ERR:
                    prefix = "[ ERROR ] ";
                    break;
                case MSG_WARN:
                    prefix = "[ WARNING ] ";
                    break;
                case MSG_INFO:
                case MSG_VERBOSE:
                default: // don't die, but don't prepend anything
                    prefix = "";
            }

            try
            {
                consoleDocument.insertString(consoleDocument.getLength(), prefix + arg0 + "\n", null);
            }
            catch (BadLocationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void packagerStart()
        {
            try
            {
                consoleDocument.insertString(consoleDocument.getLength(), "Stopped Packaging", null);
            }
            catch (BadLocationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void packagerStop()
        {
            try
            {
                consoleDocument.insertString(consoleDocument.getLength(), "Started Packaging", null);
            }
            catch (BadLocationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static final int DIALOG = 0;
    public static final int PANEL = 1;
    
    private JDialog frame;
    private JPanel display;
    private JTextArea console;    
    private Document consoleDocument;
    
    private JTextField baseDir, output;
    private JButton browseBase, browseOutput;
    
    public PackagerListener getPackagerListener()
    {
        return new CompileListener();        
    }

    public void actionPerformed(ActionEvent e)
    {
        File file = null;
        
        if (e.getSource().equals(browseBase))
        {
            file = UI.getFile(frame.getParent(), "Select a directory...", true);
            
            if (file != null)            
                baseDir.setText(file.getAbsolutePath());
        }
        else
        {
            file = UI.getFile(frame.getParent(), "Select an output file...", false);
            
            if (file != null)
                output.setText(file.getAbsolutePath());
        }
    }
}
