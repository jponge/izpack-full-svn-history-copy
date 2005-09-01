/*
 * Created on Jun 29, 2004
 * 
 * $Id: Author.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Author.java 
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

/** Represents an author of a Page */
public class Author implements Comparable
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

    public String toString()
    {
       return name + "(" + email + ")";       
    }
    
    /** Sort Author objects by the author's last name **/
    
    public int compareTo(Object o)
    {
        if (o instanceof Author)
        {
            String lastName = ((Author) o).name.split(" ")[1];
            String lastName2 = name.split(" ")[1];         

            return lastName2.compareTo(lastName);
        }
        else
        {
            throw new ClassCastException("Comparative object is not of type Author");
        }
    }
}