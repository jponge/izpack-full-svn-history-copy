/*
 * Created on Mar 31, 2005
 * 
 * $Id: PackGenerator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackGenerator.java 
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

import izpack.frontend.model.PackModel;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.DependencyListSelect;
import izpack.frontend.view.components.OSSelector;
import izpack.frontend.view.components.YesNoRadioPanel;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
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
public class PackEditor extends TableEditor
{
    public PackEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 25dlu", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();        
        
        name = new JTextField();
        desc = new JTextField();
        id = new JTextField();
        osSel = new OSSelector();
        requiredPanel = new YesNoRadioPanel("yes");
        preselectPanel = new YesNoRadioPanel("yes");
        looseFilesPanel = new YesNoRadioPanel("no");
        
        name.getDocument().addDocumentListener(this);
        desc.getDocument().addDocumentListener(this);        
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        ok.addActionListener(this);
        
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        cancel.addActionListener(this);
        
        builder.append(lr.getText("UI.PackEditor.Name"), name, 2);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.Desc"), desc, 2);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.ID"), id, 2);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.OS"), osSel, 2);
        builder.nextLine();
        
        builder.appendUnrelatedComponentsGapRow();        
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.Required"), requiredPanel);
        builder.nextLine();
        builder.append("<html>" + lr.getText("UI.PackEditor.PreSel"), preselectPanel);
        builder.nextLine();
        builder.append("<html>" + lr.getText("UI.PackEditor.Loose"), looseFilesPanel);
        builder.nextLine();
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();        
        
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        builder.nextLine();
        
        configureValidation();
        
        getContentPane().add(builder.getPanel());
        pack();
        getRootPane().setDefaultButton(ok);
        setModal(true);
    }    
    
    /**
     * @return
     */
    private JPanel createDependencyPanel()
    {
        JPanel p = new JPanel();        
        
        DependencyListSelect dls = new DependencyListSelect(null);
        dls.setButtonsToDisplay(AbstractListSelect.LEFT_RIGHT_BUTTONS);
        
        p.add(dls);
        
        return p;
    }

    public void setVisible(boolean b)
    {
        name.requestFocusInWindow();
        
        validateEditor();
        
        super.setVisible(b);
    }
    
    public void configure(ElementModel em)
    {
        PackModel p = (PackModel) em;        
        
        name.setText(p.getName());
        desc.setText(p.getDesc());
        id.setText(p.getId());
        osSel.setOsModel(p.getOS());
        
        requiredPanel.setBoolean(p.isRequired());
        preselectPanel.setBoolean(p.isPreselected());
        looseFilesPanel.setBoolean(p.isLoose());
    }
    
    public ElementModel getModel()
    {
        PackModel p = new PackModel();
        
        p.setName(name.getText());
        p.setDesc(desc.getText());
        p.setId(id.getText());
        p.setOSes(osSel.getOsModel());
        
        p.setRequired(requiredPanel.getBoolean());
        p.setPreselected(preselectPanel.getBoolean());
        p.setLoose(looseFilesPanel.getBoolean());
        
        return p;
    }    
    
    JTextField name, desc, id;
    OSSelector osSel;
    YesNoRadioPanel preselectPanel;
    YesNoRadioPanel looseFilesPanel;
    YesNoRadioPanel requiredPanel;    
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {        
        name.setText("");
        desc.setText("");
        id.setText("");
        
        osSel.setNoneSelected();
       
        requiredPanel.setBoolean(true);
        preselectPanel.setBoolean(true);
        looseFilesPanel.setBoolean(false);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(PackModel.class);
    }
    
    @Override
    public void configureValidation()
    {   
        System.out.println("pack config valid");
        ValidationComponentUtils.setMandatory(name, true);
        ValidationComponentUtils.setMandatory(desc, true);
        
        ValidationComponentUtils.setMessageKey(name, "Pack.name");
        ValidationComponentUtils.setMessageKey(desc, "Pack.description");        
        
        validateEditor();
    }

    @Override
    public ValidationResult validateEditor()
    {        
        ValidationResult vr = new ValidationResult();
        
        if (name.getText().length() == 0)
            vr.add(new PropertyValidationMessage(Severity.ERROR, "is mandatory", name, "Pack", "name"));
        
        if (desc.getText().length() == 0)
            vr.add(new PropertyValidationMessage(Severity.ERROR, "is mandatory", desc, "Pack", "description"));
        
        name.setToolTipText(null);
        desc.setToolTipText(null);
        
        List messages = vr.getMessages();
        for (Object object : messages)
        {
            PropertyValidationMessage message = (PropertyValidationMessage) object;
            ((JComponent) message.target()).setToolTipText(message.formattedText());
        }
        
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, vr);
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        
        return vr;
    }
}
