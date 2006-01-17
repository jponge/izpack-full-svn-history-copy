/*
 * Created on Nov 14, 2005
 * 
 * $Id: ShortcutView.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ShortcutView.java 
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

package izpack.frontend.view.stages.configure.panels.shortcut;

import izpack.frontend.controller.filters.IcoLibFilter;
import izpack.frontend.controller.filters.IconFilter;
import izpack.frontend.model.shortcut.Shortcut;
import izpack.frontend.view.components.JFileChooserIconPreview;
import izpack.frontend.view.win32.IconChooser;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import utils.UI;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

//TODO Add pack-specific creation
public class ShortcutView extends JPanel
{
    /**
     * Constructs the default view for a shortcut model
     * 
     * This is the several fields that are common across all platforms
     */
    public ShortcutView(Shortcut model)
    {        
        FormLayout layout = new FormLayout("pref, 5dlu, 150dlu:grow, 5dlu, pref");
        builder = new DefaultFormBuilder(layout);
        
        this.model = model;
        
        add(builder.getPanel());
        
        os = new JComboBox();
        
        addUIElement("OS", os);
        
        addUIElement("Name", name = new JTextField());
        
        addUIElement("Description", desc = new JTextArea(5, 25));
        desc.setWrapStyleWord(true);
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        
        addUIElement("Target", target = new JTextField(), targetBrowse = new JButton("Browse"));
        
        addUIElement("<html>Command<br>Line", cmdLine = new JTextField());
        
        addUIElement("<html>Working<br>Directory", workDir = new JTextField());
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        
        addUIElement("Icon", icon = new JTextField(), iconBrowse = new JButton("Browse"));
        addUIElement("", iconPreview = new JLabel(iconPreviewer));
        
            
        //Create the panel that has the components specific to each OS    
        specializationPanel = new JPanel();
        specializationPanel.setLayout(specializationPanelLayout = new CardLayout());
        
        specializationPanel.add(createWindowsView(), Shortcut.OS.Windows.toString());
        specializationPanel.add(createUnixView(), Shortcut.OS.Unix.toString());
        
        builder.append(specializationPanel, 5);
        
        configureListeners();
        createBindings();
        
        this.model.setInitialState(Shortcut.INITIAL_STATE.maximized);
    }
    
    private JPanel createWindowsView()
    {        
        FormLayout layout = new FormLayout("pref, 5dlu, 150dlu:grow, 5dlu, pref");
        DefaultFormBuilder winBuilder = new DefaultFormBuilder(layout);
        
        winBuilder.appendRow("20dlu");
        winBuilder.append(DefaultComponentFactory.
                        getInstance().createSeparator("Windows specific options", SwingConstants.CENTER), 5 );
        winBuilder.nextLine();
        winBuilder.appendRow("20dlu");        
        
        winBuilder.append(new JLabel("<html><u>&nbsp;Locations for Shortcuts&nbsp;</u>"), 3);
        winBuilder.nextLine();
        
        addUIElement("Desktop", desktop = new JCheckBox(), winBuilder);
        addUIElement("Program Group", programGroup = new JCheckBox(), winBuilder);
        addUIElement("Applications Menu", applications = new JCheckBox(), winBuilder);
        addUIElement("Start Menu", startMenu = new JCheckBox(), winBuilder);
        addUIElement("Startup", startup = new JCheckBox(), winBuilder);
        
        winBuilder.appendUnrelatedComponentsGapRow();
        winBuilder.nextLine();
        
        addUIElement("Initial Window State", initialState = new JComboBox(Shortcut.INITIAL_STATE.values()), winBuilder);
        
        return winBuilder.getPanel();
    }
    
    private JPanel createUnixView()
    {
        FormLayout layout = new FormLayout("pref, 5dlu, 150dlu:grow, 5dlu, pref");
        DefaultFormBuilder unixBuilder = new DefaultFormBuilder(layout);
        
        
        unixBuilder.appendRow("20dlu");
        unixBuilder.append(DefaultComponentFactory.
                        getInstance().createSeparator("Unix specific options", JSeparator.CENTER), 5 );        
        unixBuilder.nextLine();
        
        unixBuilder.appendUnrelatedComponentsGapRow();
        unixBuilder.nextLine();
        
        addUIElement("Type of shortcut", type = new JComboBox(Shortcut.TYPE.values()), unixBuilder);
        addUIElement("URL", url = new JTextField(), unixBuilder);        
        addUIElement("Show terminal window", terminal = new JCheckBox(), unixBuilder);
        
        return unixBuilder.getPanel();
    }
    
    private void addUIElement(String label, JComponent component)
    {        
        builder.append(label);
        builder.append(component);
        
        builder.nextLine();
    }
    
    private void addUIElement(String label, JComponent component, JButton button)
    {
        builder.append(label);
        builder.append(component);
        builder.append(button);
        
        builder.nextLine();
    }
    
    private void addUIElement(String label, JComponent component, DefaultFormBuilder builder)
    {        
        builder.append(label);
        builder.append(component);
        
        builder.nextLine();
    }
    
    private void addUIElement(String label, JComponent component, JButton button, DefaultFormBuilder builder)
    {
        builder.append(label);
        builder.append(component);
        builder.append(button);
        
        builder.nextLine();
    }        
    
    private void createBindings()
    {     
        BeanAdapter bindings = new BeanAdapter(model);
        
        //Bind the text fields
        Bindings.bind(name, bindings.getValueModel("name"));
        Bindings.bind(target, bindings.getValueModel("target"));
        Bindings.bind(cmdLine, bindings.getValueModel("commandLine"));
        Bindings.bind(workDir, bindings.getValueModel("workingDirectory"));
        Bindings.bind(icon, bindings.getValueModel("iconFile"));
        Bindings.bind(url, bindings.getValueModel("url"));
        
        //Bind the text area
        Bindings.bind(desc, bindings.getValueModel("description"));
        
        //Bind the checkboxes        
        Bindings.bind(desktop, bindings.getValueModel("desktop"));
        Bindings.bind(programGroup, bindings.getValueModel("programGroup"));
        Bindings.bind(applications, bindings.getValueModel("applications"));
        Bindings.bind(startMenu, bindings.getValueModel("startMenu"));
        Bindings.bind(startup, bindings.getValueModel("startup"));
        Bindings.bind(terminal, bindings.getValueModel("terminal"));
        
        Bindings.bind(os, new SelectionInList(Shortcut.OS.values()));        
        
        //Configure bindings for combo boxes
        //Get a changing selection holder, and bind that to the model
        Bindings.bind(os, osSelection = new SelectionInList(Shortcut.OS.values()));
        PropertyConnector.connect(osSelection.getSelectionHolder(), "value", model, "modelledOS");
        
        Bindings.bind(initialState, stateSelection = new SelectionInList(Shortcut.INITIAL_STATE.values()));
        PropertyConnector.connect(stateSelection.getSelectionHolder(), "value", model, "initialState");
        
        Bindings.bind(type, typeSelection = new SelectionInList(Shortcut.TYPE.values()));
        PropertyConnector.connect(typeSelection.getSelectionHolder(), "value", model, "type");
        
        model.propogateChanges();
    }
    
    private void configureListeners()
    {
        //TODO make sure if icon is a file, is included in installer files
        iconBrowse.addActionListener(new ActionListener()
        {
            // TODO Make code not a copy-and-paste from the javalobby article
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();     
                FileFilter iconFilter = new IcoLibFilter();
                
                JFileChooserIconPreview previewPane = new JFileChooserIconPreview();
                chooser.setAccessory(previewPane);                
                chooser.addPropertyChangeListener(previewPane);
                
                chooser.addChoosableFileFilter(iconFilter);
                chooser.addChoosableFileFilter(new IconFilter());
                
                //Set the selected file
                if (icon.getText() != "")
                {
                    chooser.setSelectedFile(new File(icon.getText()));
                }
                    
                if (chooser.showDialog(UI.getApplicationFrame(), "Load icon") == JFileChooser.APPROVE_OPTION)
                {
                    //If this is an icon inside a Windows resource, display the chooser
                    if ( iconFilter.accept(chooser.getSelectedFile()) )
                    {
                        IconChooser icoChooser = new IconChooser(chooser.getSelectedFile()
                                        .getAbsolutePath());
                        icoChooser.setVisible(true);
                        
                        int iconIndex = icoChooser.getSelectedIconIndex();
                        
                        
                        //-1 means cancel was pressed
                        if (iconIndex != -1)
                        {
                            model.setIconIndex(iconIndex);
                        }
                        
                        iconPreviewer = new ImageIcon(icoChooser.getIconImage());                        
                    }
                    else
                    {
                        //Just a normal image
                        iconPreviewer = new ImageIcon(chooser.getSelectedFile()
                                    .getAbsolutePath());
                    }
                    
                    //Add the icon to the preview
                    iconPreview.setIcon(iconPreviewer);
                    
                    icon.setText(chooser.getSelectedFile().getAbsolutePath());

                    validate();
                }
            }
        });

        os.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                specializationPanelLayout.show(specializationPanel, os
                                .getSelectedItem().toString());
            }

        });
    }
    
    // Common UI elements
    JComboBox os, initialState, type;
    JTextField name, target, cmdLine, workDir, icon, url;
    JTextArea desc;
    JCheckBox desktop, programGroup, applications, startMenu, startup, terminal;
    
    JButton iconBrowse, targetBrowse;
    
    JLabel iconPreview;
    ImageIcon iconPreviewer;    
    
    private Shortcut model;
    private DefaultFormBuilder builder;
    private JPanel specializationPanel;
    private CardLayout specializationPanelLayout;
    private SelectionInList stateSelection;
    private SelectionInList typeSelection;
    private SelectionInList osSelection;
}
