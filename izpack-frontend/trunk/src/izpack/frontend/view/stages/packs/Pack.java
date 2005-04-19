/*
 * Created on Mar 31, 2005
 * 
 * $Id: Pack.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Pack.java 
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
package izpack.frontend.view.stages.packs;

import izpack.frontend.model.PackModel;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.components.AddRemovePanel;
import izpack.frontend.view.components.table.FileCellEditor;
import izpack.frontend.view.components.table.FileCellRenderer;
import izpack.frontend.view.components.table.FileListHeader;
import izpack.frontend.view.components.table.PackCellEditor;
import izpack.frontend.view.components.table.PackCellRenderer;
import izpack.frontend.view.components.table.PackListHeader;
import izpack.frontend.view.stages.IzPackStage;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.w3c.dom.Document;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;


/**
 * @author Andy Gombos
 */
public class Pack extends IzPackStage
{

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = new ValidationResult();
        
        if (packTable.getRowCount() == 0)
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have at least pack created",
                            packTable,
                            "Packs",
                            "table"
                            ));      
        }
        
        for (int row = 0; row < packTable.getRowCount(); row++)
        {
	        /*if (filesTable.getRowCount() == 0)
	        {
	            vr.add(new PropertyValidationMessage(
	                            Severity.ERROR,
	                            "must have at least pack created",
	                            packTable,
	                            "Packs",
	                            "table"
	                            ));      
	        }*/
        }
        
        return vr;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#initializeStage()
     */
    public void initializeStage()
    {        
        FormLayout layout = new FormLayout("pref, 3dlu, pref", "pref, 3dlu, pref, 10dlu, pref, 3dlu, pref");        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);        
        
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        packs = new AddRemovePanel("Pack");
        files = new AddRemovePanel("File");
        
        packs.attachAddListener(new ActionListener()
                        {

                            public void actionPerformed(ActionEvent e)
                            {   
                                packTable.addElementWithEditor(PackModel.class);
                            }
            
                        });
        files.attachAddListener(new ActionListener()
                        {

                            public void actionPerformed(ActionEvent e)
                            {   
                                filesTable.addElementWithEditor(Parsable.class);
                            }
            
                        });
        
        files.attachRemoveListener(new ActionListener()
                        {

                            public void actionPerformed(ActionEvent e)
                            {   
                                filesTable.addElementWithEditor(DirectoryModel.class);
                            }
            
                        });
        
        EditorManager em = EditorManager.getInstance();
        em.addEditor(new PackEditor( (Frame) this.getParent()));
        em.addEditor(new FileEditor( (Frame) this.getParent()));
        em.addEditor(new DirectoryEditor( (Frame) this.getParent()));
        em.addEditor(new ParsableEditor( (Frame) this.getParent()));
        
        JPanel packPanel = createPackTable();
        JPanel filesPanel = createFilesTable();        
        
        builder.addSeparator("Packs", 				cc.xyw(1, 1, 3));        
        builder.add(packPanel,	 					cc.xy(1, 3));
        
        builder.add(packs, 	cc.xy(3, 3));
        
        builder.addSeparator("Files in pack", 		cc.xyw(1, 5, 3));
        builder.add(filesPanel,		 				cc.xy(1, 7));
        builder.add(files,	cc.xy(3, 7));
    }
    
    private JPanel createPackTable()
    {        
       packTable = new ListTable(new PackCellRenderer(), new PackCellEditor(), EditorManager.getInstance());
       return configureTable(packTable, new PackListHeader());
    }
    
    private JPanel createFilesTable()
    {
        filesTable = new ListTable(new FileCellRenderer(), new FileCellEditor(), EditorManager.getInstance());
        return configureTable(filesTable, new FileListHeader());
    }
    
    private JPanel configureTable(ListTable table, JPanel header)
    {   
        table.setShowVerticalLines(false);
        table.setBorder(new LineBorder(Color.BLACK));
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));       
        
        p.add(header);
        p.add(table);
        
        return p;
    }
    
    AddRemovePanel packs, files;
    ListTable packTable, filesTable;
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getLeftNavBar()
     */
    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getTopNavBar()
     */
    public JPanel getTopNavBar()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
