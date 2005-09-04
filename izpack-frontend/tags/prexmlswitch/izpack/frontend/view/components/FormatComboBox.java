/*
 * Created on Apr 18, 2005
 * 
 * $Id: FormatComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FormatComboBox.java 
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

import java.util.TreeMap;

import javax.swing.JComboBox;

/**
 * @author Andy Gombos
 */
public class FormatComboBox extends JComboBox
{
    public FormatComboBox()
    {       
        super(formatList);
        
        initFormatMap();        
        
        setEditable(false);        
    }
    
    public String getFormat()
    {        
        String format = (String) getSelectedItem();
        if (format == null || format.equals(""))
            return "";
        
        return (String) formatMap.get(format);
    }
    
    public void setFormat(String format)
    {
        if (format == null || format.equals(""))
            setSelectedIndex(-1);
        
        setSelectedItem(formatMap.get(format));
    }
    
    private void initFormatMap()
    {
        //If we've already initialized the map
        if (formatMap.size() != 0)
            return;
        
        //  Box->XML        
        formatMap.put(formatList[0], "plain");
        formatMap.put(formatList[1], "javaprop");
        formatMap.put(formatList[2], "xml");
        formatMap.put(formatList[3], "shell");
        
        //XML->Box
        formatMap.put("plain", formatList[0]);
        formatMap.put("javaprop", formatList[1]);
        formatMap.put("xml", formatList[2]);
        formatMap.put("shell", formatList[3]);
    }
    
    //From the IzPack DTD
    static final String[] formatList = new String[]{
                    "Plain",
                    "Java Properties",
                    "XML",
                    "Shell Script",
                    };
    
    static final TreeMap formatMap = new TreeMap();    
}
