/*
 * Created on Apr 18, 2005
 * 
 * $Id: FileEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileEditor.java 
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
package izpack.frontend.view.stages.packs.editors;

import izpack.frontend.controller.filters.HTMLFilter;
import izpack.frontend.controller.filters.ImageFilter;
import izpack.frontend.controller.filters.TextFilter;
import izpack.frontend.controller.filters.XMLFilter;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.view.components.OSComboBox;
import izpack.frontend.view.components.OverwriteComboBox;
import izpack.frontend.view.components.YesNoRadioPanel;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class FileSetEditor extends TableEditor
{
    public FileSetEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();
        
        target = new JTextField();
        source = new JTextField();
        overwrite = new OverwriteComboBox();
        os = new OSComboBox();
        defaultExcludes = new YesNoRadioPanel("yes");
        caseSensitive = new YesNoRadioPanel("yes");        
        
        fileSetsModel = new FileSet();
        fileSets = new JList(fileSetsModel);
        
        fileSets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        addSet = new JButton(lr.getText("UI.FileSetEditor.AddSet"));
        addSet.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {             
                fileSetsModel.addSet(new SetEditor().showNewSetDialog());
            }            
        });
        removeSet = new JButton(lr.getText("UI.FileSetEditor.RemoveSet"));
        removeSet.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {                
                fileSetsModel.removeSet( (FileSet.Set) fileSets.getSelectedValue());
            }
        });
                
        JPanel setPanel = new JPanel();
        setPanel.setLayout(new BoxLayout(setPanel, BoxLayout.Y_AXIS));
        setPanel.add(addSet);
        setPanel.add(Box.createVerticalStrut(5));
        setPanel.add(removeSet);
        
        browse = new JButton(lr.getText("UI.Buttons.Browse"));
        
        browse.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {        
                JFileChooser chooser = new JFileChooser();
                chooser.addChoosableFileFilter(new ImageFilter());
                chooser.addChoosableFileFilter(new TextFilter());
                chooser.addChoosableFileFilter(new XMLFilter());
                chooser.addChoosableFileFilter(new HTMLFilter());
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);                
                chooser.setDialogTitle(lr.getText("UI.FileEditor.Title"));
                
                if (source.getText().length() != 0)
                {
                    chooser.setSelectedFile(new File(source.getText()));
                }
                
                int returnVal = chooser.showOpenDialog(browse.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {                                          
                       source.setText(chooser.getSelectedFile().getAbsolutePath());                   
                }
            }
        });
        
        builder.append(lr.getText("UI.FileEditor.Source"), source, browse);
        builder.nextLine();
        builder.append(lr.getText("UI.FileEditor.Target"), target);
        builder.nextLine();        
        builder.append("<html>" + lr.getText("UI.FileEditor.Overwrite"), overwrite);
        builder.nextLine();
        builder.append(lr.getText("UI.FileEditor.OS"), os);
        builder.nextLine();        
        
        builder.append(lr.getText("UI.FileSetEditor.Excludes"), defaultExcludes);
        builder.nextLine();
        builder.append(lr.getText("UI.FileSetEditor.CaseSensitive"), caseSensitive);
        builder.nextLine();               
        
        builder.append(new JScrollPane(fileSets), 3);
        builder.append(setPanel);
        
        builder.nextLine();
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        builder.nextLine();
        
        getContentPane().add(builder.getPanel());
        pack();
        getRootPane().setDefaultButton(ok);
        setModal(true);
    }
    
    public void setVisible(boolean b)
    {
        source.requestFocusInWindow();
        
        super.setVisible(b);
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configure(izpack.frontend.model.ElementModel)
     */
    public void configure(ElementModel model)
    {
        FileSet fs = (FileSet) model;

        source.setText(fs.source);
        target.setText(fs.target);
        
        overwrite.setOverwriteOption(fs.override);
        os.setOS(fs.os);
        
        defaultExcludes.setBoolean(fs.defaultExcludes);
        caseSensitive.setBoolean(fs.caseSensitive);
        
        fileSets.setModel(fs);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {        
        source.setText("");
        target.setText("$INSTALL_PATH");

        overwrite.setSelectedIndex(-1);
        os.setSelectedIndex(-1);
        
        defaultExcludes.setSelected("yes");
        caseSensitive.setSelected("yes");
        
        fileSetsModel = null;
        fileSetsModel = new FileSet();
        fileSets.setModel(fileSetsModel);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        FileSet fs = new FileSet();
        
        fs.source = source.getText();
        fs.target = target.getText();
        
        fs.os = os.getOS();
        fs.override = overwrite.getOverwriteOption();
        
        fs.caseSensitive = caseSensitive.getBoolean();
        fs.defaultExcludes = defaultExcludes.getBoolean();
        
        fs.setSetList(fileSetsModel.getSetList());
                
        return fs;
    }

    JButton browse;
    JTextField target, source;    
    OSComboBox os;
    OverwriteComboBox overwrite;
    YesNoRadioPanel defaultExcludes;
    YesNoRadioPanel caseSensitive;
    
    JList fileSets;
    FileSet fileSetsModel;
    JButton addSet, removeSet;
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(FileSet.class);
    }
}
