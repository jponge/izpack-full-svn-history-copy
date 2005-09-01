/*
 * Created on Apr 7, 2005
 * 
 * $Id: AddRemovePanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : AddRemovePanel.java 
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

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class AddRemovePanel extends JPanel
{
    public AddRemovePanel(String object)
    {
	    FormLayout layout = new FormLayout("3dlu, pref, 3dlu", "6dlu, pref, 12dlu, pref, 6dlu");
	    DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
	    
	    add = new JButton("Add " + object);
	    remove = new JButton("Remove " + object);
	    
	    builder.setRow(2);
	    builder.setColumn(2);
	    builder.add(add);
	    builder.setRow(4);
	    builder.setColumn(2);
	    builder.add(remove);
    }
    
    public void attachAddListener(ActionListener listener)
    {
        add.addActionListener(listener);
    }
    
    public void attachRemoveListener(ActionListener listener)
    {
        remove.addActionListener(listener);
    }
    
    JButton add, remove;
}
