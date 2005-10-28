/*
 * Created on Apr 1, 2005
 * 
 * $Id: YesNoCheckBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : YesNoCheckBox.java 
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

import javax.swing.JCheckBox;

/**
 * Standard JCheckBox, but provides a simple way to get a "yes" or "no" state value
 * 
 * @author Andy Gombos
 */
public class YesNoCheckBox extends JCheckBox
{    
    
    public YesNoCheckBox()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public YesNoCheckBox(String text)
    {
        super(text);
        // TODO Auto-generated constructor stub
    }
    
    public YesNoCheckBox(String text, boolean selected)
    {
        super(text, selected);
        // TODO Auto-generated constructor stub
    }
    
    public String getState()
    {        
        return isSelected() ? "yes" : "no";
    }
}
