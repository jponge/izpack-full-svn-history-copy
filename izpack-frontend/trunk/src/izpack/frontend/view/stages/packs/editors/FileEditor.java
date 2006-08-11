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

import izpack.frontend.controller.filters.HTMLFilter;
import izpack.frontend.controller.filters.ImageFilter;
import izpack.frontend.controller.filters.TextFilter;
import izpack.frontend.controller.filters.XMLFilter;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.view.components.OSSelector;
import izpack.frontend.view.components.OverwriteComboBox;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */

public class FileEditor extends TableEditor
{
    public FileEditor(Frame parent)
    {
        super(parent);
        setTitle(lr.getText("UI.FileEditor.Title"));
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());        
        builder.setDefaultDialogBorder();
        
        target = new JTextField();
        source = new JTextField();
        overwrite = new OverwriteComboBox();
        os = new OSSelector();
        
        target.getDocument().addDocumentListener(this);
        source.getDocument().addDocumentListener(this);
                
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
        builder.append(lr.getText("UI.FileEditor.OS"), os, 3);
        builder.nextLine();        
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        
        configureValidation();
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
        FileModel fm = (FileModel) model;

        source.requestFocusInWindow();
        source.setText(fm.source);
        target.setText(fm.target);
        
        overwrite.setOverwriteOption(fm.override);
        os.setOsModel(fm.os);
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
        os.setNoneSelected();
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        FileModel fm = new FileModel();
        
        fm.source = source.getText();
        fm.target = target.getText();
        
        fm.os = os.getOsModel();
        fm.override = overwrite.getOverwriteOption();
        
        return fm;
    }

    JButton browse;
    JTextField target, source;    
    OSSelector os;
    OverwriteComboBox overwrite;
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(FileModel.class);
    }

    @Override
    public void configureValidation()
    {   
        ValidationComponentUtils.setMandatory(target, true);
        ValidationComponentUtils.setMandatory(source, true);
        
        ValidationComponentUtils.setMessageKey(target, "Target.file");
        ValidationComponentUtils.setMessageKey(source, "Source.file");        
        
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, ValidationResult.EMPTY);
    }

    @Override
    public ValidationResult validateEditor()
    {        
        ValidationResult vr = new ValidationResult();
        
        if (target.getText().length() == 0)
            vr.add(new PropertyValidationMessage(Severity.ERROR, "is mandatory", target, "Target", "file"));
        
        if (source.getText().length() == 0)
            vr.add(new PropertyValidationMessage(Severity.ERROR, "is mandatory", source, "Source", "file"));
        else        
            if (!new File(source.getText()).exists())
                vr.add(new PropertyValidationMessage(Severity.WARNING, "may not exist", source, "Source", "file"));
        
        target.setToolTipText(null);
        source.setToolTipText(null);
        
        List messages = vr.getMessages();
        for (Object object : messages)
        {
            PropertyValidationMessage message = (PropertyValidationMessage) object;
            ((JComponent) message.target()).setToolTipText(message.formattedText());
        }        
        
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, vr);        
        
        return vr;
    }
}
