/*
 * Created on Jan 24, 2006
 * 
 * $Id: JDKPath.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : JDKPath.java 
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

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Simple panel for JDK finding - set the minimum versions and stuff
 * @author Andy Gombos
 */
public class JDKPath extends JPanel implements ConfigurePanel
{
    public JDKPath()
    {
        FormLayout layout = new FormLayout("pref, 5dlu, pref, 5dlu, pref", "pref, 5dlu, pref, 10dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        
        CellConstraints cc = new CellConstraints();
        
        builder.add(new JLabel("Minimum JDK Version:"), cc.xy(1, 1));
        builder.add(minVer = new JTextField(6),         cc.xyw(3, 1, 3));        
        
        builder.add(new JLabel("Maximum JDK Version:"), cc.xy(1, 3));
        builder.add(maxVer = new JTextField(6),         cc.xyw(3, 3, 3));                
        
        builder.add(new JLabel("Display panel if a JDK exists"), cc.xy(1, 5));
        builder.add(yes = new JRadioButton("Yes"),               cc.xy(3, 5));
        builder.add(no = new JRadioButton("No"),                 cc.xy(5, 5));
        builder.nextLine();
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(yes);
        bg.add(no);
    }
    
    public Element[] createXML(Document doc)
    {
        Element root = XML.createElement("variables", doc);
        
        //Skip if JDK valid
        Element skipIfValid = XML.createElement("variable", doc);
        skipIfValid.setAttribute("name", "JDKPathPanel.skipIfValid");
        if (yes.isSelected())
            skipIfValid.setAttribute("value", "yes");
        else
            skipIfValid.setAttribute("value", "no");
        
        root.appendChild(skipIfValid);
        
        //Min version
        Element minVerElem = XML.createElement("variable", doc);
        minVerElem.setAttribute("name", "JDKPathPanel.minVersion");
        minVerElem.setAttribute("value", minVer.getText());
        root.appendChild(minVerElem);
        
        //Max version
        Element maxVerElem = XML.createElement("variable", doc);
        maxVerElem.setAttribute("name", "JDKPathPanel.maxVersion");
        maxVerElem.setAttribute("value", maxVer.getText());
        root.appendChild(maxVerElem);
        
        return new Element[]{root};
    }

    public void initFromXML(Document xmlFile)
    {
        //Load variables. Some may be null
        String minVerStr = XML.getVariable(xmlFile, "JDKPathPanel.minVersion");
        String maxVerStr = XML.getVariable(xmlFile, "JDKPathPanel.maxVersion");
        String skip      = XML.getVariable(xmlFile, "JDKPathPanel.skipIfValid");

        if (minVerStr != null)
            minVer.setText(minVerStr);
        
        if (maxVerStr != null)
            maxVer.setText(maxVerStr);        
        
        if (skip != null)
        {
            skip = skip.toLowerCase();
            
            //Skip
            if (skip.equals("yes") || skip.equals("true"))
                yes.setSelected(true);
            //Don't skip            
            else
                no.setSelected(true);                
        }
    }
    
    JTextField minVer, maxVer;
    JRadioButton yes, no;
    //TODO I don't know if this needs to be internationalized
    JLabel dotLabel = new JLabel(".");
}
