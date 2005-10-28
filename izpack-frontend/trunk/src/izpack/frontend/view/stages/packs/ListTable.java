/*
 * Created on Apr 11, 2005
 * 
 * $Id: ListTable.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : ListTable.java Description : TODO Add description Author's email :
 * gumbo@users.berlios.de
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

package izpack.frontend.view.stages.packs;

import izpack.frontend.model.files.ElementModel;
import izpack.frontend.view.components.table.IzTableCellRenderer;
import izpack.frontend.view.components.table.TableEditor;
import izpack.frontend.view.stages.packs.editors.EditorManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * @author Andy Gombos
 */
public class ListTable extends JTable implements MouseListener
{
    public ListTable(IzTableCellRenderer renderer, TableCellEditor editor,
                    EditorManager em)
    {
        model = new DefaultTableModel();
        setModel(model);

        model.addColumn("");

        setDefaultRenderer(Object.class, renderer);
        setDefaultEditor(Object.class, editor);
        setRowHeight(renderer.getHeight());
        
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        addMouseListener(this);
        
        this.renderer = renderer;
        this.editor = editor;        
        
        this.em = em;
    }
    
    
    //Allow model changes
    public void setModel(TableModel model)
    {
        super.setModel(model);
        
        this.model = (DefaultTableModel) model;
    }
    
    
    //Doesn't do much currently
    //Marked for removal
    public void setVisibleRows(int rowCount)
    {
        model.setRowCount(rowCount);
    }
    
    public void addElement(ElementModel em)
    {
        boolean elementAdded = false;
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (model.getValueAt(i, 0) == null)
            {
                replaceElement(em, i);
                elementAdded = true;
                break;
            }
        }    
        
        if (!elementAdded)
            model.addRow(new Object[]{em});
    }
    
    public void replaceElement(ElementModel eModel, int row)
    {
        model.removeRow(row);
        model.insertRow(row, new Object[]{eModel});
    }
    
    public boolean addElementWithEditor(Class type)
    {        
        doubleClickEditor = em.getEditor(type);
        doubleClickEditor.configureClean();
        doubleClickEditor.setVisible(true);
        
        if (doubleClickEditor.wasOKPressed())
        {
            addElement(doubleClickEditor.getModel());
            return true;
        }
        
        return false;
    }
    
    private void insertElementWithEditor(int row, Class type)
    {
        doubleClickEditor = em.getEditor(type);
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
            
            //Ignore list element is null
            //Doesn't make sense for files panel
            if (model.getValueAt(row, col) != null)            
            {            
                doubleClickEditor = em.getEditor(model.getValueAt(row, col).getClass());
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
    private EditorManager em;
    
    private int emptyRowCount = 0;
}
