/*
 * Created on Oct 15, 2005
 * 
 * $Id: Compile.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Compile.java 
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
package izpack.frontend.view.stages.compile;

import izpack.frontend.actions.CompileManager;
import izpack.frontend.controller.XMLCreator;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;
import utils.XML;

import com.izforge.izpack.compiler.PackagerListener;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;

public class Compile extends IzPackStage implements ActionListener
{
    public Compile()
    {        
        display = new JPanel();
        console = new JTextArea(25, 50);
        consoleDocument = console.getDocument();
        
        final StringBuffer installType = new StringBuffer();        
        
        FormLayout layout = new FormLayout("pref, 3dlu, center:pref, 3dlu, pref", "pref, 5dlu, pref, 5dlu, pref,"   //Installer type 
                                                         +"10dlu, pref,"                    //Base Directory
                                                         +"10dlu, pref,"                    //Output
                                                         +"10dlu, pref,"                    //OK/Cancel
                                                         +"20dlu, 75dlu");                   //Console
        
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
        
        
        JButton OK = new JButton("Compile");
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                performCompile(bg.getSelection().getActionCommand());
            }            
        });
        
        builder.setColumn(3);
        builder.add(ButtonBarFactory.buildCenteredBar(OK));
        
        builder.nextLine(2);
        
        JScrollPane scroll = new JScrollPane(console);
        console.setLineWrap(false);
        builder.add(scroll, new CellConstraints(1, layout.getRowCount(), layout.getColumnCount(), 1));
        
        add(display);
    }
    
    protected void performCompile(String actionCommand)
    {
        Document xml = new XMLCreator(IzPackStage.getAllStages()).createInstallXML();
        
        //TODO let user pick
        String filename = output.getText().substring(0, output.getText().length() - 3) + "xml";
        
        XML.writeXML(filename, xml);
        
        CompileManager.compile(xml, new String[]{actionCommand, baseDir.getText(), output.getText()}, new CompileListener());
    }

    @Override
    public Element[] createInstallerData(Document doc)
    {        
        return null;
    }

    @Override
    public ValidationResult validateStage()
    {        
        return ValidationResult.EMPTY;
    }

    @Override
    public StageDataModel getDataModel()
    {
        return null;
    }

    public void initializeStage()
    {    
        
    }

    public void initializeStageFromXML(Document doc)
    {
        //Do nothing        
    }

    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return new JPanel();
    }

    private JPanel display;
    private JTextArea console;    
    private javax.swing.text.Document consoleDocument;
    
    private JTextField baseDir, output;
    private JButton browseBase, browseOutput;
    
    public void actionPerformed(ActionEvent e)
    {
        File file = null;
        
        if (e.getSource().equals(browseBase))
        {
            file = UI.getFile(UI.getApplicationFrame(), "Select a directory...", true);
            
            if (file != null)            
                baseDir.setText(file.getAbsolutePath());
        }
        else
        {
            file = UI.getFile(UI.getApplicationFrame(), "Select an output file...", false);
            
            if (file != null)
                output.setText(file.getAbsolutePath());
        }
        
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

}
