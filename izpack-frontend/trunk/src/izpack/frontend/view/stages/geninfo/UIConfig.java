/*
 * Created on Mar 30, 2005
 * 
 * $Id: UIConfig.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : UIConfig.java 
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

package izpack.frontend.view.stages.geninfo;

import izpack.frontend.view.components.YesNoCheckBox;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.configure.panels.IzPackPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

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
