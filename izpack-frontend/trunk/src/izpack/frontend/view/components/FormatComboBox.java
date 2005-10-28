/*
 * Created on Apr 18, 2005
 * 
 * $Id: FormatComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FormatComboBox.java 
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
