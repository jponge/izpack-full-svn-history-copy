/*
 * Created on Mar 31, 2005
 * 
 * $Id: PackGenerator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PackGenerator.java 
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

import izpack.frontend.model.PackModel;
import izpack.frontend.model.files.ElementModel;
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
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();        
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        
        getContentPane().add(builder.getPanel());
        pack();
        getRootPane().setDefaultButton(ok);
        setModal(true);
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
