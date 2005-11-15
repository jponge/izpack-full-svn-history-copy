/*
 * Created on Nov 7, 2005
 * 
 * $Id: ShortcutPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ShortcutPanel.java 
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

package izpack.frontend.view.stages.configure.panels;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ShortcutPanel extends JPanel implements ConfigurePanel
{
    public ShortcutPanel()
    {
        setLayout(new BorderLayout());
        
        JPanel shortcutTablePanel = new JPanel();
        
        FormLayout topLayout = new FormLayout("pref:grow, 5dlu, pref", "pref, 0dlu, pref");
        DefaultFormBuilder topBuilder = new DefaultFormBuilder(topLayout, shortcutTablePanel);
        
        shortcutTable = new JTable(new Object[][]{{"a", "b", "C"}, {"d", "e", "f"}, {"g", "h", "i"}}, new Object[]{"OS", "Name", "Target"});        
        
        //Create the buttons to add and remove shortcuts
        ButtonStackBuilder bsb = new ButtonStackBuilder();        
        bsb.addButtons(new JButton[]{new JButton("Add"), new JButton("Remove"), new JButton("Duplicate")});
        
        //Add the components
        CellConstraints cc = new CellConstraints();
        
        topBuilder.add(shortcutTable.getTableHeader(), cc.xy(1, 1));
        topBuilder.add(shortcutTable, cc.xyw(1, 3, 1, CellConstraints.FILL, CellConstraints.TOP));
        topBuilder.add(bsb.getPanel(), cc.xy(3, 3));
        
        add(shortcutTablePanel, BorderLayout.NORTH);
        
        //Configure the panel to respond to selection changes for the editing view
        shortcutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shortcutTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {

            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    shortcutView.removeAll();
                    shortcutView.add(new JLabel((String) shortcutTable.getValueAt(shortcutTable.getSelectedRow(), 0)));                    
                    
                    shortcutView.validate();                    
                }
            }
            
        });
        
        shortcutView = new JPanel();
        add(shortcutView, BorderLayout.CENTER);
    }

    public Element createXML(Document doc)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub

    }

    final private JTable shortcutTable;
    private JPanel shortcutView;
}
