/*
 * Created on Apr 29, 2005
 * 
 * $Id: Comparator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Comparator.java 
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

import java.util.HashMap;

/**
 * @author Andy Gombos
 */
public class FileOrderComparator implements java.util.Comparator
{    
    //Creates the class->order mapping
    //Based off the DTD
    public FileOrderComparator() 
    {
        orderMap = new HashMap(9);
        
        int index = 0;
        
        orderMap.put("DirectoryModel", new Integer(index++));
        orderMap.put("FileModel", new Integer(index++));
        orderMap.put("FileSet", new Integer(index++));        
        orderMap.put("Parsable", new Integer(index++));
        orderMap.put("Executable", new Integer(index++));        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1)
    {        
        String name1 = arg0.getClass().getSimpleName();
        String name2 = arg1.getClass().getSimpleName();
        
        Integer pos1 = (Integer) orderMap.get(name1);
        Integer pos2 = (Integer) orderMap.get(name2);
        
        return pos1.compareTo(pos2);
    }

    private static HashMap orderMap;
}
