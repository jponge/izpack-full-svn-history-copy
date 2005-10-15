/*
 * Created on Nov 18, 2004
 * 
 * $Id: PageSelectList.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PageSelectList.java 
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

import izpack.frontend.model.SelectListModel;

import javax.swing.JList;

/**
 * @author Andy Gombos
 */
public class SelectList extends JList
{    
    public SelectList()
    {
        super();
        
        setModel(new SelectListModel());
    }    
        
    public int getNumElements()
    {   
        return getModel().getSize();
    }   
}
