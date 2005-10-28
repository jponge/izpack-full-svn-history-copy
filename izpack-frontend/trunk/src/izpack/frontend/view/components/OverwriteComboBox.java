/*
 * Created on Apr 8, 2005
 * 
 * $Id: OverwriteComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : OverwriteComboBox.java 
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
