/*
 * Created on Aug 25, 2005
 * 
 * $Id: PanelConfigurator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelConfigurator.java 
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
package izpack.frontend.view.stages.configure;

import javax.swing.JPanel;

import org.w3c.dom.Document;

import com.jgoodies.validation.ValidationResult;

import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;

public class PanelConfigurator extends IzPackStage
{

    public PanelConfigurator()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValidationResult validateStage()
    {
        return new ValidationResult();
    }

    @Override
    public StageDataModel getDataModel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void initializeStage()
    {
        // TODO Auto-generated method stub

    }

    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
