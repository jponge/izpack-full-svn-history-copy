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
package izpack.frontend.view.renderers;

import izpack.frontend.model.LanguageMap;
import izpack.frontend.model.stages.GeneralInformationModel.LangModel;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

/**
 * @author Andy Gombos
 */
public class LangLabel extends DefaultListCellRenderer
{
    public LangLabel()
    {
        displayLabel = new JLabel("", SwingConstants.LEFT);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof LangModel)
        {
            LangModel model = (LangModel) value;
            displayLabel.setText(langMap.get(model.iso3Code));
            displayLabel.setIcon(new ImageIcon(model.flag));
            
            if (isSelected)
                displayLabel.setBorder(selected);
            else
                displayLabel.setBorder(unselected);
            
            return displayLabel;
        }        
        
        return null;
    }    
    
    static Border unselected = new SoftBevelBorder(SoftBevelBorder.RAISED);
    static Border selected = new SoftBevelBorder(SoftBevelBorder.LOWERED);
    
    LanguageMap langMap = LanguageMap.getInstance();
    JLabel displayLabel;    
}
