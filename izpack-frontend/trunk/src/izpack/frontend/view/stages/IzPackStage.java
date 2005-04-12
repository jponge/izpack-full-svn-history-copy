/*
 * Created on Jul 30, 2004
 * 
 * $Id: IzPackStage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : IzPackStage.java 
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
package izpack.frontend.view.stages;

import izpack.frontend.controller.GUIController;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import org.w3c.dom.Document;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public abstract class IzPackStage extends JPanel implements Stage
{        
    public abstract Document createInstallerData();
    
    public abstract ValidationResult validateStage();
    
    public void registerStage(Object stage)
    {        
        stageList.add(stage);
    }

    public IzPackStage getStage(Class stage)
    {
        for (Iterator iter = stageList.iterator(); iter.hasNext();)
        {
            IzPackStage element = (IzPackStage) iter.next();
            if (element.getClass().equals(stage))
            {
                return element;
            }
        }
        
        return null;
    }

    /**
	 * Get the lang-resources object.
	 * @return The language resource model.
	 */
	public LangResources langResources() {
		return GUIController.getInstance().langResources();
	}
	
	/**
	 * Get the application configuration.
	 * @return the application configuration model.
	 */
	public AppConfiguration appConfiguration() {
		return GUIController.getInstance().appConfiguration();
	}
	
	private ArrayList stageList = new ArrayList();
}
