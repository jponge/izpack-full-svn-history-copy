/*
 * Created on Oct 11, 2005
 * 
 * $Id: Compile.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Compile.java 
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.izforge.izpack.compiler.CompilerConfig;
import com.izforge.izpack.compiler.CompilerException;
import com.izforge.izpack.compiler.PackagerListener;

public class Compile extends AbstractAction
{
    public Compile()
    {
        super("Compile", null);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        //Calls the compiler

        CompilerConfig compiler;
        try
        {
            compiler = new CompilerConfig(
                            "H:/izpack-src/_dist/sample/install2.xml",
                            "H:/izpack-src/_dist/sample/", "standard",
                            "installer.jar");

            compiler.getCompiler().setPackagerListener(new PackagerListener()
            {

                public void packagerMsg(String arg0)
                {
                    packagerMsg(arg0, PackagerListener.MSG_INFO);

                }

                public void packagerMsg(String arg0, int arg1)
                {
                    final String prefix;
                    switch (arg1)
                    {
                    case MSG_DEBUG:
                        prefix = "[ DEBUG ] ";
                        break;
                    case MSG_ERR:
                        prefix = "[ ERROR ] ";
                        break;
                    case MSG_WARN:
                        prefix = "[ WARNING ] ";
                        break;
                    case MSG_INFO:
                    case MSG_VERBOSE:
                    default: // don't die, but don't prepend anything
                        prefix = "";
                    }

                    System.out.println(prefix + arg0);
                }

                public void packagerStart()
                {
                    System.out.println("Started packaging");

                }

                public void packagerStop()
                {
                    System.out.println("Stopped Packaging");
                }
            });

            //          CompilerConfig compiler = new CompilerConfig("H:\\insttemp\\", "standard", "installer.jar", null, );
            compiler.executeCompiler();

            // Waits
            while (compiler.isAlive())
                Thread.sleep(100);

            if (compiler.wasSuccessful())
                System.out.println("Successful Compilation");
        }
        catch (CompilerException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (Exception e2)
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }
}
