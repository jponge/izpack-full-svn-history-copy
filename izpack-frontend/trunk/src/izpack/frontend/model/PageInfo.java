/*
 * Created on Jun 3, 2004
 * 
 * $Id: PageInfo.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PageInfo.java 
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

/**
 * @author Andy Gombos
 * 
 * This could be an anonymous class, but I think that could get unwieldly.
 * Countains all of the configuration data/attributes of a <code>Page</code>
 */

public class PageInfo
{
    public Author[] getAuthors()
    {
        return authors;
    }
    public String getLongDesc()
    {
        return longDesc;
    }
    public Resource[] getResources()
    {
        return resources;
    }
    public String getShortDesc()
    {
        return shortDesc;
    }
    
    /** Short (few words) description */
    private String shortDesc;
    
    /** A longer, more detailed description of the panel's functions */
    private String longDesc;
    
    /** The author of the panel */
    private Author[] authors;
    
    /** The resources the panel accepts/understands */    
    private Resource[] resources;
    
    /** Represents an author of a Page */
    public class Author
    {        
        private String name;
        private String email;
        
        public Author(String name, String email)
        {
            this.name = name;
            this.email = email;
        }
        
        public String getEmail()
        {
            return email;
        }
        public String getName()
        {
            return name;
        }
        public void setEmail(String email)
        {
            this.email = email;
        }
        public void setName(String name)
        {
            this.name = name;
        }
    }
    
    /** Represents a resource that the panel understands */
    public class Resource
    {
        /** Name of the resource (what the panel calls it) */
        private String name;
        
        /** Is this resource required for the panel to function? */
        private boolean required;
        
        /** Type of the resource (for automatic type checking) */
        private int type;
        
        /* Resource type definitions */
        private static final int TXT = 0;
        private static final int IMG = 1;
        private static final int XML = 2;
        private static final int HTML = 3;
        
        public Resource(String name, boolean required, int type)
        {
            this.name = name;
            this.required = required;
            this.type = type;
        }
        
        public String getName()
        {
            return name;
        }
        public boolean isRequired()
        {
            return required;
        }
        public int getType()
        {
            return type;
        }
        public void setName(String name)
        {
            this.name = name;
        }
        public void setRequired(boolean required)
        {
            this.required = required;
        }
        public void setType(int type)
        {
            this.type = type;
        }
    }
}
