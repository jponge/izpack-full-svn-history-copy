/*
 * Created on Oct 13, 2005
 * 
 * $Id: CompileManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : CompileManager.java 
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

package izpack.frontend.actions;

import izpack.frontend.controller.GUIController;

import java.io.StringWriter;
import java.lang.Thread.State;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import utils.XML;

import com.izforge.izpack.compiler.CompilerConfig;
import com.izforge.izpack.compiler.CompilerException;
import com.izforge.izpack.compiler.PackagerListener;

import exceptions.DocumentCreationException;

public class CompileManager
{   
    public static void compile(String filename, String[] installArgs, PackagerListener pl)
    {
        try
        {
            compile(XML.createDocument(filename), installArgs, pl);
        }
        catch (DocumentCreationException e)
        {
            //Do nothing, we already displayed an error
        }
    }
    
    public static void compile(Document xmlFile, String[] installArgs, PackagerListener pl)
    {   
        StringWriter stringStream = new StringWriter(2000);
        XML.writeXML(new StreamResult(stringStream), xmlFile);
        
        //TODO Make this configurable
        XML.writeXML(installArgs[2] + ".xml", xmlFile);
     
        currentCompile = new CompileThread(pl, installArgs, stringStream.toString());
        
        currentCompile.start();        
    }
    
    @SuppressWarnings("deprecation")
    public static void stopCompile()
    {
        //Check to see if there is a compiler to stop        
        if (currentCompile != null && currentCompile.getState() == State.RUNNABLE)
        {
            currentCompile.getCompiler().stop();
        }
    }
    
    protected static class CompileThread extends Thread
    {
        public CompileThread(PackagerListener pl, String[] installArgs, String xmlData)
        {
            packagerListener = pl;
            
            try
            {   
                compiler = new CompilerConfig(installArgs[0], installArgs[1], installArgs[2], 
                                pl, xmlData);
                
                compiler.setIzpackHome(GUIController.getInstance().appConfiguration().getIzpackHome());
            }
            catch (CompilerException e)
            {                
                packagerListener.packagerMsg(e.getMessage(), PackagerListener.MSG_ERR);
                
                e.printStackTrace();
            }            
        }
        
        public CompilerConfig getCompiler()
        {
            return compiler;
        }
        
        @Override
        public void run()
        {       
            if (compiler == null)
                return;
            
            try
            {   
                compiler.executeCompiler();
        
                // Waits
                while (compiler.isAlive())
                    Thread.sleep(100);
        
                if (compiler.wasSuccessful())
                    packagerListener.packagerMsg("Successful Compilation");                        
            }
            catch (CompilerException e1)
            {
                packagerListener.packagerMsg(e1.getMessage(), PackagerListener.MSG_ERR);
                
                e1.printStackTrace();
            }
            catch (InterruptedException e)
            {                
            }
            catch (Exception e)
            {
                packagerListener.packagerMsg(e.getMessage(), PackagerListener.MSG_ERR);
                
                e.printStackTrace();
            }
        }
        
        private final PackagerListener packagerListener;
        private CompilerConfig compiler;
    }
    
    private static CompileThread currentCompile;    
}
