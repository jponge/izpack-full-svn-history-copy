/*
 * Created on Apr 27, 2005
 * 
 * $Id: SetEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : SetEditor.java 
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
