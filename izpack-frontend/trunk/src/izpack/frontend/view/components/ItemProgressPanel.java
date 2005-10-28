/*
 * Created on Aug 25, 2005
 * 
 * $Id: ItemProgressPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ItemProgressPanel.java 
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

package izpack.frontend.view.components;

import izpack.frontend.model.stages.ConfigurationStageModel;
import izpack.frontend.view.renderers.ItemProgressRenderer;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.LineBorder;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/*
 * 
 * TODO Associate this with the XML class containers as well as the panel configurators, see what information needs to be present
 * 
 * This is more of an indicator rather than an actuator, though
 * 
 * 
 */
public class ItemProgressPanel extends JPanel
{
    public ItemProgressPanel(ConfigurationStageModel model)
    {
        this.model = model;
        
        panelListBinding = new SelectionInList((ListModel) model.getPanels());
        
        panelList = BasicComponentFactory.createList(panelListBinding, new ItemProgressRenderer());
        
        //panelList = new JList(new ArrayListModel(model.getPanels()));
        //panelList.setCellRenderer(new ItemProgressRenderer());
        
        add(panelList);
        
        setBorder(new LineBorder(Color.BLACK));
    }
    
    public ValueModel getSelectionIndexHolder()
    {
        return panelListBinding.getSelectionIndexHolder();
    }
    
    public ValueModel getSelectionHolder()
    {
        return panelListBinding.getSelectionHolder();
    }    
    
    public ValueModel getListModel()
    {
        return panelListBinding.getListHolder();
    }
    
    public void setPanels(ListModel list)
    {
        panelListBinding.setListModel(list);
    }
    
    public void calculatePreferredSize()
    {   
        ListModel listElems = model.getPanels();
        
        Dimension largest = new Dimension(0,0);     
        int height = 0;
        
        for (int i = 0; i < listElems.getSize(); i++)
        {   
            Dimension size = panelList.getCellRenderer().getListCellRendererComponent(panelList, listElems.getElementAt(i), 
                            i, true, true).getPreferredSize();
            
            height += size.height;
            
            if (size.width > largest.width)
                largest = size;
        }
        
        panelList.setPreferredSize(new Dimension(largest.width + 5, height));              
    }
    
    JList panelList;    
    SelectionInList panelListBinding;
    ConfigurationStageModel model;        
}
