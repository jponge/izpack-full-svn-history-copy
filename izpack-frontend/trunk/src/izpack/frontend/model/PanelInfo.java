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
 * Countains all of the configuration data/attributes of a <code>Panel</code>
 */

public class PanelInfo implements Comparable
{
    public PanelInfo(String classname, String editorClassname, String name, String shortDesc, String longDesc,
                    Author[] authors, Resource[] resources)
    {
        this.classname = classname;
        this.editorClassname = editorClassname;
        this.name = name;
        this.authors = authors;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.resources = resources;
        
        if (authors.length == 0)
            authors = null;
        
        if (resources.length == 0)
            resources = null;
    }
    
    public String toString()
    {
        return name;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0)
    {        
        if (arg0 instanceof PanelInfo)
        {
            PanelInfo pi = (PanelInfo) arg0;
            return name.compareTo(pi.name);
        }
        
        return 0;
    }
    
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
    
    public String getName()
    {
        return name;
    }
    
    public String getClassname()
    {
        return classname;
    }
    
    /** Name of the IzPack class represented */
    private String classname;
    
    /** Name of the class that will visually edit this panel */
    private String editorClassname;
    
    /** Name of the panel */
    private String name;
    
    /** Short (few words) description */
    private String shortDesc;
    
    /** A longer, more detailed description of the panel's functions */
    private String longDesc;
    
    /** The author of the panel */
    private Author[] authors;
    
    /** The resources the panel accepts/understands */    
    private Resource[] resources;
    
    /** Represents a resource that the panel understands */
    public static class Resource
    {
        /** Name of the resource (what the panel calls it) */
        private String id;
        
        /** Is this resource required for the panel to function? */
        private boolean required;
        
        /** Type of the resource (for automatic type checking) */
        private int type;
        
        /* Resource type definitions */
        public static final int TXT = 0;
        public static final int IMG = 1;
        public static final int XML = 2;
        public static final int HTML = 3;
        public static final int UNKNOWN = 4;
        
        public Resource(String id, boolean required, int type)
        {
            this.id = id;
            this.required = required;
            this.type = type;
        }
        
        public String getID()
        {
            return id;
        }
        public boolean isRequired()
        {
            return required;
        }
        public int getType()
        {
            return type;
        }
        public void setID(String id)
        {
            this.id = id;
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

    public String getEditorClassname()
    {
        return editorClassname;
    }

    public void setEditorClassname(String editorClassname)
    {
        this.editorClassname = editorClassname;
    }
}
