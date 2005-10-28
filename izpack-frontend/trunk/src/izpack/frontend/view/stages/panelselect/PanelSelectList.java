/*
 * Created on Mar 30, 2005
 * 
 * $Id: PanelSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PanelSelect.java 
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

package izpack.frontend.view.stages.panelselect;

import izpack.frontend.controller.PanelInfoManager;
import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.SelectListModel;
import izpack.frontend.model.stages.PanelModel;
import izpack.frontend.model.stages.PanelSelectionModel;
import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.SelectList;
import izpack.frontend.view.renderers.ImageLabel;

import java.util.ArrayList;
import java.util.Iterator;

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
        
        src.setCellRenderer(new ImageLabel());
        dest.setCellRenderer(new ImageLabel());
        
        panelSelectModel = new PanelSelectionModel();
        
        dest.setModel(panelSelectModel);        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
        int index = 0;
	    ArrayList panels = PanelInfoManager.getAvailablePanels();    	    
        for (Iterator iter = panels.iterator(); iter.hasNext();)
        {
            PanelInfo panel = (PanelInfo) iter.next();
            PanelModel model = new PanelModel();
            
            model.configData = panel;
            model.valid = false;
            
            ( (SelectListModel) src.getModel() ).addElement(model);
            index++;            
        }
	}
    
    public PanelSelectionModel getDestModel()
    {
        return panelSelectModel;
    }
    
    SelectList src, dest;
    PanelSelectionModel panelSelectModel;
}
