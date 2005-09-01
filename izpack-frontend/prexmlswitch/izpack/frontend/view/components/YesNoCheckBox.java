/*
 * Created on Apr 1, 2005
 * 
 * $Id: YesNoCheckBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : YesNoCheckBox.java 
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

import javax.swing.JCheckBox;

/**
 * Standard JCheckBox, but provides a simple way to get a "yes" or "no" state value
 * 
 * @author Andy Gombos
 */
public class YesNoCheckBox extends JCheckBox
{    
    
    public YesNoCheckBox()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public YesNoCheckBox(String text)
    {
        super(text);
        // TODO Auto-generated constructor stub
    }
    
    public YesNoCheckBox(String text, boolean selected)
    {
        super(text, selected);
        // TODO Auto-generated constructor stub
    }
    
    public String getState()
    {        
        return isSelected() ? "yes" : "no";
    }
}
