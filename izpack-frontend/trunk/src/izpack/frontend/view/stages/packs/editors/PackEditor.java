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
import izpack.frontend.view.components.OSComboBox;
import izpack.frontend.view.components.YesNoRadioPanel;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

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
        osBox = new OSComboBox();
        requiredPanel = new YesNoRadioPanel("yes");
        preselectPanel = new YesNoRadioPanel("yes");
        looseFilesPanel = new YesNoRadioPanel("no");        
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        ok.addActionListener(this);
        
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        cancel.addActionListener(this);
        
        builder.append(lr.getText("UI.PackEditor.Name"), name);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.Desc"), desc, 2);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.ID"), id);
        builder.nextLine();
        builder.append(lr.getText("UI.PackEditor.OS"), osBox);
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
        
        //builder.append(createDependencyPanel());
        
        //builder.appendUnrelatedComponentsGapRow();
        //builder.nextLine();
        
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        builder.nextLine();
        
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
        
        super.setVisible(b);
    }
    
    public void configure(ElementModel em)
    {
        PackModel p = (PackModel) em;        
        
        name.setText(p.getName());
        desc.setText(p.getDesc());
        id.setText(p.getId());
        osBox.setOS(p.getOS());
        
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
        p.setOS(osBox.getOS());
        
        p.setRequired(requiredPanel.getBoolean());
        p.setPreselected(preselectPanel.getBoolean());
        p.setLoose(looseFilesPanel.getBoolean());
        
        return p;
    }    
    
    JTextField name, desc, id;
    OSComboBox osBox;
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
        
        osBox.setSelectedIndex(-1);
       
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
}
