/*
 * Created on Apr 22, 2005
 * 
 * $Id: ExecutableEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ExecutableEditor.java 
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

import izpack.frontend.controller.ComboBoxParser;
import izpack.frontend.controller.filters.ExecutableFilter;
import izpack.frontend.controller.filters.JARFilter;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.view.ClassChooser;
import izpack.frontend.view.components.YesNoRadioPanel;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class ExecutableEditor extends TableEditor
{

    /**
     * @param parent
     */
    public ExecutableEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, max(80dlu;p), 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();
        
        target = new JTextField();
        execClass = new JTextField();
        bin = new JRadioButton(lr.getText("UI.ExecutableEditor.Type.Bin"));
        jar = new JRadioButton(lr.getText("UI.ExecutableEditor.Type.JAR"));
        stage = new JComboBox(ComboBoxParser.parseProperty(lr.getText("UI.ComboBox.Stage")));
        failure = new JComboBox(ComboBoxParser.parseProperty(lr.getText("UI.ComboBox.Failure")));
        keep = new YesNoRadioPanel("no");
        
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout());
        typePanel.add(bin);
        typePanel.add(jar);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(bin);
        bg.add(jar);
        
        browse = new JButton(lr.getText("UI.Buttons.Browse"));
        
        browse.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.addChoosableFileFilter(new JARFilter());
                chooser.addChoosableFileFilter(new ExecutableFilter());                
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle(lr.getText("UI.FileEditor.Title"));

                if (target.getText().length() != 0)
                {
                    chooser.setSelectedFile(new File(target.getText()));
                }

                int returnVal = chooser.showOpenDialog(browse.getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    if (chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith("jar"))
                    {
                        ClassChooser cc = new ClassChooser(null, lr.getText("UI.ExecutableEditor.ClassSelect"));
                        execClass.setText(cc.getSelectedClass(chooser.getSelectedFile()));
                        jar.setSelected(true);
                    }
                    target.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        builder.append(lr.getText("UI.ExecutableEditor.Target"), target, browse);
        builder.nextLine();
        builder.append(lr.getText("UI.ExecutableEditor.Class"), execClass);
        builder.nextLine();
        builder.append(lr.getText("UI.ExecutableEditor.Type"), typePanel);
        builder.nextLine();
        builder.append(lr.getText("UI.ExecutableEditor.Stage"), stage);
        builder.nextLine();
        builder.append(lr.getText("UI.ExecutableEditor.Failure"), failure);
        builder.nextLine();
        builder.append(lr.getText("UI.ExecutableEditor.Keep"), keep);
        builder.nextLine();
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        
        getContentPane().add(builder.getPanel());
        pack();
        getRootPane().setDefaultButton(ok);
        setModal(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(Executable.class);        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configure(izpack.frontend.model.ElementModel)
     */
    public void configure(ElementModel model)
    {
        Executable em = (Executable) model;
        
        target.setText(em.target);
        execClass.setText(em.execClass);
        
        if (em.type.equalsIgnoreCase("bin"))
            bin.setSelected(true);
        else
            jar.setSelected(true);
        
        stage.setSelectedItem(em.stage);
        
        failure.setSelectedItem(em.failure);
        
        keep.setSelected(em.keep);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {
        target.setText("");
        execClass.setText("");
        
        bin.setSelected(true);
        
        stage.setSelectedIndex(-1);
        
        failure.setSelectedIndex(-1);
        
        keep.setSelected("no");
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        Executable em = new Executable();
        
        em.target = target.getText();
        em.execClass = execClass.getText();
        
        if (bin.isSelected())
            em.type = "bin";
        else
            em.type = "jar";
        
        em.stage = (String) stage.getSelectedItem();
        
        em.failure = (String) failure.getSelectedItem();
        
        em.keep = keep.getTrueFalse();
        
        return em;
    }

    JButton browse;
    JTextField target;
    JTextField execClass;
    JRadioButton bin, jar;
    JComboBox stage;
    JComboBox failure;
    YesNoRadioPanel keep;
}
