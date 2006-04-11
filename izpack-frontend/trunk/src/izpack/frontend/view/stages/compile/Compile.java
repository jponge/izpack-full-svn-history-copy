/*
 * Created on Oct 15, 2005
 * 
 * $Id: Compile.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Compile.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    
    public void cancelCompile()
    {
        CompileManager.stopCompile();                        
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
