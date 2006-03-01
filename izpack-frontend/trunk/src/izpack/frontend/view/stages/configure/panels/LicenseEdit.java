/*
 * Created on Jan 10, 2006
 * 
 * $Id: FileEdit.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FileEdit.java 
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

package izpack.frontend.view.stages.configure.panels;

import izpack.frontend.controller.LicenseLoader;
import izpack.frontend.model.LicenseModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

/**
 * A base class that gives the user a spot to load a file, as well as a text field to edit the file onscreen
 * 
 * Subclasses implement all knowledge about how to handle thier specific type (text editors just dump the file, HTML editors
 * provide a source and rendered pane, etc)
 * 
 * @author Andy Gombos
 */
public abstract class LicenseEdit extends JPanel implements ConfigurePanel, ActionListener
{
    public LicenseEdit(int type)
    {   
        setLayout(new BorderLayout());
        
        licenseType = type;
        
        //The browsing panel that displays the filename
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        
        JButton browse = new JButton("Browse");
        browse.addActionListener(this);
        
        filePanel.add(new JLabel("Filename:"));
        filePanel.add(Box.createHorizontalStrut(10));
        filePanel.add(filename = getStdLicenseComboBox());
        filePanel.add(Box.createHorizontalStrut(15));
        filePanel.add(browse);
        
        filename.addActionListener(this);
        
        add(filePanel, BorderLayout.SOUTH);
        
        chooser = new JFileChooser();        
        
        editorHolder = new JPanel();
        editorHolder.setBorder(new EmptyBorder(5, 5, 5, 5));        
        add(editorHolder, BorderLayout.CENTER);
    }
    
    private JComboBox getStdLicenseComboBox()
    {        
        licenses = LicenseLoader.loadLicences();
        
        //Add on the prefix and extension for the standard licenses based on the type requested
        for (LicenseModel license : licenses)
        {   
            if (licenseType == TEXT)
            {
                license.filename = "res/licenses/" + license.filename + ".txt";
            }
            else if (licenseType == HTML)
            {
                license.filename = "res/licenses/html/" + license.filename + ".html";
            }
        }
        
        JComboBox licenseList = new JComboBox();
    
        licenseList.addItem("<html><font size='+1'><u>GPL Compatible</u></font>");        
        
        for (LicenseModel license : licenses)
        {            
            if (license.gplCompatible)            
                licenseList.addItem(license);            
        }       
        
        licenseList.addItem("<html><font size='+1'><u>GPL Incompatible</u></font>");
        
        for (LicenseModel license : licenses)
        {
            if (!license.gplCompatible)
                licenseList.addItem(license);
        }
        
        return licenseList;
    }

    protected void setTextEditor(JComponent textEditor)
    {
        this.textEditor = textEditor;
        editorHolder.removeAll();
        editorHolder.add(textEditor);
    }
    
    protected JComponent getTextEditor()
    {
        return textEditor;
    }    
    
    protected void addFileFilter(FileFilter filter)
    {
        chooser.addChoosableFileFilter(filter);
    }
    
    /**
     * Shows the browsing dialog, and then tells the subclass to update it's display
     */    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            //Only update if user didn't click cancel
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                String file = chooser.getSelectedFile().getAbsolutePath();
                
                //Add a new combo box entry            
                filename.addItem(file);
                
                updateEditorDisplay(file);
            }
        }
        else if (e.getSource().equals(filename))
        {
            if (filename.getSelectedItem() instanceof LicenseModel)
            {
                LicenseModel license = (LicenseModel) filename.getSelectedItem();
                updateEditorDisplay(license.filename);
            }
        }
    }
    
    /** 
     * Allow subclasses to be notified whenever the file selection changes, but without requiring them to bind to anything
     * 
     * @param filename The new filename to load
     */
    protected abstract void updateEditorDisplay(String filename);
    
    private ArrayList<LicenseModel> licenses;
    private JPanel editorHolder;
    private JComponent textEditor;
    protected JComboBox filename;
    private JFileChooser chooser;
    
    private int licenseType;
    
    public static final int TEXT = 0;
    public static final int HTML = 1;
}
