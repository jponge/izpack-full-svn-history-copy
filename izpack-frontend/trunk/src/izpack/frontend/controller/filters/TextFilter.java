/*
 * Created on Apr 19, 2005
 * 
 * $Id: TextFilter.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : TextFilter.java 
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
package izpack.frontend.controller.filters;

import izpack.frontend.view.IzPackFrame;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Andy Gombos
 */
public class TextFilter extends FileFilter
{

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f)
    {
        String name = f.getName().toLowerCase();        
        
        return f.isDirectory() || 
        	name.endsWith("txt") ||
        	name.endsWith("ini") ||
        	name.endsWith("conf");
    }

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return IzPackFrame.getInstance().langResources().getText("UI.FileFilters.Text.Desc");
    }

}
