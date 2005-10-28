/*
 * Created on Apr 8, 2005
 * 
 * $Id: YesNoRadioPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : YesNoRadioPanel.java 
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

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class YesNoRadioPanel extends JPanel
{
    public YesNoRadioPanel(String defaultSelected)
    {
        yes = new JRadioButton("Yes");
        no = new JRadioButton("No");
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(yes);
        bg.add(no);
        
        FormLayout layout = new FormLayout("pref, 2dlu, pref", "pref");        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);        
        
        CellConstraints cc = new CellConstraints();
        builder.add(yes,	cc.xy(1, 1));
        builder.add(no,		cc.xy(3, 1));
        
        setSelected(defaultSelected);
    }    
    
    public void setSelected(String def)
    {
        if (def.equalsIgnoreCase("yes") || def.equalsIgnoreCase("true"))
            yes.setSelected(true);
        else if (def.equalsIgnoreCase("no") || def.equalsIgnoreCase("false"))
            no.setSelected(true);
    }
    
    public String getYesNo()
    {
        if (yes.isSelected())
            return "yes";        
        
        return "no";
    }
    
    public String getTrueFalse()
    {
        if (yes.isSelected())
            return "true";        
        
        return "false";
    }
    
    public boolean getBoolean()
    {
        return yes.isSelected();
    }
    
    public void setBoolean(boolean yes)
    {
        if (yes)
            setSelected("yes");
        else
            setSelected("no");        
    }
    
    JRadioButton yes, no;    
}
