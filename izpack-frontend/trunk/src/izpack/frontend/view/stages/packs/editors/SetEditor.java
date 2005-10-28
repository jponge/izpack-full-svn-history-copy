/*
 * Created on Apr 27, 2005
 * 
 * $Id: SetEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : SetEditor.java 
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

import izpack.frontend.model.LangResources;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.model.files.FileSet.Set;
import izpack.frontend.view.IzPackFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class SetEditor extends JDialog implements ActionListener
{
    public SetEditor()
    {
        setTitle(lr.getText("UI.SetEditor.Title"));
        
        FormLayout layout = new FormLayout("pref, 3dlu, max(80dlu;p)", "pref, 3dlu, pref, 6dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());        
        
        builder.setDefaultDialogBorder();
        
        descriptor = new JTextField();
        include = new JRadioButton(lr.getText("UI.SetEditor.Include"));
        exclude = new JRadioButton(lr.getText("UI.SetEditor.Exclude"));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(include);
        bg.add(exclude);
        
        include.setSelected(true);
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        ok.addActionListener(this);
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        cancel.addActionListener(this);
        
        CellConstraints cc = new CellConstraints();        
        builder.add(new JLabel(lr.getText("UI.SetEditor.Name")), new CellConstraints(1, 1), descriptor, cc.xy(3, 1));
        builder.add(include, cc.xy(1, 3));        
        builder.add(exclude, cc.xy(3, 3));        
        builder.add(ButtonBarFactory.buildOKCancelBar(ok, cancel), cc.xyw(1, 5, 3, CellConstraints.CENTER, CellConstraints.FILL));
        
        add(builder.getPanel());        
        pack();
        setModal(true);
    }
    
    public void actionPerformed(ActionEvent e)
    { 
        setVisible(false);
        
        if (e.getSource().equals(cancel))
            cancelled = true;
        else
            cancelled = false;
    }
    
    /**
     * @return
     */
    public Set showNewSetDialog()
    {
        setVisible(true);
        
        if (cancelled)
        {
            return null;
        }
        
        Set set = null;
        
        if (include.isSelected())
            set = new FileSet.Include(descriptor.getText());
        else
            set = new FileSet.Exclude(descriptor.getText());
        
        return set;
    }

    boolean cancelled = false;
    JTextField descriptor;
    JButton ok, cancel;
    JRadioButton include, exclude;
    LangResources lr = IzPackFrame.getInstance().langResources();
}
