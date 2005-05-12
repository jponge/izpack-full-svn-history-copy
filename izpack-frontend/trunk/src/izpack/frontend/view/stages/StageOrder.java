/*
 * Created on May 3, 2005
 * 
 * $Id: StageOrder.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : StageOrder.java 
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

import izpack.frontend.model.LangResources;
import izpack.frontend.view.IzPackFrame;
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
        stages = new ArrayList();        
                
        stages.add(new StageContainer(lr.getText("UI.StageNames.GenInfo"), GeneralInformation.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.PanelSelect"), PanelSelection.class));
        stages.add(new StageContainer(lr.getText("UI.StageNames.Packs"), Pack.class));
        //stages.add(new StageContainer(lr.getText("UI.StageNames.Configure"), Configure.class));
    }
    
    public StageContainer getPreviousStage(Class stage)
    {   
        int stageIndex = stages.indexOf(new StageContainer("", stage));        
        
        //For previous container
        stageIndex--;
        
        if (stageIndex >= 0)
            return (StageContainer) stages.get(stageIndex);
        else        
            return null;
    }
    
    public StageContainer getNextStage(Class stage)
    {
        int stageIndex = stages.indexOf(new StageContainer("", stage));        
        
        //For previous container
        stageIndex++;
        
        if (stageIndex < stages.size())
            return (StageContainer) stages.get(stageIndex);
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
    
    private ArrayList stages;
    private static LangResources lr = IzPackFrame.getInstance().langResources();
}
