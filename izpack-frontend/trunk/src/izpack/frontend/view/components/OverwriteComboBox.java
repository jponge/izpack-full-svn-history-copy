/*
 * Created on Apr 8, 2005
 * 
 * $Id: OverwriteComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : OverwriteComboBox.java 
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

import javax.swing.JComboBox;

/**
 * @author Andy Gombos
 */
public class OverwriteComboBox extends JComboBox
{    
    public OverwriteComboBox()
    {
        super(overwriteList);
        
        setEditable(false);        
    }
    
    public String getOverwriteOption()
    {
        if (getSelectedIndex() == -1)
            return "";
        
        return xmlValues[getSelectedIndex()];
    }
    
    public void setOverwriteOption(String overwrite)
    {
        if (overwrite == null || overwrite.equals(""))
            setSelectedIndex(-1);
            
        for (int i = 0; i < xmlValues.length; i++)
        {
            if (overwrite.equals(xmlValues[i]))
            {
                setSelectedIndex(i);
                return;
            }
        }
    }
    
    
    /*      
     * User friendly choice names
     */    
    static final String[] overwriteList = new String[]{
                    "Overwrite",
                    "Keep existing file",
                    "Ask, default overwrite",
                    "Ask, default keep existing",
                    "Update if newer"
                    };
    
    /*
     * From the IzPack DTD
     * Values that go into the installation file
     * Index corresponds to text choice above
     */
    static final String[] xmlValues = new String[]{
                    "true",
                    "false",
                    "asktrue",
                    "askfalse",
                    "update"
    				};                           
}
