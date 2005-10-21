/*
 * Created on Oct 15, 2005
 * 
 * $Id: Compile.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Compile.java 
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
package izpack.frontend.view.stages.compile;

import izpack.frontend.actions.CompileDisplay;
import izpack.frontend.actions.CompileEvent;
import izpack.frontend.actions.CompileListener;
import izpack.frontend.actions.CompileManager;
import izpack.frontend.controller.XMLCreator;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;

import java.io.StringWriter;

import javax.swing.JPanel;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.validation.ValidationResult;

public class Compile extends IzPackStage implements CompileListener
{
    public Compile()
    {        
        displayer = new CompileDisplay(false, null);
        
        displayer.addCompileListener(this);
        
        add(displayer);
    }
    
    public void compileRequested(CompileEvent ce)
    {    
        displayer.next();
        
        CompileManager.compile(new XMLCreator(IzPackStage.getAllStages()).createInstallXML(), 
                        new String[]{ce.getBaseDir(),
                        ce.getInstallType(), 
                        ce.getOutputFile()}, 
                        displayer.getPackagerListener());
    }

    @Override
    public Element[] createInstallerData(Document doc)
    {        
        return null;
    }

    @Override
    public ValidationResult validateStage()
    {        
        return ValidationResult.EMPTY;
    }

    @Override
    public StageDataModel getDataModel()
    {
        return null;
    }

    public void initializeStage()
    {    
        
    }

    public void initializeStageFromXML(Document doc)
    {
        //Do nothing        
    }

    public JPanel getLeftNavBar()
    {
        // TODO Auto-generated method stub
        return new JPanel();
    }
    
    final CompileDisplay displayer;
}
