/*
 * Created on Apr 29, 2005
 * 
 * $Id: Comparator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Comparator.java 
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
