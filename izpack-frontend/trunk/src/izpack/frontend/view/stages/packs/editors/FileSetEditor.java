/*
 * Created on Apr 22, 2005
 * 
 * $Id: FileSetEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileSetEditor.java 
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

import java.awt.Frame;

import izpack.frontend.model.files.ElementModel;
import izpack.frontend.view.components.table.TableEditor;

/**
 * @author Andy Gombos
 */
public class FileSetEditor extends TableEditor
{

    /**
     * @param parent
     */
    public FileSetEditor(Frame parent)
    {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configure(izpack.frontend.model.ElementModel)
     */
    public void configure(ElementModel model)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
