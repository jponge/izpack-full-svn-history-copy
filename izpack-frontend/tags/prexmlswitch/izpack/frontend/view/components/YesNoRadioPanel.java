/*
 * Created on Apr 8, 2005
 * 
 * $Id: YesNoRadioPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : YesNoRadioPanel.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
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
