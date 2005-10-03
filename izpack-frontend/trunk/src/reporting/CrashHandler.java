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
        e.getCause().printStackTrace();
        Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
        
        Set<Thread> threads = stackTraces.keySet();
        
        System.out.println("Listing all thread traces:");
        
        for (Thread thread : threads)
        {
            System.out.println("Thread: " + thread.getName());
            StackTraceElement[] stackTrace = stackTraces.get(thread);
            
            for (int i = 0; i < stackTrace.length; i++)
            {
                System.out.println("\t" + stackTrace[i]);
            }
        }
        
        System.out.println("Listed");
    }
}
