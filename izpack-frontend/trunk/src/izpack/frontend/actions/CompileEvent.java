/*
 * Created on Oct 19, 2005
 * 
 * $Id: CompileEvent.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : CompileEvent.java 
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
package izpack.frontend.actions;

import java.awt.Event;

public class CompileEvent
{
    public CompileEvent (String installType, String baseDir, String outputFile)
    {
        this.installType = installType;
        this.baseDir = baseDir;
        this.outputFile = outputFile;
    }   
    
    private String installType, baseDir, outputFile;

    public String getBaseDir()
    {
        return baseDir;
    }

    public String getInstallType()
    {
        return installType;
    }

    public String getOutputFile()
    {
        return outputFile;
    }

    public void setBaseDir(String baseDir)
    {
        this.baseDir = baseDir;
    }

    public void setInstallType(String installType)
    {
        this.installType = installType;
    }

    public void setOutputFile(String outputFile)
    {
        this.outputFile = outputFile;
    }
}
