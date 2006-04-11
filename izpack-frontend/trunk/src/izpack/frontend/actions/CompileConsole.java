/*
 * Created on Oct 19, 2005 $Id: CompileConsole.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos File : CompileConsole.java
 * Description : TODO Add description Author's email : gumbo@users.berlios.de
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.izforge.izpack.compiler.PackagerListener;

public class CompileConsole extends JComponent implements PackagerListener
{
    public CompileConsole()
    {
        // Create an anoymous subclass that disables line wrapping
        console = new JTextPane()
        {
            public boolean getScrollableTracksViewportWidth()
            {

                if (getSize().width < getParent().getSize().width)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            public void setSize(Dimension d)
            {

                if (d.width < getParent().getSize().width)
                {
                    d.width = getParent().getSize().width;
                }

                super.setSize(d);
            }
        };

        scroll = new JScrollPane(console);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        
        //TODO Internationalize
        redo = new JButton("Return to the settings panel");
        
        add(redo, BorderLayout.SOUTH);
        
        consoleDocument = console.getDocument();
    }

    public void packagerMsg(String arg0)
    {
        packagerMsg(arg0, PackagerListener.MSG_INFO);
    }

    public void packagerMsg(String arg0, int arg1)
    {
        final SimpleAttributeSet attribs = new SimpleAttributeSet();

        switch (arg1)
        {
            case MSG_DEBUG:
                attribs.addAttribute(StyleConstants.Background, Color.BLUE);
                break;
            case MSG_ERR:
                attribs.addAttribute(StyleConstants.Background, Color.RED);
                break;
            case MSG_WARN:
                attribs.addAttribute(StyleConstants.Background, Color.YELLOW);
                break;
            case MSG_INFO:
            case MSG_VERBOSE:
            default: // don't die, but don't set any highlighting styles
        }

        try
        {
            // Insert the highlighted text
            consoleDocument.insertString(consoleDocument.getLength(), arg0
                            + "\n", attribs);
            
            console.setCaretPosition(consoleDocument.getLength());
        }
        catch (BadLocationException e)
        {
        }
    }

    public void packagerStart()
    {
        try
        {
            consoleDocument.insertString(consoleDocument.getLength(),
                            "Started Packaging \n", null);
        }
        catch (BadLocationException e)
        {
        }

    }

    public void packagerStop()
    {
        try
        {
            consoleDocument.insertString(consoleDocument.getLength(),
                            "Stopped Packaging \n", null);
        }
        catch (BadLocationException e)
        {
        }
    }
    
    public JButton getReturnButton()
    {
        return redo;
    }

    private JTextPane console;
    private JScrollPane scroll;
    private JButton redo;

    private Document consoleDocument;    
}
