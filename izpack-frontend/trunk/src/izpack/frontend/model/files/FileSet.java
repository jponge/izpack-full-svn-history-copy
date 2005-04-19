/*
 * Created on Apr 12, 2005
 * 
 * $Id: FileSet.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileSet.java 
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
package izpack.frontend.model.files;

import java.util.ArrayList;

import org.w3c.dom.Document;

/**
 * @author Andy Gombos
 */
public class FileSet extends PackFileModel
{
    public boolean caseSensitive = true, defaultExcludes = true;
    private ArrayList files;

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.PackElement#writeXML()
     */
    public Document writeXML()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void addSet(String set, boolean include)
    {
        if (include)
            files.add(new Include(set));
        else
            files.add(new Exclude(set));
    }
    
    class Include
    {
        public Include(String set)
        {
            this.set = set;
        }
        
        public String set;
        
        public Document writeXML()
        {
            return null;
        }
    }
    
    class Exclude
    {
        public Exclude(String set)
        {
            this.set = set;
        }
        
        public String set;
        
        public Document writeXML()
        {
            return null;
        }
    }
}
