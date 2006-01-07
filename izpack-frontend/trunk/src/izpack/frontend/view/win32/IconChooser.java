/*
 * Created on Jan 5, 2006 $Id: IconChooser.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos File : IconChooser.java Description : TODO Add
 * description Author's email : gumbo@users.berlios.de Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package izpack.frontend.view.win32;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EventObject;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import utils.UI;

public class IconChooser extends JDialog
{
    public IconChooser(String filename)
    {
        try
        {
            ni = new NativeIconAccessor(filename);
        }
        catch (NativeIconException nie)
        {
            UI.showError(nie.getMessage(), "Error loading icons");

            return;
        }

        //Confiugure the table        
        JTable table = new JTable((int) Math.ceil(ni.getNumIcons() / 4.0), 4);

        table.setDefaultRenderer(Object.class, new Renderer());
        table.setDefaultEditor(Object.class, new SelectingEditor(this));

        table.setShowGrid(false);
        table.setTableHeader(null);
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);

        //Add the icons to the table
        int numIcon = 0;

        for (int y = 0; y < table.getRowCount(); y++)
        {
            int maxIconHeight = 0;

            for (int x = 0; x < 4; x++)
            {
                if (numIcon < ni.getNumIcons())
                {
                    Data d = new Data();
                    d.image = ni.getIcon(numIcon);
                    d.index = numIcon;

                    table.getModel().setValueAt(d, y, x);

                    if (d.image.getHeight() > maxIconHeight)
                        maxIconHeight = d.image.getHeight();

                    numIcon++;
                }
            }

            table.setRowHeight(y, maxIconHeight + rendererAddHeight);
        }

        //Try to free up all our intermediate objects
        Runtime.getRuntime().gc();

        JScrollPane scroll = new JScrollPane(table);

        setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);

        //Add the information label and the cancel button
        JPanel infoPanel = new JPanel();
        JButton cancel;
        
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));

        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(new JLabel("Double click to select an icon"));
        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(cancel = new JButton("Cancel"));

        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectedIconIndex = -1;
                dispose();
            }
        });
        
        getContentPane().add(infoPanel, BorderLayout.SOUTH);

        setModal(true);
        pack();
    }
    
    public int getSelectedIconIndex()
    {
        return selectedIconIndex;
    }
    
    public BufferedImage getIconImage()
    {
        return ni.getIcon(selectedIconIndex);
    }

    public class Renderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column)
        {
            if (value != null)
            {
                Data data = (Data) value;

                JPanel panel = new JPanel();
                JLabel label = new JLabel(new ImageIcon(data.image));

                panel.setLayout(new BorderLayout());
                panel.setSize(data.image.getWidth(), data.image.getHeight()
                                + rendererAddHeight);

                if (isSelected)
                    panel.setBackground(table.getSelectionBackground());
                else
                    panel.setBackground(table.getBackground());

                panel.add(label);

                return panel;
            }
            else
                return new JLabel("");
        }
    }

    /**
     * Uses the editing framework to provide a double-click selection behavior
     * 
     * @author Andy Gombos
     */
    public class SelectingEditor implements TableCellEditor
    {
        public SelectingEditor(JDialog dialog)
        {
            this.dialog = dialog;
        }

        public Object getCellEditorValue()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isCellEditable(EventObject anEvent)
        {
            if (anEvent instanceof MouseEvent)
            {
                return ((MouseEvent) anEvent).getClickCount() >= 2;
            }
            else
            {
                return false;
            }
        }

        public boolean shouldSelectCell(EventObject anEvent)
        {
            return true;
        }

        public boolean stopCellEditing()
        {
            return true;
        }

        public void cancelCellEditing()
        {
        }

        public void addCellEditorListener(CellEditorListener l)
        {
        }

        public void removeCellEditorListener(CellEditorListener l)
        {
        }

        public Component getTableCellEditorComponent(JTable table,
                        Object value, boolean isSelected, int row, int column)
        {
            //Don't edit, store the cell and close the dialog

            selectedIconIndex = ((Data) value).index;
            dialog.dispose();

            return new JPanel();
        }

        private JDialog dialog;
    }

    public class Data
    {
        BufferedImage image;
        int index;
    }

    private NativeIconAccessor ni = null;
    private int rendererAddHeight = 6;
    private int selectedIconIndex;
}
