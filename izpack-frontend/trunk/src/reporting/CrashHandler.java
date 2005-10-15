package reporting;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Set;
/*
 * Created on Sep 20, 2005
 * 
 * $Id: CrashHandler.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : CrashHandler.java 
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

public class CrashHandler implements UncaughtExceptionHandler
{
    public void uncaughtException(Thread t, Throwable e)
    {   
        System.err.println("Unhandled exception");
        System.err.println("Submitting an error report.");
        System.err.println("This contains no personal information");
        ErrorSubmitter.sendErrorReport(e);
        
        System.exit(-1);
    }
}
