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

import izpack.frontend.model.ElementModel;
import izpack.frontend.model.LangResources;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.PackElement;
import izpack.frontend.model.files.PackFileModel;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.components.FormatComboBox;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class FileCellEditor extends AbstractCellEditor implements TableCellEditor
{
    /*
     * For some reason, the components must be added at show time
     * So only create the layouts now
     * 
     * Add components later
     */
    public FileCellEditor()
    {
        fileDir = new JPanel();
        parsable = new JPanel();
        
        //Setup file/directory editor
        FormLayout fdLayout = new FormLayout(
                        "5dlu, 40dlu, 4dlu, 80dlu, 15dlu, center:80dlu, 5dlu",
                        "15dlu");
        fdBuilder = new DefaultFormBuilder(fdLayout, fileDir);
        
        //Setup parsable editor
        FormLayout pLayout = new FormLayout(
                        "5dlu, 40dlu, 4dlu, 80dlu, 15dlu, center:80dlu, 5dlu",
                        "15dlu");
        pBuilder = new DefaultFormBuilder(pLayout, parsable);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column)
    {
        JPanel panel = new JPanel();
        
        if (value instanceof PackFileModel)
            panel = getFileDirEditor( (PackFileModel) value);
        else if (value instanceof Parsable)
            panel = getParsableEditor( (Parsable) value);
     
        editingValue = (PackElement) value;
        
        return panel;    
    }
    
    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue()
    {   
        if (editingValue instanceof PackFileModel)
        {   
	        ( (PackFileModel) editingValue ).target = target.getText();
	        ( (PackFileModel) editingValue ).source = source.getText();	        
        }
        else if (editingValue instanceof Parsable)
        {
            ( (Parsable) editingValue).targetfile = target.getText();
        }        
        
        return editingValue;        
    }    
    
    private JPanel getFileDirEditor(PackFileModel value)
    {
        if (value instanceof DirectoryModel)
            type.setText(lr.getText("UI.Editors.Dir"));
        else if (value instanceof FileModel)
            type.setText(lr.getText("UI.Editors.File"));        
        
        target.setText(value.target);
        source.setText(value.source);
        
        fileDir.removeAll();
        CellConstraints cc = new CellConstraints();
        fdBuilder.add(type, cc.xy(2, 1));
        fdBuilder.add(source, cc.xy(4, 1));
        fdBuilder.add(target, cc.xy(6, 1));

        
        return fileDir;
    }
    
    private JPanel getParsableEditor(Parsable value)
    {
        type.setText(lr.getText("UI.Editors.Parsable"));
        
        target.setText(value.targetfile);
        format.setFormat(value.type);
        
        parsable.removeAll();
        CellConstraints cc = new CellConstraints();
        pBuilder.add(type, cc.xy(2, 1));
        pBuilder.add(target, cc.xy(4, 1));
        pBuilder.add(format, cc.xy(6, 1));
        
        return parsable;
    }
    
    private JPanel getExecutableEditor(Executable value)
    {
        return null;
    }
        
    JPanel fileDir, parsable, executable;
    JLabel type = new JLabel();
    JTextField target = new JTextField(), source = new JTextField();
    FormatComboBox format = new FormatComboBox();
    DefaultFormBuilder pBuilder, fdBuilder;
    
    PackElement editingValue = null;
    
    static LangResources lr = IzPackFrame.getInstance().langResources();
}
