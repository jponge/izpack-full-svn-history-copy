/*
 * Created on Mar 31, 2005
 * 
 * $Id: LangLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LangLabel.java 
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

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author Andy Gombos
 */
public class LangLabel extends JLabel
{
    /**
     * @param text
     * @param icon
     * @param horizontalAlignment
     */
    public LangLabel(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);                
    }
    
    public void setISO3Code(String code)
    {
        iso3Code = code;
    }
    
    public String getISO3Code()
    {
        return iso3Code;
    }
    
    String iso3Code;
}
