/*
 * Created on Apr 7, 2005
 * 
 * $Id: PackCellRenderer.java Feb 8, 2004 izpack-frontend Copyright (C)
 * 2005 Andy Gombos
 * 
 * File : PackCellRenderer.java Description : TODO Add description Author's
 * email : gumbo@users.berlios.de
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

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class PackCellRenderer implements TableCellRenderer, IzTableCellRenderer
{
    public PackCellRenderer()
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
   
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column)
    {
        PackModel pm = (PackModel) value;
        
        if (value == null)
        {               
            return blankPanel;
        }
                
        name.setText(pm.getName());
        desc.setText(pm.getDesc());
        required.setSelected(pm.isRequired());

        if (isSelected)            
            panel.setBorder(selected);
        else
            panel.setBorder(null);
        
        return panel;    
    }
    
    public int getHeight()
    {
        return (int) panel.getPreferredSize().getHeight();
    }
    
    JPanel panel, blankPanel = new JPanel();
    JLabel name = new JLabel(), desc = new JLabel();
    JCheckBox required = new JCheckBox();
}

