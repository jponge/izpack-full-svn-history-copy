/*
 * Created on Apr 11, 2005
 * 
 * $Id: PackCellEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PackCellEditor.java 
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
package izpack.frontend.view.components.table;

import izpack.frontend.model.PackModel;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellEditor;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class PackCellEditor extends AbstractCellEditor implements TableCellEditor
{
    public PackCellEditor()
    {
        panel = new JPanel();
        FormLayout layout = new FormLayout(
                        "5dlu, 80dlu, 4dlu, 80dlu, 15dlu, center:40dlu, 5dlu",
                        "15dlu");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, panel);

        CellConstraints cc = new CellConstraints();

        builder.add(name, cc.xy(2, 1));
        builder.add(desc, cc.xy(4, 1));
        builder.add(required, cc.xy(6, 1));
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column)
    {
        PackModel pm = (PackModel) value;
        
        if (value == null)
            return null;

        name.setText(pm.getName());
        desc.setText(pm.getDesc());
        required.setSelected(pm.isRequired());
     
        editingValue = pm;
        
        if (isSelected)
            panel.setBorder(selected);
        else
            panel.setBorder(null);
        
        return panel;    
    }
    
    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue()
    {         
        editingValue.setName(name.getText());
        editingValue.setDesc(desc.getText());
        editingValue.setRequired(required.isSelected());
        
        return editingValue;        
    }    
        
    JPanel panel;
    JTextField name = new JTextField(), desc = new JTextField();
    JCheckBox required = new JCheckBox();
    PackModel editingValue = null;
    private Border selected = new SoftBevelBorder(SoftBevelBorder.RAISED);
}
