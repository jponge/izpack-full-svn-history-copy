/*
 * Created on Apr 11, 2005
 * 
 * $Id: FileCellRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileCellRenderer.java 
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

import izpack.frontend.model.LangResources;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.model.files.PackFileModel;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.components.FormatComboBox;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import utils.UI;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class FileCellRenderer implements IzTableCellRenderer
{
    public FileCellRenderer()
    {
        fileDir = new JPanel(); 
        fileSet = new JPanel();
        parsable = new JPanel();
        executable = new JPanel();
        
        fileType = new JLabel(lr.getText("UI.Editors.File"));
        dirType = new JLabel(lr.getText("UI.Editors.Dir"));
        setType = new JLabel(lr.getText("UI.Editors.FileSet"));
        parseType = new JLabel(lr.getText("UI.Editors.Parsable"));
        execType = new JLabel(lr.getText("UI.Editors.Exec"));
        
        FormLayout layout = new FormLayout(
                        "5dlu, 40dlu, 4dlu, 80dlu, 15dlu, center:80dlu, 5dlu",
                        "15dlu");
        
        //Setup file/directory editor
        fdBuilder = new DefaultFormBuilder(layout, fileDir);        
        
        //Setup file set editor        
        sBuilder = new DefaultFormBuilder(layout, fileSet);
        
        //Setup parsable editor        
        pBuilder = new DefaultFormBuilder(layout, parsable);        
        
        //Setup executable editor
        eBuilder = new DefaultFormBuilder(layout, executable);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column)
    {
        JPanel panel = new JPanel();
        
        if (value != null)
        {
	        if (value instanceof PackFileModel && !(value instanceof FileSet))
	            panel = getFileDirRenderer( (PackFileModel) value);
	        else if (value instanceof FileSet)
	            panel = getFileSetRenderer((FileSet) value);
	        else if (value instanceof Parsable)
	            panel = getParsableRenderer( (Parsable) value);
	        else if (value instanceof Executable)
	            panel = getExecutableRenderer( (Executable) value);        
        }
        
        if (isSelected && value != null)
            panel.setBorder(selected);
        else
            panel.setBorder(null);
        
        return panel;    
    }    
    
    private JPanel getFileDirRenderer(PackFileModel value)
    {   
        CellConstraints cc = new CellConstraints();        
        
        fileDir.removeAll();
        
        if (value instanceof DirectoryModel)
            fdBuilder.add(dirType, cc.xy(2, 1));
        else if (value instanceof FileModel)
            fdBuilder.add(fileType, cc.xy(2, 1));
        
        fdBuilder.add(source, cc.xy(4, 1));
        fdBuilder.add(target, cc.xy(6, 1));
        
        target.setText(value.target);
        source.setText(UI.cutMiddleOfString(value.source));
        
        return fileDir;
    }
    
    private JPanel getFileSetRenderer(FileSet value)
    {   
        target.setText(value.target);
        source.setText(UI.cutMiddleOfString(value.source));
        
        fileSet.removeAll();
     
        CellConstraints cc = new CellConstraints();
        sBuilder.add(setType, cc.xy(2, 1));
        sBuilder.add(source, cc.xy(4, 1));
        sBuilder.add(target, cc.xy(6, 1));
        
        return fileSet;
    }
    
    private JPanel getParsableRenderer(Parsable value)
    {
        target.setText(value.targetfile);
        format.setFormat(value.type);
        
        parsable.removeAll();
        
        CellConstraints cc = new CellConstraints();
        pBuilder.add(parseType, cc.xy(2, 1));
        pBuilder.add(target, cc.xy(4, 1));
        pBuilder.add(format, cc.xy(6, 1));
        
        return parsable;
    }
    
    private JPanel getExecutableRenderer(Executable value)
    {
        target.setText(value.target);
        source.setText(value.execClass);  
        
        executable.removeAll();
        
        CellConstraints cc = new CellConstraints();
        eBuilder.add(execType, cc.xy(2, 1));
        eBuilder.add(target, cc.xy(4, 1));
        eBuilder.add(source, cc.xy(6, 1));
        
        return executable;
    }
        
    JPanel fileDir, fileSet, parsable, executable;
    JLabel fileType, dirType, setType, parseType, execType;
    JLabel target = new JLabel(), source = new JLabel();
    FormatComboBox format = new FormatComboBox();
    DefaultFormBuilder fdBuilder, sBuilder, pBuilder, eBuilder;
    
    static LangResources lr = IzPackFrame.getInstance().langResources();

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.IzTableCellRenderer#getHeight()
     */
    public int getHeight()
    {
        return (int) fileDir.getPreferredSize().getHeight();
    }
}
