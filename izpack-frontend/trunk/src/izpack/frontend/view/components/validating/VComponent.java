/*
 * Created on Nov 18, 2004
 * 
 * $Id: VComponent.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : VComponent.java 
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

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Andy Gombos
 */
public abstract class VComponent extends JPanel
{
    /*
     * Called by pages to validate all the input parts (text in a text box, files from a chooser, etc)
     */
    public boolean validateInput()    
    {
        boolean valid = isInputValid();
        if (!valid)
            label.setForeground(Color.RED);
        
        return valid;
    }
    
    /*
     * Called internally to determine if the input is actually valid
     * 
     * 
     */
    protected abstract boolean isInputValid();
    
    JLabel label;    
}