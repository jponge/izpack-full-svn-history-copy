/*
 * Created on May 25, 2005
 * 
 * $Id: SelectListModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : SelectListModel.java 
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
