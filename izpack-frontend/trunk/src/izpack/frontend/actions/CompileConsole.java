/*
 * Created on Oct 19, 2005 $Id: CompileConsole.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group File : CompileConsole.java
 * Description : TODO Add description Author's email : gumbo@users.berlios.de
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

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

    private JTextPane console;
    private JScrollPane scroll;

    private Document consoleDocument;
}
