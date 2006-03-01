/*
 * Created on Nov 28, 2005
 * 
 * $Id: ShortcutRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ShortcutRenderer.java 
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

package izpack.frontend.view.renderers;

import izpack.frontend.model.shortcut.Shortcut;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Render a shortcut into three columns of a table
 * The shortcut data is stored in the first column
 * 
 * TODO add selection coloring
 * @author Andy Gombos
 */
public class ShortcutRenderer extends DefaultTableCellRenderer
{
    public ShortcutRenderer()
    {
        unselected = new JLabel();
        selected = new JLabel();
        
        selected.setOpaque(true);
        selected.setBackground(UIManager.getColor("Table.selectionBackground"));                
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value != null)
        {
            Shortcut s = (Shortcut) table.getValueAt(row, 0);
            String dispLabel;
            
            switch (column)
            {
                //OS
                case 0:
                    dispLabel = s.getModelledOS().toString();
                    break;
                //Name    
                case 1:                    
                    dispLabel = s.getName();
                    break;
                //Target    
                case 2:                    
                    dispLabel = s.getTarget();
                    break;
                default:
                    dispLabel = "";
            }
            
            if (isSelected)                
            {                
                selected.setText(dispLabel);
                return selected;
            }
            else
            {
                unselected.setText(dispLabel);
                return unselected;
            }
        }
        
        // TODO Auto-generated method stub
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
    }
    
    private JLabel unselected;
    private JLabel selected;
}
