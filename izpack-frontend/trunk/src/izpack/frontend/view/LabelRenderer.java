package izpack.frontend.view;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
/*
 * Created on Nov 18, 2004
 * 
 * $Id: LabelRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LabelRenderer.java 
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

/**
 * @author Andy Gombos
 */
public class LabelRenderer extends DefaultListCellRenderer//implements ListCellRenderer
{

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JComponent c = (JComponent) value;
        if (isSelected)
            c.setBorder(selected);
        else
            c.setBorder(unselected);
        
        return (Component) c;        
    }

    Border unselected = new SoftBevelBorder(SoftBevelBorder.RAISED);
    Border selected = new SoftBevelBorder(SoftBevelBorder.LOWERED);
}
