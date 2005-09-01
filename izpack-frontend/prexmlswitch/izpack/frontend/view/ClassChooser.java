/*
 * Created on Apr 23, 2005
 * 
 * $Id: ClassChooser.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ClassChooser.java 
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
package izpack.frontend.view;

import izpack.frontend.model.LangResources;
import izpack.frontend.view.components.JARTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import utils.UI;

import com.jgoodies.forms.factories.ButtonBarFactory;

/**
 * @author Andy Gombos
 */
public class ClassChooser extends JDialog implements ActionListener
{
    private JARTree tree;
    private JButton ok;
    private JButton cancel;
    
    private static LangResources lr = IzPackFrame.getInstance().langResources();
    private boolean cancelled;

    public ClassChooser(Frame parent, String text)
    {
        super(parent, text);
        
        tree = new JARTree();        
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
        
        setLayout(new BorderLayout());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        add(ButtonBarFactory.buildOKCancelBar(ok, cancel), BorderLayout.SOUTH);
        
        setPreferredSize(new Dimension(300, 400));
        pack();
        setModal(true);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(ok))
        {
            setVisible(false);
            cancelled = false;
        }
        else if (e.getSource().equals(cancel))
        {
            setVisible(false);
            cancelled = true;
        }        
    }
    
    public String getSelectedClass(File jarFile)
    {   
        JarFile jar = null;
        try
        {
            jar = new JarFile(jarFile);
        }
        catch (IOException e)
        {
            UI.showError(e.getLocalizedMessage(), lr.getText("UI.Error"));
            return "";
        }
        
        tree.addJar(jar);
        
        setVisible(true);
        
        if (cancelled)
            return "";
        else
        {
            TreePath path = tree.getSelectionPath();
            String fqc = "";            
            
            //0 is the root node
            Object[] pathComponents = path.getPath();            
            
            String sPackage = (String) ( (DefaultMutableTreeNode) pathComponents[1] ).getUserObject();
            String className = (String) ( (DefaultMutableTreeNode) pathComponents[2] ).getUserObject();
            
            if (sPackage.equals("Default Package"))
                fqc = className;
            else
                fqc = sPackage + "." + className;            
            
            return fqc;
        }
        
    }
}
