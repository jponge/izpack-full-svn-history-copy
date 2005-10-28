/*
 * Created on Mar 29, 2005
 * 
 * $Id: IO.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : IO.java 
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
