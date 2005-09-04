/*
 * Created on Apr 11, 2005
 * 
 * $Id: PackListHeader.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PackListHeader.java 
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
package izpack.frontend.view.components.table;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class PackListHeader extends JPanel
{
    public PackListHeader()
    {	    
	    FormLayout layout = new FormLayout("10dlu, 80dlu, 4dlu, 80dlu, 15dlu, 40dlu, 10dlu", "15dlu");
	    DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
	    
	    CellConstraints cc = new CellConstraints();
	    
	    builder.add(new JLabel("Name"), cc.xy(2, 1));
	    builder.add(new JLabel("Description"), cc.xy(4, 1));
	    builder.add(new JLabel("Required"), cc.xy(6, 1));
    }
}
