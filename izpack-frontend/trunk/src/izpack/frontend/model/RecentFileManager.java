/*
 * Created on Jul 30, 2004
 * 
 * $Id: RecentFileManager.java Feb 8, 2004 izpack-frontend Copyright (C)
 * 2001-2003 IzPack Development Group
 * 
 * File : RecentFileManager.java Description : TODO Add description Author's
 * email : gumbo@users.berlios.de
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
import java.util.Iterator;
import java.util.List;
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
 */
public class RecentFileManager
{
    public static RecentFileManager getInstance()
    {
        if (instance == null)
        {
            instance = new RecentFileManager();
            files = new ArrayList();
        }
        return instance;
    }
    
    public ArrayList loadRecentList()
    {
        if (files.size() != 0)
            return files;
        
        try
        {
            IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
            IXMLReader reader = StdXMLReader.fileReader("conf/recent.xml");
            parser.setReader(reader);
            XMLElement xml = (XMLElement) parser.parse();

            //Load the authors array
            Vector fileElems = xml.getChildrenNamed("file");            
            for (Iterator iter = fileElems.iterator(); iter.hasNext();)
            {
                XMLElement element = (XMLElement) iter.next();                
                files.add(element.getAttribute("path"));
            }            

            return files;
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
    
    public void addUsedFile(String file)
    {
        files.add(0, file);
    }
    
    public void saveRecentFiles() throws IOException
    {
        int length = files.size() < 5 ? files.size() : 5;
        List choppedFiles = files.subList(0, length);
        XMLWriter out = new XMLWriter(new FileOutputStream("conf/recent.xml"));
        XMLElement head = new XMLElement("recent");
        for (Iterator iter = choppedFiles.iterator(); iter.hasNext();)
        {
            String element = (String) iter.next();
            XMLElement xelement = new XMLElement("file");            
            xelement.setAttribute("path", element);            
            head.addChild(xelement);
        }
        out.write(head);
    }
    
    private static ArrayList files;
    private static RecentFileManager instance;
}