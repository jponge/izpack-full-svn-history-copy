/*
 * Created on Jan 10, 2006
 * 
 * $Id: Info.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Info.java 
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
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.IO;

/** Implement the license panel.  This allows the user to select or create
 * a README type file - plain text that is
 * 
 * @author Andy Gombos
 */
public class License extends LicenseEdit
{
    public License()
    {
        super(LicenseEdit.TEXT);
        
        JScrollPane scrollPane = new JScrollPane();
        editor = new JTextArea();
        
        scrollPane.getViewport().add(editor);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        editor.setWrapStyleWord(true);
        
        setTextEditor(scrollPane);
    }
    
    @Override
    protected void updateEditorDisplay(String filename)
    {
        editor.setText(IO.loadFileIntoString(new File(filename)));
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
    
    private JTextArea editor;

}
