/*
 * Created on Aug 25, 2005
 * 
 * $Id: ItemProgressPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ItemProgressPanel.java 
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
package izpack.frontend.view.components;

import izpack.frontend.model.stages.ConfigurationStageModel;
import izpack.frontend.view.renderers.ItemProgressRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

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
        /*
        ArrayList<PanelModel> listElems = model.getPanels();
        
        Dimension largest = new Dimension(0,0);       
        
        for (int i = 0; i < listElems.size(); i++)
        {   
            Dimension size = panelList.getCellRenderer().getListCellRendererComponent(panelList, listElems.get(i), 
                            i, true, true).getPreferredSize();
            
            if (size.width > largest.width)
                largest = size;     
        }        
        
        System.out.println(panelList.getPreferredSize());
        panelList.setPreferredSize(new Dimension(largest.width + 5, panelList.getHeight()));
        System.out.println(panelList.getPreferredSize());
        */
    }
    
    JList panelList;    
    SelectionInList panelListBinding;
    ConfigurationStageModel model;        
}
