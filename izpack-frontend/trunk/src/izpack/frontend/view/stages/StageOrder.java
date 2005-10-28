/*
 * Created on May 3, 2005
 * 
 * $Id: StageOrder.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : StageOrder.java 
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

package izpack.frontend.view.stages;

import izpack.frontend.model.LangResources;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.stages.compile.Compile;
import izpack.frontend.view.stages.configure.PanelConfigurator;
import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.packs.Pack;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.util.ArrayList;

/**
 * @author Andy Gombos
 */
public class StageOrder
{
    public StageOrder()
    {
        stages = new ArrayList<StageContainer>();        
                
        stages.add(new StageContainer(lr.getText("UI.StageNames.GenInfo"), GeneralInformation.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.PanelSelect"), PanelSelection.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.Packs"), Pack.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.Configure"), PanelConfigurator.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.Compile"), Compile.class));
    }
    
    public StageContainer getPreviousStage(Class stage)
    {   
        int stageIndex = stages.indexOf(new StageContainer("", stage));        
        
        //For previous container
        stageIndex--;
        
        if (stageIndex >= 0)
            return stages.get(stageIndex);
        else        
            return null;
    }
    
    public StageContainer getNextStage(Class stage)
    {
        int stageIndex = stages.indexOf(new StageContainer("", stage));        
        
        //For previous container
        stageIndex++;
        
        if (stageIndex < stages.size())
            return stages.get(stageIndex);
        else        
            return null;
    }
    
    public class StageContainer
    {
        public StageContainer(String buttonText, Class stageClass)
        {
            this.buttonText = buttonText;
            this.stageClass = stageClass;
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {   
            return obj.equals(stageClass);
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return stageClass.getSimpleName();         
        }
        
        public String buttonText = "";
        public Class stageClass = null;        
    }
    
    private ArrayList<StageContainer> stages;
    private static LangResources lr = IzPackFrame.getInstance().langResources();
}
