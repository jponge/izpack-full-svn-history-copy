/*
 * Created on Nov 18, 2004
 * 
 * $Id: VTextBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : VTextBox.java 
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
package izpack.frontend.view.components.validating;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Andy Gombos
 * 
 * A validating textfield - checks to see if input exists
 */
public class VTextInput extends VComponent
{

    public VTextInput(String labelText)
    {
        super();
        label = new JLabel(labelText);
        input = new JTextField();
     
        //TODO make this look better - it's squished
        input.setPreferredSize(new Dimension(250, label.getPreferredSize().height));
        
        super.add(label);
        super.add(input);
    }
    
    public String getText()
    {
        return input.getText();
    }
    
    public void setText(String text)
    {
        input.setText(text);
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.validating.VComponent#isInputValid()
     */
    protected boolean isInputValid()
    {
        return !input.getText().equals("");         
    }   
    
    JTextField input;
}
