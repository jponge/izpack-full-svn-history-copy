/*
 * Created on Apr 27, 2005
 * 
 * $Id: DependencyListSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : DependencyListSelect.java 
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

import izpack.frontend.model.PackModel;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class DependencyListSelect extends AbstractListSelect
{
    public DependencyListSelect(ArrayList packs)
    {
        super();
        
        src = new SelectList();
        dest = new SelectList();
        
        this.packs = packs;
        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
        for (Iterator iter = packs.iterator(); iter.hasNext();)
        {
            PackModel pm = (PackModel) iter.next();
            src.addElement(pm.getName());
        }
    }
    
    SelectList src, dest;
    ArrayList packs;
    
    /*
     *	Not implemented... 
     */
    public Element createXML()
    {        
        return null;
    }

    public void initFromXML(Document xmlFile)
    {
    }
    
    public ValidationResult validatePanel()
    {
        return null;
    }
}
