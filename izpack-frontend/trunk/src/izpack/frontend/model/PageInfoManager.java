/*
 * Created on Jun 3, 2004
 * 
 * $Id: PageInfoManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PageInfoManager.java 
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

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Andy Gombos
 * 
 * Provides a common class to query about <code>Page</code>s.  
 * 
 * Reads the XML data files which specify data for each panel, as well as the master file
 * which lists panels.  
 */

public class PageInfoManager
{
    
    private String[] getConfigFiles()
    {
        return new File(CONFIG_PATH).list(new FilenameFilter() 
	        {
	            public boolean accept(File dir, String name)
	            {
	                return name.endsWith("xml");
	            }            
	        });
    }
    
    private static final String CONFIG_PATH = "/conf/pages/";
}
