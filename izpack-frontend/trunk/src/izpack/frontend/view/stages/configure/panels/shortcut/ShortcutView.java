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

import izpack.frontend.model.shortcut.Shortcut;
import izpack.frontend.view.components.JFileChooserIconPreview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.UI;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.FormLayout;

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
        
        os = new JComboBox(model.getSupportedOSes());
        
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
        addUIElement("", iconPreview = new JLabel("Preview", iconPreviewer, JLabel.LEADING));
            iconPreview.setHorizontalTextPosition(JLabel.LEADING);
            
        appendUnixView();
        
        configureListeners();
    }
    
    private void appendWindowsView()
    {        
        builder.appendSeparator("Locations for Shortcuts");
        
        addUIElement("Desktop", desktop = new JCheckBox());
        addUIElement("Program Group", programGroup = new JCheckBox());
        addUIElement("Applications Menu", applications = new JCheckBox());
        addUIElement("Start Menu", startMenu = new JCheckBox());
        addUIElement("Startup", startup = new JCheckBox());
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        
        addUIElement("Initial Window State", initialState = new JComboBox(model.getInitialStates()));
    }
    
    private void appendUnixView()
    {
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        
        addUIElement("Type of shortcut", type = new JComboBox(model.getShortcutTypes()));
        addUIElement("URL", url = new JTextField());
        addUIElement("Show terminal window", terminal = new JCheckBox());
    }
    
    private void removeUnixView()
    {
        System.out.println("Removing");
        remove(type);
        remove(url);
        remove(terminal);
        
        appendWindowsView();
        
        invalidate();
        UI.getApplicationFrame().pack();
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
    
    private void createBindings()
    {     
        
    }
    
    private void configureListeners()
    {
        iconBrowse.addActionListener(new ActionListener()
                        {
                            //TODO Make code not a copy-and-paste from the javalobby article
                            public void actionPerformed(ActionEvent e)
                            {
                                JFileChooser chooser = new JFileChooser();
                                JFileChooserIconPreview previewPane = new JFileChooserIconPreview();
                                chooser.setAccessory(previewPane);
                                chooser.addPropertyChangeListener(previewPane);
                                if (chooser.showDialog(UI.getApplicationFrame(), "Load") == JFileChooser.APPROVE_OPTION)
                                {
                                    iconPreviewer = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
                                    icon.setText(chooser.getSelectedFile().getAbsolutePath());
                                    
                                    iconPreview.setIcon(iconPreviewer);
                                    
                                    validate();
                                }
                            }            
                        });
        
        os.addActionListener(new ActionListener()
                        {

                            public void actionPerformed(ActionEvent e)
                            {
                                if (!os.getSelectedItem().toString().equals("Unix"));
                                    removeUnixView();                                
                            }
            
                        }
        );
    }
    
    //Common UI elements
    JComboBox os, initialState, type;
    JTextField name, target, cmdLine, workDir, icon, url;
    JTextArea desc;
    JCheckBox desktop, programGroup, applications, startMenu, startup, terminal;
    
    JButton iconBrowse, targetBrowse;
    
    JLabel iconPreview;
    ImageIcon iconPreviewer;
    
    
    private Shortcut model;
    private DefaultFormBuilder builder;
}
