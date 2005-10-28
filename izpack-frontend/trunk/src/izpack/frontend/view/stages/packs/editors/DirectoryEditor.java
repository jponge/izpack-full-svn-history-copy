/*
 * Created on Apr 18, 2005
 * 
 * $Id: FileEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FileEditor.java 
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

package izpack.frontend.view.stages.packs.editors;

import izpack.frontend.controller.filters.DirectoryFilter;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.view.components.OSComboBox;
import izpack.frontend.view.components.OverwriteComboBox;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class DirectoryEditor extends TableEditor
{
    public DirectoryEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();
        
        target = new JTextField();
        source = new JTextField();
        overwrite = new OverwriteComboBox();
        os = new OSComboBox();        
                
        browse = new JButton(lr.getText("UI.Buttons.Browse"));
        
        browse.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {        
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new DirectoryFilter());
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setDialogTitle(lr.getText("UI.DirEditor.Title"));
                
                if (source.getText().length() != 0)
                {
                    chooser.setSelectedFile(new File(source.getText()));
                }
                
                int returnVal = chooser.showOpenDialog(browse.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {                                          
                       source.setText(chooser.getSelectedFile().getAbsolutePath() + System.getProperty("file.separator"));                   
                }
            }
        });
        
        builder.append(lr.getText("UI.DirEditor.Source"), source, browse);
        builder.nextLine();
        builder.append(lr.getText("UI.DirEditor.Target"), target);
        builder.nextLine();        
        builder.append("<html>" + lr.getText("UI.DirEditor.Overwrite"), overwrite);
        builder.nextLine();
        builder.append(lr.getText("UI.DirEditor.OS"), os);
        builder.nextLine();
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        
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
        DirectoryModel fm = (DirectoryModel) model;

        source.requestFocusInWindow();
        source.setText(fm.source);
        target.setText(fm.target);
        
        overwrite.setOverwriteOption(fm.override);
        os.setOS(fm.os);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {
        source.requestFocusInWindow();
        source.setText("");
        target.setText("$INSTALL_PATH");

        overwrite.setSelectedIndex(-1);
        os.setSelectedIndex(-1);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        DirectoryModel fm = new DirectoryModel();
        
        fm.source = source.getText();
        fm.target = target.getText();
        
        fm.os = os.getOS();
        fm.override = overwrite.getOverwriteOption();
        
        return fm;
    }

    JButton browse;
    JTextField target, source;    
    OSComboBox os;
    OverwriteComboBox overwrite;
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(DirectoryModel.class);
    }
}
