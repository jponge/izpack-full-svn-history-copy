/*
 * Created on Mar 30, 2005
 * 
 * $Id: PanelSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelect.java 
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
package izpack.frontend.view.stages.panelselect;

import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.PanelInfoManager;
import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.ImageLabel;
import izpack.frontend.view.components.SelectList;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Andy Gombos
 */
public class PanelSelect extends AbstractListSelect
{
    public PanelSelect()
    {
        super();
        
        src = new SelectList();
        dest = new SelectList();
        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
	    ArrayList panels = PanelInfoManager.getAvailablePages();    	    
        for (Iterator iter = panels.iterator(); iter.hasNext();)
        {
            PanelInfo page = (PanelInfo) iter.next();
            
            src.addElement( new ImageLabel(page.getName(), page.getShortDesc(), "res/imgs/folder.png") );
        }
	}
    
    SelectList src, dest;

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#createXML()
     */
    public Element createXML()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }    
}
