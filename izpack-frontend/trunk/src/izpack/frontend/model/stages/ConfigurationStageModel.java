/*
 * Created on Aug 25, 2005
 * 
 * $Id: ConfigurationStageModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ConfigurationStageModel.java 
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

package izpack.frontend.model.stages;

import izpack.frontend.view.stages.configure.panels.ConfigurePanel;

import java.util.ArrayList;

import javax.swing.ListModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.binding.beans.Model;

public class ConfigurationStageModel extends Model implements StageDataModel
{

    public ConfigurationStageModel(PanelSelectionModel psm)
    {
        super();
        
        //panels = new ArrayListModel(psm);
        panels = psm;
        editors = new ArrayList<ConfigurePanel>();
    }

    //TODO fix this so the elements are inserted properly
    public Element[] writeToXML(Document doc)
    {   
        if (editors == null)
            return new Element[]{};
        
        ArrayList<Element> editorXML = new ArrayList<Element>(editors.size());
        
        //TODO Create the interesting XML data
        for (ConfigurePanel editor : editors)
        {
            Element xml = editor.createXML(doc);
            
            if (xml != null)
            {   
                editorXML.add(xml);
            }
        }        
        
        return editorXML.toArray(new Element[0]);
    }

    public void initFromXML(Document doc)
    {
        PanelSelectionModel psm = (PanelSelectionModel) panels;
        
        if (psm.getSize() == 0)
            psm.initFromXML(doc);
    
        for (int i = 0; i < psm.getSize(); i++)
        {
            
        }
    }
    
    public void setEditors(ArrayList<ConfigurePanel> editors)
    {
        this.editors = editors;
    }

    private ListModel panels;
    private ArrayList<ConfigurePanel> editors;
    int currentlyActivePanel;
    
    public int getCurrentlyActivePanel()
    {
        return currentlyActivePanel;
    }

    public ListModel getPanels()
    {
        return panels;
    }
 
    public void setCurrentlyActivePanel(int currentlyActivePanel)
    {
        this.currentlyActivePanel = currentlyActivePanel;
    }

    public void setPanels(ListModel panels)
    {
        this.panels = panels;
    }
    
    public void setPanels(ArrayList<PanelModel> panels)
    {
        //this.panels = new ArrayListModel(panels);
    }

    public ArrayList<ConfigurePanel> getEditors()
    {
        return editors;
    }
}
