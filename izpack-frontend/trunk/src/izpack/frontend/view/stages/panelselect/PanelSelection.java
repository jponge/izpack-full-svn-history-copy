/*
 * Created on Jul 3, 2004
 * 
 * $Id: PanelSelection.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelection.java 
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

import izpack.frontend.view.stages.IzPackStage;

import org.w3c.dom.Document;

/**
 * @author Andy Gombos
 */
public class PanelSelection extends IzPackStage
{
	
	public void initializeStage() 
	{	
		add(new PanelSelect());
	}

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createXMLSaveData()
     */
    public Document createXMLSaveData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#readXMLSaveData(org.w3c.dom.Document)
     */
    public void readXMLSaveData(Document data)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public boolean validateStage()
    {
        // TODO Auto-generated method stub
        return false;
    }
	
}
