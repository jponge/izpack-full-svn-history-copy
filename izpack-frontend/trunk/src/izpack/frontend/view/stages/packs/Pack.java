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

import izpack.frontend.model.LangResources;
import izpack.frontend.model.PackModel;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.components.table.FileCellEditor;
import izpack.frontend.view.components.table.FileCellRenderer;
import izpack.frontend.view.components.table.FileListHeader;
import izpack.frontend.view.components.table.PackCellEditor;
import izpack.frontend.view.components.table.PackCellRenderer;
import izpack.frontend.view.components.table.PackListHeader;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.packs.editors.DirectoryEditor;
import izpack.frontend.view.stages.packs.editors.EditorManager;
import izpack.frontend.view.stages.packs.editors.ExecutableEditor;
import izpack.frontend.view.stages.packs.editors.FileEditor;
import izpack.frontend.view.stages.packs.editors.FileSetEditor;
import izpack.frontend.view.stages.packs.editors.PackEditor;
import izpack.frontend.view.stages.packs.editors.ParsableEditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Document;

import utils.XML;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;


/**
 * @author Andy Gombos
 */
public class Pack extends IzPackStage implements ActionListener
{
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {
        for (int row = 0; row < packTable.getRowCount(); row++)
        {
            Object rowObj = packTable.getValueAt(row, 0);
            
            if (rowObj != null)
            {
                Document d = ( (PackModel) rowObj).writePack();
                System.out.println("~~~~~");
                XML.printXML(d);
               	System.out.println("~~~~~~");
            }
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = new ValidationResult();        
        
        int lastRow = 0;
        
        for (int i = 0; i < packTable.getRowCount(); i++)
        {
            if (packTable.getValueAt(i, 0) == null)
            {
                lastRow = --i;
                break;
            }
        }
        
        
        if (lastRow < 0)
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have at least one pack created",
                            packTable,
                            "Packs",
                            "table"
                            ));      
        }
        
        for (int row = 0; row < packTable.getRowCount(); row++)
        {
            Object rowObj = packTable.getValueAt(row, 0);
            
            if (rowObj == null)
                continue;
            
            PackModel pm = (PackModel) rowObj;            
          
            
            for (int i = 0; i < packTable.getRowCount(); i++)
            {
                if (pm.getFilesModel().getValueAt(i, 0) == null)
                {
                    lastRow = --i;
                    break;
                }
            }
            
	        if (lastRow < 0)
	        {
	            vr.add(new PropertyValidationMessage(
	                            Severity.ERROR,
	                            "must have at least one file added to a pack",
	                            filesTable,
	                            "Pack",
	                            "files"
	                            ));      
	        }
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
        
        EditorManager em = EditorManager.getInstance();
        em.addEditor(new PackEditor( (Frame) this.getParent()));
        em.addEditor(new FileEditor( (Frame) this.getParent()));
        em.addEditor(new FileSetEditor( (Frame) this.getParent()));
        em.addEditor(new DirectoryEditor( (Frame) this.getParent()));
        em.addEditor(new ParsableEditor( (Frame) this.getParent()));
        em.addEditor(new ExecutableEditor( (Frame) this.getParent()));
        
        JPanel packPanel = createPackTable();
        JPanel filesPanel = createFilesTable();        
        
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Packs", 				cc.xyw(1, 1, 3));        
        builder.add(packPanel,	 					cc.xy(1, 3));
        
        builder.add(createPackButtons(), 	cc.xy(3, 3));
        
        builder.addSeparator("Files in pack", 		cc.xyw(1, 5, 3));
        builder.add(filesPanel,		 				cc.xy(1, 7));
        builder.add(createFileButtons(),	cc.xy(3, 7));
    }
    
    private JPanel createPackTable()
    {        
       packTable = new ListTable(new PackCellRenderer(), new PackCellEditor(), EditorManager.getInstance());
       packTable.setVisibleRows(5);
       

        //Register for notification of selection changes.
        //Used to display the correct files model       
        //Adapted from the Sun tutorials
        ListSelectionModel lsm = packTable.getSelectionModel();
        lsm.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                //Ignore extra messages
                if (e.getValueIsAdjusting())    
                    return;
                

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty())
                {
                    int selectedRow = lsm.getMinSelectionIndex();
                    
                    Object row = packTable.getValueAt(selectedRow, 0);
                    if (row != null && row instanceof PackModel)
                    {
                        PackModel pm = (PackModel) row;                        
                        
                        filesTable.setModel(pm.getFilesModel());                        
                    }
                }
            }
        });
       
       return configureTable(packTable, new PackListHeader());
    }
    
    private JPanel createFilesTable()
    {
        filesTable = new ListTable(new FileCellRenderer(), new FileCellEditor(), EditorManager.getInstance());
        filesTable.setVisibleRows(15);
        
        return configureTable(filesTable, new FileListHeader());
    }
    
    private JPanel configureTable(ListTable table, JPanel header)
    {           
        table.setShowGrid(false);
        table.setBackground(getBackground());
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        
        table.setTableHeader(null);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));       
        
        p.add(header);
        p.add(createScrollableTable(table));
        
        return p;
    }
    
    private JScrollPane createScrollableTable(ListTable table)
    {
        JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        scroll.setViewportView(table);
        
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        
        return scroll;
    }
    
    private JPanel createPackButtons()
    {
        ButtonStackBuilder builder = new ButtonStackBuilder();        
        
        packAdd = new JButton(lr.getText("UI.PackStage.Pack.Add"));
        	packAdd.setName("packAdd");
        	packAdd.addActionListener(this);
        packRemove = new JButton(lr.getText("UI.PackStage.Pack.Remove"));
        	packRemove.setName("packRemove");
        	packRemove.addActionListener(this);
        
        builder.addGridded(packAdd);
        builder.addUnrelatedGap();
        builder.addUnrelatedGap();
        builder.addUnrelatedGap();
        builder.addGridded(packRemove);
        
        return builder.getPanel();
    }
    
    private JPanel createFileButtons()
    {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        
        fileAdd = new JButton(lr.getText("UI.Editors.File"));
        	fileAdd.setName("file");
        	fileAdd.addActionListener(this);
        dirAdd = new JButton(lr.getText("UI.Editors.Dir"));
        	dirAdd.setName("dir");
        	dirAdd.addActionListener(this);
        setAdd = new JButton(lr.getText("UI.Editors.FileSet"));
        	setAdd.setName("set");
        	setAdd.addActionListener(this);	
        parseAdd = new JButton(lr.getText("UI.Editors.Parsable"));
        	parseAdd.setName("parse");
        	parseAdd.addActionListener(this);
        execAdd = new JButton(lr.getText("UI.Editors.Exec"));
        	execAdd.setName("exec");
        	execAdd.addActionListener(this);        
        fileRemove = new JButton(lr.getText("UI.PackStage.Files.Remove"));
    		fileRemove.setName("removeFile");
    		fileRemove.addActionListener(this);
        
        builder.addGridded(DefaultComponentFactory.getInstance().createSeparator(
                        lr.getText("UI.PackStage.Files.Add"), SwingConstants.CENTER));
        builder.addGridded(fileAdd);
        builder.addRelatedGap();
        builder.addGridded(dirAdd);
        builder.addRelatedGap();
        builder.addGridded(setAdd);
        builder.addRelatedGap();
        builder.addGridded(parseAdd);
        builder.addRelatedGap();
        builder.addGridded(execAdd);
        builder.addUnrelatedGap();
        builder.addUnrelatedGap();
        builder.addUnrelatedGap();
        builder.addGridded(fileRemove);
        
        return builder.getPanel();
    }    
    
    ListTable packTable, filesTable;
    JButton packAdd, packRemove;
    JButton fileAdd, dirAdd, setAdd, parseAdd, execAdd, fileRemove;
    
    LangResources lr = IzPackFrame.getInstance().langResources();
    
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

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (! (e.getSource() instanceof JButton) )
            return;
        
        JButton src = (JButton) e.getSource();
        Class type = null;
        
        //Pack table buttons
        if (src.getName().equals("packAdd"))
        {
            packTable.addElementWithEditor(PackModel.class);      
            
            
            //Change the model so that the first added pack gets its files
            //set correctly
            for (int row = 0; row < packTable.getRowCount(); row++)
            {
                Object rowObj = packTable.getValueAt(row, 0);
                
                if (rowObj == null)
                {
                    row--;
                    PackModel pm = (PackModel) packTable.getValueAt(row, 0);
                    filesTable.setModel(pm.getFilesModel());
                    
                    return;
                }
            }                
        }
        else if (src.getName().equals("packRemove"))
        {
            DefaultTableModel model = (DefaultTableModel) packTable.getModel();
            
            if (packTable.getEditingRow() != -1)
            {
                int row = packTable.getEditingRow();
                
                //Make the cell not editable, so we can delete it
                //Otherwise, the row after is deleted. Makes sense, huh
                packTable.getCellEditor().stopCellEditing();
                
                //Remove row, and keep the table length the same
                model.removeRow(row);
                model.addRow(new Object[]{});
            }
            else if (filesTable.getSelectedRow() != -1)
            {
                model.removeRow(packTable.getSelectedRow());
                model.addRow(new Object[]{});
            }
        }
        
        //File table buttons
        if (src.getName().equals("file"))
        {
            type = FileModel.class;
        }
        else if (src.getName().equals("dir"))
        {
            type = DirectoryModel.class;
        }
        else if (src.getName().equals("set"))
        {
            System.out.println("file set");
            type = FileSet.class;
        }
        else if (src.getName().equals("parse"))
        {
            type = Parsable.class;
        }
        else if (src.getName().equals("exec"))
        {
            type = Executable.class;
        }
        else if (src.getName().equals("removeFile"))
        {   
            DefaultTableModel model = (DefaultTableModel) filesTable.getModel();
            
            if (filesTable.getEditingRow() != -1)
            {                
                int row = filesTable.getEditingRow();
                
                //Make the cell not editable, so we can delete it
                //Otherwise, the row after is deleted. Makes sense, huh
                filesTable.getCellEditor().stopCellEditing();
                
                //Remove row, and keep the table length the same
                model.removeRow(row);
                model.addRow(new Object[]{});
            }
            else if (filesTable.getSelectedRow() != -1)
            {                
                model.removeRow(filesTable.getSelectedRow());
                model.addRow(new Object[]{});
            }
        }
        
        if (type != null)
        {
            filesTable.addElementWithEditor(type);
        }            
    }
}
