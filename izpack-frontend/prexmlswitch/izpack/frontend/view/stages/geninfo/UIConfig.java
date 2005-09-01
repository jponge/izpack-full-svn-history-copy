/*
 * Created on Mar 30, 2005
 * 
 * $Id: UIConfig.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : UIConfig.java 
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
package izpack.frontend.view.stages.geninfo;

import izpack.frontend.view.components.YesNoCheckBox;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.configure.panels.ConfigurePanel;
import izpack.frontend.view.stages.configure.panels.IzPackPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */
public class UIConfig extends IzPackPanel
{
    /**
     * @param information
     */
    public UIConfig(IzPackStage stage)
    {        
        super(stage);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.Page#initComponents()
     * 
     * TODO Implement LAF preferences 
     */
    public void initComponents()
    {
        System.out.println("init");
        
        FormLayout layout = new FormLayout("pref, 3dlu, pref, 30dlu",
        "pref, 3dlu, pref, 3dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        
        pm = stage.getValidatingModel();
        
        //TODO internationailze
        resizable = new YesNoCheckBox("Allow window resizes");
        
        width = BasicComponentFactory.createIntegerField(pm.getModel("width"));
        width.setColumns(5);
        height = BasicComponentFactory.createIntegerField(pm.getModel("height"));
        height.setColumns(5);
        Bindings.bind(resizable, pm.getModel("resizable"));
        
        builder.add(resizable, new CellConstraints(1, 1, 4, 1));
        builder.nextRow(2);        
        builder.add(new JLabel("Width"));
        builder.setColumn(3);
        builder.add(width);
        builder.nextRow(2);
        builder.setColumn(1);
        builder.add(new JLabel("Height"));
        builder.setColumn(3);
        builder.add(height);
        
        configureValidation();
    }
    
    private void configureValidation()
    {     
        ValidationComponentUtils.setMandatory(height, true);
        ValidationComponentUtils.setMandatory(width, true);
        
        ValidationComponentUtils.setMessageKey(height, "GUIPrefs.height");
        ValidationComponentUtils.setMessageKey(width, "GUIPrefs.width");
        
        ValidationComponentUtils.updateComponentTreeValidationBackground(this, ValidationResult.EMPTY);
    }
    
    PresentationModel pm;
    YesNoCheckBox resizable;
    JTextField width, height;
}
