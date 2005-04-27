/*
 * Created on Apr 19, 2005
 * 
 * $Id: EditorManager.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : EditorManager.java 
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
package izpack.frontend.view.stages.packs.editors;

import izpack.frontend.view.components.table.TableEditor;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Andy Gombos
 */
public class EditorManager
{
    private EditorManager()
    {
        editors = new ArrayList();
    }
    
    public static EditorManager getInstance()
    {
        if (instance == null)
        {
            instance = new EditorManager();
        }
        
        return instance;
    }
    
    public void addEditor(TableEditor editor)
    {
        editors.add(editor);
    }
    
    public TableEditor getEditor(Class type)
    {
        System.out.println("looking for " + type);
        for (Iterator iter = editors.iterator(); iter.hasNext();)
        {
            TableEditor editor = (TableEditor) iter.next();
            
            if (editor.handles(type))
                return editor;
        }
        
        return null;
    }
    
    private static EditorManager instance;
    private ArrayList editors;
}	
