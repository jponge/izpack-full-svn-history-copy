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

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

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
        
        panelListBinding = new SelectionInList(model.getPanels());
        
        panelList = BasicComponentFactory.createList(panelListBinding, new ItemProgressRenderer());
        
        add(panelList);
    }
    
    public ValueModel getSelectionIndexHolder()
    {
        return panelListBinding.getSelectionIndexHolder();
    }
    
    public ValueModel getSelectionHolder()
    {
        return panelListBinding.getSelectionHolder();
    }
    
    JList panelList;    
    SelectionInList panelListBinding;
    ConfigurationStageModel model;
}
