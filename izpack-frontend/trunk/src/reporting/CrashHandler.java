package reporting;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
/*
 * Created on Sep 20, 2005
 * 
 * $Id: CrashHandler.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : CrashHandler.java 
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


public class CrashHandler implements UncaughtExceptionHandler
{
    public void uncaughtException(Thread t, Throwable e)
    {   
        System.err.println("Unhandled exception " + e.getClass().getName() + " " + e.getMessage());
        System.err.println("Submitting an error report.");
        System.err.println("This contains no personal information");
        
        System.err.println("Please write a quick message of what you were doing when the exception occurred."); 
        System.err.println("To skip, or sumbit a message, press ENTER.");
        
        String message = "";
        
        try
        {
            message = new BufferedReader(new InputStreamReader(System.in)).readLine();
        }
        catch (IOException e1)
        {
            //Ignore, no sense in causing more errors
        }
        
        if (message == null)
            message = "";
        
        ErrorSubmitter.sendErrorReport(e, message);
        
        System.exit(-1);
    }
}
