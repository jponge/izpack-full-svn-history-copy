/*
 * Created on Jun 29, 2004
 * 
 * $Id: Author.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Author.java 
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