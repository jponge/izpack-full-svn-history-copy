/*
 * Created on Jun 29, 2004
 * 
 * $Id: AuthorManager.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : AuthorManager.java Description : TODO Add description Author's email :
 * gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;

/**
 * @author Andy Gombos
 * 
 * Manage the persistent list of authors who have been used in the installer 
 */
public class AuthorManager
{
    public static ArrayList loadAuthors()
    {        
        try
        {
            IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
            IXMLReader reader = StdXMLReader.fileReader("conf/authors.xml");
            parser.setReader(reader);
            XMLElement xml = (XMLElement) parser.parse();

            //Load the authors array            
            Vector authorsElem = xml.getChildrenNamed("author");
            ArrayList authors = new ArrayList();
            for (Iterator iter = authorsElem.iterator(); iter.hasNext();)
            {
                XMLElement element = (XMLElement) iter.next();
                String aname = element.getAttribute("name");
                String email = element.getAttribute("email");
                Author author = new Author(aname, email);
                authors.add(author);
            }

            Collections.sort(authors);
            
            return authors;
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (XMLException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeAuthors(ArrayList authors) throws IOException
    {
        XMLWriter out = new XMLWriter(new FileOutputStream("conf/authors.xml"));
        XMLElement head = new XMLElement("authors");
        for (Iterator iter = authors.iterator(); iter.hasNext();)
        {
            Author element = (Author) iter.next();
            XMLElement xelement = new XMLElement("author");            
            xelement.setAttribute("email", element.getEmail());
            xelement.setAttribute("name", element.getName());
            head.addChild(xelement);
        }
        out.write(head);        
    }
}

