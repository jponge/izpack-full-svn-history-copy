/*
 * Created on Mar 23, 2005
 * 
 * $Id: XInfo.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : XInfo.java 
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.IO;
import utils.UI;
import utils.XML;

import com.jgoodies.validation.ValidationResult;

/**
 * Panel to set text parsed by the variable substitutor. See XInfo documentation for more information.
 * 
 * @author Andy Gombos
 */
public class XInfo extends FileEdit
{    
    public XInfo()
    {
        super();
        
        JScrollPane scrollPane = new JScrollPane();
        editor = new JTextArea();
        
        scrollPane.getViewport().add(editor);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        editor.setWrapStyleWord(true);
        
        setTextEditor(scrollPane);
    }
    
    private JTextArea editor;
    private String filename;

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#createXML()
     * 
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     */
    public Element[] createXML(Document doc)
    {   
        return new Element[]{XML.createResourceTree("XInfoPanel.info", filename, doc)};
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     * 
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     */
    public void initFromXML(Document document)
    {
        String filename = XML.getResourceValueAsPath(document, "XInfoPanel.info");
        
        //Check to see if there's anything to load from
        if (filename != null)
        {
            updateEditorDisplay(filename);
        }
    }

    @Override
    protected void updateEditorDisplay(String filename)
    {        
        editor.setText(IO.loadFileIntoString(new File(filename)));
        
        this.filename = filename;       
    }
}
