/*
 * Created on Apr 18, 2005
 * 
 * $Id: ParsableEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ParsableEditor.java 
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

import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.components.FormatComboBox;
import izpack.frontend.view.components.OSSelector;
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
public class ParsableEditor extends TableEditor
{
    public ParsableEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();
        
        targetFile = new JTextField();
        format = new FormatComboBox();
        encoding = new JTextField();
        os = new OSSelector();
        
        targetFile.getDocument().addDocumentListener(this);
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        ok.addActionListener(this);
        
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        cancel.addActionListener(this);
        
        browse = new JButton(lr.getText("UI.Buttons.Browse"));
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();                
                int returnVal = chooser.showOpenDialog(browse.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   targetFile.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }            
        });        
               
        builder.append(lr.getText("UI.ParsableEditor.TargetFile"), targetFile, browse);
        builder.nextLine();        
        builder.append(lr.getText("UI.ParsableEditor.Format"), format);
        builder.nextLine();
        builder.append(lr.getText("UI.ParsableEditor.Encoding"), encoding);
        builder.nextLine();
        builder.append(lr.getText("UI.ParsableEditor.OS"), os, 3);
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
        targetFile.requestFocusInWindow();
        
        super.setVisible(b);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configure(izpack.frontend.model.ElementModel)
     */
    public void configure(ElementModel model)
    {
        Parsable pm = (Parsable) model;
        
        targetFile.requestFocusInWindow();
        targetFile.setText(pm.targetfile);
        encoding.setText(pm.encoding);
        
        format.setFormat(pm.type);
        os.setOsModel(pm.os);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {
        targetFile.requestFocusInWindow();
        targetFile.setText("");
        encoding.setText("");
        
        format.setSelectedIndex(-1);
        os.setNoneSelected();        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        Parsable pm = new Parsable();
        
        pm.targetfile = targetFile.getText();
        pm.encoding = encoding.getText();
        
        pm.type = format.getFormat();
        pm.os = os.getOsModel();
        
        return pm;
    }
    
    JButton browse;
    JTextField targetFile;
    FormatComboBox format;
    JTextField encoding;
    OSSelector os;
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(Parsable.class);
    }   
    
    @Override
    public void configureValidation()
    {   
        ValidationComponentUtils.setMandatory(targetFile, true);        
        
        ValidationComponentUtils.setMessageKey(targetFile, "Target.file");                
        
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, ValidationResult.EMPTY);
    }

    @Override
    public ValidationResult validateEditor()
    {        
        ValidationResult vr = new ValidationResult();        
        
        if (targetFile.getText().length() == 0)
            vr.add(new PropertyValidationMessage(Severity.ERROR, "is mandatory", targetFile, "Target", "file"));
        else if (!new File(targetFile.getText()).exists())
            vr.add(new PropertyValidationMessage(Severity.WARNING, "may not exist", targetFile, "Target", "file"));
        
        targetFile.setToolTipText(null);
        
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
