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
import izpack.frontend.model.SelectListModel;
import izpack.frontend.model.stages.PanelSelectionModel;
import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.SelectList;
import izpack.frontend.view.renderers.ImageLabel;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class PanelSelectList extends AbstractListSelect
{
    public PanelSelectList()
    {
        super();
        
        src = new SelectList();
        dest = new SelectList();
        
        panelSelectModel = new PanelSelectionModel();
        
        dest.setModel(panelSelectModel);        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
        int index = 0;
	    ArrayList panels = PanelInfoManager.getAvailablePages();    	    
        for (Iterator iter = panels.iterator(); iter.hasNext();)
        {
            PanelInfo page = (PanelInfo) iter.next();
            
            ( (SelectListModel) src.getModel() ).addElement( new ImageLabel(index, page.getClassname(), page.getName(), page.getShortDesc(), "res/imgs/folder.png") );
            index++;            
        }
	}
    
    public PanelSelectionModel getDestModel()
    {
        return panelSelectModel;
    }
    
    SelectList src, dest;
    PanelSelectionModel panelSelectModel;
    

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#createXML()
     */
    /*public Element createXML()
    {
        ListModel lm = dest.getModel();
        
        Element panels = XML.createRootElement("panels");
        Document panelsDoc = panels.getOwnerDocument();
        
        for (int i = 0; i < lm.getSize(); i++)
        {
            ImageLabel il = (ImageLabel) lm.getElementAt(i);
            String classname = il.getClassname();
            
            Element panel = XML.createElement("panel", panelsDoc);
            panel.setAttribute("classname", classname);
            
            panels.appendChild(panel);
        }
        
        return panels;
    }*/

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#validatePanel()
     */
    public ValidationResult validatePanel()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
