/*
 * Created on May 25, 2005
 * 
 * $Id: SelectListModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : SelectListModel.java 
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

package izpack.frontend.model;

import javax.swing.DefaultListModel;

/**
 * @author Andy Gombos
 */
public class SelectListModel extends DefaultListModel
{
    public Object getElementAt(int index)
    {   
        return get(index);
    }
    
    public void addElementAt(int index, Object element)
    {
        add(index, element);        
    }
    
    public void removeElements(int indicies[])
    {
        for (int i = 0; i < indicies.length; i++)
            remove(indicies[i]);
    }
    
    public void moveElement(int startingIndex, int delta)
    {        
        Object original = get(startingIndex);
        Object replaced = get(startingIndex + delta);
        set(startingIndex + delta, original);
        set(startingIndex, replaced);
    }
}
