/*
 * Created on Jan 12, 2006
 * 
 * $Id: HTMLEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : HTMLEditor.java 
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

package izpack.frontend.view.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import utils.UI;
import de.xeinfach.kafenio.KafenioPanel;
import de.xeinfach.kafenio.KafenioPanelConfiguration;
import de.xeinfach.kafenio.component.ExtendedHTMLDocument;
import de.xeinfach.kafenio.component.ExtendedHTMLEditorKit;

/**
 * Provide a tabbed 
 * @author Andy Gombos
 */
public class HTMLEditor extends JPanel implements ChangeListener
{
    public HTMLEditor()
    {
        setLayout(new BorderLayout());        
        
        //TODO move to another method
        KafenioPanelConfiguration conf = new KafenioPanelConfiguration();        
                
        conf.setCustomToolBar1("CUT,COPY,PASTE,SEPARATOR,BOLD"
        + ",ITALIC,UNDERLINE,SEPARATOR,LEFT,CENTER,RIGHT,JUSTIFY");        

        editor = new KafenioPanel(conf);
        editor.setKafenioParent(UI.getApplicationFrame());
        
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new GridLayout(2, 1));
        toolbarPanel.add(editor.getJToolBar1());
        toolbarPanel.add(editor.getJToolBar2());
        
        display = new JPanel();
        tabs = new JTabbedPane();
        
        display.add(editor);
        
        tabs.addChangeListener(this);
        tabs.setTabPlacement(JTabbedPane.BOTTOM);

        tabs.addTab("WYSIWYG", editor.getHTMLScrollPane());
        tabs.addTab("Source", editor.getSrcScrollPane());
        
        add(toolbarPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.SOUTH);
        add(display, BorderLayout.CENTER);
        
        try
        {
            loadDocument("F:/program files/izpack/src/dist-files/install-readme.html");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (BadLocationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stateChanged(ChangeEvent e)
    {        
        //WYSIWYG tab is displayed        
        if(tabs.getSelectedIndex() == 0)
        {
            //Copy source to WYSIWYG            
            editor.getTextPane().setText(editor.getSourcePane().getText());
            
            //Enable the toolbars
            JToolBar toolbar1 = editor.getJToolBar1();
            for (int i = 0; i < toolbar1.getComponentCount(); i++)
                toolbar1.getComponent(i).setEnabled(true);
            
            JToolBar toolbar2 = editor.getJToolBar2();
            for (int i = 0; i < toolbar2.getComponentCount(); i++)
                toolbar2.getComponent(i).setEnabled(true);
        }
        else
        {
            //Copy WYSIWYG to source
            editor.getSourcePane().setText(editor.getTextPane().getText());
            
            //Disable the toolbars
            JToolBar toolbar1 = editor.getJToolBar1();
            for (int i = 0; i < toolbar1.getComponentCount(); i++)
                toolbar1.getComponent(i).setEnabled(false);
            
            JToolBar toolbar2 = editor.getJToolBar2();
            for (int i = 0; i < toolbar2.getComponentCount(); i++)
                toolbar2.getComponent(i).setEnabled(false);
        }        
    }
    
    public void loadDocument(String file) throws IOException, BadLocationException
    {        
        ExtendedHTMLEditorKit htmlKit = editor.getExtendedHtmlKit();
        ExtendedHTMLDocument htmlDoc = (ExtendedHTMLDocument)(htmlKit.createDefaultDocument());
                    
        InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(file)));       
            
        htmlKit.read(isr, htmlDoc, 0);
        isr.close();
        
        editor.registerDocument(htmlDoc);
        editor.refreshOnUpdate();
    }
    
    private KafenioPanel editor;
    private JPanel display;
    private JTabbedPane tabs;
}
