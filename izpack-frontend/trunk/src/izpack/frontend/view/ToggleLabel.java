/*
 * Created on Jun 26, 2004
 * 
 * $Id: ToggleLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ToggleLabel.java 
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
package izpack.frontend.view;


import java.awt.Color;

import javax.swing.BorderFactory;


/**
 * @author Andy Gombos
 */
public class ToggleLabel extends ImageLabel
{
    public ToggleLabel(String name, String shortDesc, String imgFile)
    {
        super("<html>" +
        		"<b>" + name + "</b>" +
        		"<p> <font color=#969696>" + shortDesc + "</font>", imgFile);       
        
        setBorder(BorderFactory.createLineBorder(Color.decode("0xB8B8B8")));
        setBackground(Color.WHITE);
    }
}
