/*
 * Created on Mar 29, 2005
 * 
 * $Id: IO.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : IO.java 
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
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Provide convenience IO methods as needed
 * 
 * @author Andy Gombos
 */
public class IO
{
    /**
     * Loads a file into memory that can then be put into a text area, processed, etc.
     * 
     * @param f The file to load
     * @return The file contents, or "" if an error occurred
     */
    public static String loadFileIntoString(File f)
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            StringBuffer buf = new StringBuffer(1024);	//Sized for 1K of data
            String data = "";
                        
            while (data != null)
            {
                data = in.readLine();
                
                if (data != null)
                    buf.append( data + System.getProperty("line.separator") );               
            }
            
            return buf.toString();
        }
        catch (FileNotFoundException fnfe)
        {
            UI.showError(fnfe.getLocalizedMessage(), "Error! File not found");            
        }
        catch (IOException ioe)
        {
            UI.showError(ioe.getLocalizedMessage(), "Error reading file");            
        }
        
        return "";
    }
}
