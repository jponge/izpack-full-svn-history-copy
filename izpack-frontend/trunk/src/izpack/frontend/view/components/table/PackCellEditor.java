/*
 * Created on Apr 11, 2005
 * 
 * $Id: PackCellEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackCellEditor.java 
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

package izpack.frontend.view.components.table;

import izpack.frontend.model.PackModel;

import java.awt.Color;
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
