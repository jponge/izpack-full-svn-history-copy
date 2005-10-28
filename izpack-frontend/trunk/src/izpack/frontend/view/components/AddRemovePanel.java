/*
 * Created on Apr 7, 2005
 * 
 * $Id: AddRemovePanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : AddRemovePanel.java 
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
