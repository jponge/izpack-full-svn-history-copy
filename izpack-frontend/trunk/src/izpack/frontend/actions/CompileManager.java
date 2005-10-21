/*
 * Created on Oct 13, 2005
 * 
 * $Id: CompileManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : CompileManager.java 
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

import java.awt.EventQueue;
import java.io.StringWriter;

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
     
        System.out.println("Starting compiler");
        System.out.println("On EVT: " + SwingUtilities.isEventDispatchThread());
        //SwingUtilities.invokeLater(new CompileThread(pl, installArgs, stringStream.toString()));
        new CompileThread(pl, installArgs, stringStream.toString()).start();
        System.out.println("After start");
    }
    
    protected static class CompileThread extends Thread
    {
        public CompileThread(PackagerListener pl, String[] installArgs, String xmlData)
        {
            packagerListener = pl;
            
            try
            {               
                System.out.println("basedir " + installArgs[0]);
                System.out.println("kind " + installArgs[1]);
                System.out.println("output " + installArgs[2]);
                compiler = new CompilerConfig(installArgs[0], installArgs[1], installArgs[2], 
                                pl, xmlData.toString());
                
                System.out.println("On EVT CT: " + SwingUtilities.isEventDispatchThread());
            }
            catch (CompilerException e)
            {                
                packagerListener.packagerMsg(e.getMessage(), PackagerListener.MSG_ERR);
                
                e.printStackTrace();
            }            
        }
        
        @Override
        public void run()
        {       
            if (compiler == null)
                return;
            
            try
            {            
                System.out.println("On EVT CTR: " + SwingUtilities.isEventDispatchThread());
                
                System.out.println(compiler.getCompiler().getProperties().keys());
                
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
}
