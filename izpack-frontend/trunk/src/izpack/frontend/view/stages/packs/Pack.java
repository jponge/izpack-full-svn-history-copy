/*
 * Created on Mar 31, 2005
 * 
 * $Id: Pack.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Pack.java 
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

package izpack.frontend.view.stages.packs;

import izpack.frontend.controller.validators.PackStageValidator;
import izpack.frontend.model.LangResources;
import izpack.frontend.model.PackModel;
import izpack.frontend.model.files.DirectoryModel;
import izpack.frontend.model.files.Executable;
import izpack.frontend.model.files.FileModel;
import izpack.frontend.model.files.FileSet;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.model.stages.PackStageModel;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.components.table.FileCellEditor;
import izpack.frontend.view.components.table.FileCellRenderer;
import izpack.frontend.view.components.table.FileListHeader;
import izpack.frontend.view.components.table.PackCellEditor;
import izpack.frontend.view.components.table.PackCellRenderer;
import izpack.frontend.view.components.table.PackListHeader;
import izpack.frontend.view.components.table.TableEditor;
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
import java.util.Iterator;
import java.util.ListIterator;

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
import org.w3c.dom.Element;

import utils.UI;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class Pack extends IzPackStage implements ActionListener,
                ListSelectionListener
{
    private PackStageModel model;

    public Pack()
    {
        super();

        model = new PackStageModel();
        model.addColumn("");

        validator = new PackStageValidator(model);

        FormLayout layout = new FormLayout("pref, 3dlu, pref",
                        "pref, 3dlu, pref, 10dlu, pref, 3dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);

        builder.setDefaultDialogBorder();

        EditorManager em = EditorManager.getInstance();

        Frame parent = null;

        em.addEditor(new PackEditor(parent));
        em.addEditor(new FileEditor(parent));
        em.addEditor(new FileSetEditor(parent));
        em.addEditor(new DirectoryEditor(parent));
        em.addEditor(new ParsableEditor(parent));
        em.addEditor(new ExecutableEditor(parent));

        JPanel packPanel = createPackTable();
        JPanel filesPanel = createFilesTable();

        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Packs", cc.xyw(1, 1, 3));
        builder.add(packPanel, cc.xy(1, 3));

        builder.add(createPackButtons(), cc.xy(3, 3));

        builder.addSeparator("Files in pack", cc.xyw(1, 5, 3));
        builder.add(filesPanel, cc.xy(1, 7));
        builder.add(createFileButtons(), cc.xy(3, 7));
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Element[] createInstallerData(Document doc)
    {
        return model.writeToXML(doc);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#initializeStage()
     */
    public void initializeStage()
    {    
         Frame parent = UI.getApplicationFrame();
         
         //Set the parent component
         ListIterator li = EditorManager.getInstance().getEditorIterator();
         for (Iterator iter = li; iter.hasNext();)
         {
             TableEditor element = (TableEditor) iter.next();
             element.setLocationRelativeTo(parent);
         }
    }

    private JPanel createPackTable()
    {
        packTable = new ListTable(new PackCellRenderer(), new PackCellEditor(),
                        EditorManager.getInstance());
        packTable.setModel(model);
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
                if (e.getValueIsAdjusting()) return;

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
        filesTable = new ListTable(new FileCellRenderer(),
                        new FileCellEditor(), EditorManager.getInstance());
        filesTable.setVisibleRows(10);

        return configureTable(filesTable, new FileListHeader());
    }

    private JPanel configureTable(ListTable table, JPanel header)
    {
        table.setShowGrid(false);
        table.setBackground(getBackground());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setTableHeader(null);

        table.getSelectionModel().addListSelectionListener(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(header);
        p.add(createScrollableTable(table));

        return p;
    }

    private JScrollPane createScrollableTable(ListTable table)
    {
        JScrollPane scroll = new JScrollPane();
        scroll
                        .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

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

        builder.addGridded(DefaultComponentFactory.getInstance()
                        .createSeparator(lr.getText("UI.PackStage.Files.Add"),
                                        SwingConstants.CENTER));
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
        return new JPanel();
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        return super.getBottomInfoBar();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (!(e.getSource() instanceof JButton)) return;

        JButton src = (JButton) e.getSource();
        Class type = null;

        //Pack table buttons
        if (src.getName().equals("packAdd"))
        {
            //Make sure OK was pressed when adding a pack
            if (packTable.addElementWithEditor(PackModel.class))
            {
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

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#getDataModel()
     */
    public StageDataModel getDataModel()
    {
        // TODO Auto-generated method stub
        return model;
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e)
    {
        validateStage();
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = validator.validate();
        validationModel.setResult(vr);

        return vr;
    }

    private static PackStageValidator validator;

    public void initializeStageFromXML(Document doc)
    {
        model.initFromXML(doc);
    }

}
