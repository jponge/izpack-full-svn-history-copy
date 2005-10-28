/*
 * Created on Apr 7, 2005
 * 
 * $Id: OSComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : OSComboBox.java 
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
public class OSComboBox extends JComboBox
{
    public OSComboBox()
    {       
        super(osList);
        
        setEditable(false);        
    }
    
    public String getOS()
    {
        if (getSelectedIndex() == -1)
            return "";
        
        return (String) getSelectedItem();
    }
    
    public void setOS(String os)
    {
        if (os == null || os.equals(""))
            setSelectedIndex(-1);
        
        setSelectedItem(os);
    }
    
    //From the IzPack DTD
    static final String[] osList = new String[]{
                    "Windows",
                    "Unix",
                    "Mac",
                    };
}
