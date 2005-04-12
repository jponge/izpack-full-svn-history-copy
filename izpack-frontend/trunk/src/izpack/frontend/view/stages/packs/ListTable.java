/*
 * Created on Apr 11, 2005
 * 
 * $Id: ListTable.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : ListTable.java Description : TODO Add description Author's email :
 * gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.view.stages.packs;

import izpack.frontend.model.ElementModel;
import izpack.frontend.model.PackModel;
import izpack.frontend.view.components.table.IzTableCellRenderer;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Andy Gombos
 */
public class ListTable extends JTable implements MouseListener
{
    public ListTable(IzTableCellRenderer renderer, TableCellEditor editor,
                    TableEditor doubleClickEditor)
    {
        model = new DefaultTableModel();
        setModel(model);

        model.addColumn("");

        setDefaultRenderer(Object.class, renderer);
        setDefaultEditor(Object.class, editor);
        setRowHeight(renderer.getHeight());
        model.setRowCount(3);

        addMouseListener(this);
        
        this.renderer = renderer;
        this.editor = editor;
        this.doubleClickEditor = doubleClickEditor;
    }
    
    public void addElement(ElementModel em)
    {
        model.addRow(new Object[]{em});     
    }
    
    public void replaceElement(ElementModel em, int row)
    {
        model.removeRow(row);
        model.insertRow(row, new Object[]{em});
    }
    
    public void addElementWithEditor()
    {
        doubleClickEditor.configureClean();
        doubleClickEditor.setVisible(true);
        
        if (doubleClickEditor.wasOKPressed())
        {
            addElement(doubleClickEditor.getModel());
        }
    }
    
    private void insertElementWithEditor(int row)
    {
        doubleClickEditor.configureClean();
        doubleClickEditor.setVisible(true);
        
        if (doubleClickEditor.wasOKPressed())
        {
            replaceElement(doubleClickEditor.getModel(), row);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        if (!(e.getSource() instanceof JTable)) return;

        //Open the editor
        if (e.getClickCount() == 2)
        {
            JTable src = (JTable) e.getSource();
            //Stop cell editing so that our changes are
            // committed, and not the editor's
            editor.cancelCellEditing();

            int row = src.getSelectedRow(), col = src.getSelectedColumn();                       
            
            //List element is null
            if (model.getValueAt(row, col) == null)
            {
                insertElementWithEditor(row);
            }
            else
            {            
	            doubleClickEditor.configure((ElementModel) src.getValueAt(row, col));
	            doubleClickEditor.setVisible(true);
	
	            if (doubleClickEditor.wasOKPressed())
	            {
	                DefaultTableModel lm = (DefaultTableModel) src.getModel();
	                
	                lm.setValueAt(doubleClickEditor.getModel(), row, col);
	            }
            }
        }
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    private DefaultTableModel model;
    private TableCellEditor editor;
    private TableCellRenderer renderer;
    private TableEditor doubleClickEditor;
    
    private int emptyRowCount = 0;
}
