/*
 * Created on Apr 23, 2005
 * 
 * $Id: JARTree.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : JARTree.java 
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
package izpack.frontend.view.components;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * 
 * Display all the classes in a JAR file in a nice JTree
 * @author Andy Gombos
 */
public class JARTree extends JTree
{    
    public JARTree()
    {
        super();
        
        root = new DefaultMutableTreeNode();
        setModel(new DefaultTreeModel(root));
        
        setRootVisible(true);
        
        packages = new HashMap();
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(new ImageIcon("res/imgs/class.png"));
        renderer.setOpenIcon(new ImageIcon("res/imgs/packageOpen.png"));
        renderer.setClosedIcon(new ImageIcon("res/imgs/packageClosed.png"));
        
        setCellRenderer(renderer);
    }
    
    public void addJar(JarFile jar)
    {           
        Enumeration jarEntries = jar.entries();
        String path, name;
        
        root.setUserObject(jar.getName());
        
        for (int currentEntry = 0; jarEntries.hasMoreElements(); currentEntry++)
        {
            String entry = ( (JarEntry) jarEntries.nextElement()).getName();
            
            if (entry.endsWith(".class"))
            {                
                int packageEnd = entry.lastIndexOf('/');
                if (packageEnd == -1)
                {
                    path = "Default Package";
                    name = entry.substring(0, entry.length() - 6);
                }
                else
                {
                    path = entry.substring(0, packageEnd);
                    name = entry.substring(packageEnd + 1, entry.length() - 6);
                }
                
                if (name.indexOf('$') == -1)
                {                    
                    path = path.replace('/', '.');
                    
                    DefaultMutableTreeNode parent = getPackageNode(path);
                    parent.add(new DefaultMutableTreeNode(name));                    
                }
            }
        }
        
        //Ensure that the package level is visible
        TreePath rootPath = new TreePath( ( (DefaultMutableTreeNode) root.getFirstChild() ).getPath() );
        makeVisible(rootPath);
    }
    
    private DefaultMutableTreeNode getPackageNode(String path)
    {
        //We already have created this package
        if (packages.containsKey(path))
        {
            return (DefaultMutableTreeNode) packages.get(path);
        }
        //Create it
        else
        {
	        DefaultMutableTreeNode newParent = new DefaultMutableTreeNode(path);
	        packages.put(path, newParent);        
	        
	        root.add(newParent);
	        
	        return newParent;
        }
    }
    
    DefaultMutableTreeNode root;
    
    /*
     * Allow us to quickly find the right parent node for a class
     * Beats searching the tree every time
     */
    HashMap packages;    
}
