package reporting;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/*
 * Created on Oct 5, 2005
 * 
 * $Id: PostTest.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PostTest.java 
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


public class ErrorSubmitter
{
    public static void sendErrorReport(Throwable e, String message)
    {
        try
        {
            HttpURLConnection con = (HttpURLConnection) new URL("http://izpack.berlios.de/process.php").openConnection();
            
            con.setDoOutput(true);
            
            con.setRequestMethod("POST");        
            
            OutputStream os = con.getOutputStream();
            
            
            StringBuffer properties = new StringBuffer();            
            
            Properties props = System.getProperties();
            Set<Object> keys = props.keySet();
            
            os.write("exceptionList=".getBytes());            
                       
            Throwable t = e;
            writeException(t, os);
            
            //Write out any linked exceptions too
            while ( (t = t.getCause()) != null)
                writeException(t, os);
            
            os.write("&threadDump=".getBytes());
            
            Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
            
            Set<Thread> threads = stackTraces.keySet();
            
            for (Thread thread : threads)
            {
                os.write( ("Thread: " + thread.getName() + "\r\n" ).getBytes() );
                StackTraceElement[] stackTrace = stackTraces.get(thread);
                
                for (int i = 0; i < stackTrace.length; i++)
                {
                    os.write( ("\t" + stackTrace[i] + "\r\n").getBytes());
                }
            }
            
            for (Object object : keys)
            {
                properties.append(object + " = " + props.getProperty((String) object) + "\r\n");
            }                   
            
            os.write("&properties=".getBytes());
            os.write(properties.toString().getBytes());
            
            os.write("&message=".getBytes());
            os.write(message.getBytes());
            
            if (con.getResponseCode() == 200)
                System.err.println("Submit succeeded");
            
            con.disconnect();
        }
        catch (Exception e1)
        {
            //Do nothing, we're doing this because of an error :)
            //Not really critical anyway            
        }
    }

    private static void writeException(Throwable e, OutputStream os) throws IOException
    {
        StackTraceElement[] trace = e.getStackTrace();
        
        os.write(( e.toString() + "\r\n" ).getBytes());
        
        for (int i = 0; i < trace.length; i++)
        {
            os.write( ("\t" + trace[i] + "\r\n").getBytes());
        }
    }

}
