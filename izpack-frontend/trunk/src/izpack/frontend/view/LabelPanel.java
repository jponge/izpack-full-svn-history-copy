/*
 * Created on Jun 26, 2004
 * 
 * $Id: LabelPanel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LabelPanel.java 
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

import izpack.frontend.model.PageInfo;
import izpack.frontend.model.PageInfoManager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class LabelPanel
{
    public LabelPanel(boolean blank)
    {        
        if (!blank)
        {	                
	        CellConstraints cc = new CellConstraints();
	        
	        builder.appendRow("3dlu");	        
	        
	        for (Iterator iter = panels.iterator(); iter.hasNext();)
	        {
	            PageInfo page = (PageInfo) iter.next();
	            
	            ToggleLabel tl = new ToggleLabel(page.getName(), page.getShortDesc(), "res/imgs/folder.png");
	            labels.add(tl);
	            
	            builder.appendRow("pref");
	            builder.add(tl, cc.xy(2, curRow));
	            builder.appendRow("3dlu");
	            curRow++; curRow++;
	        }
        }        
    }
    
    public JPanel getPanel()
    {
        JPanel p = builder.getPanel();
        p.setBackground(Color.decode("0xECEFF5"));
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return p;
    }
    
    //This algorithm may not be perfect
    public void addLabel(int selection[])
    {
        curRow++;
        if (builder.getRowCount() < curRow)
        {
            builder.appendRow("pref");
        }
        
        CellConstraints cc = new CellConstraints();
        
        for (int i = 0; i < selection.length; i++)
        {
            builder.add((ToggleLabel) labels.get(i), cc.xy(2, curRow));
            builder.appendRow("3dlu");   
            curRow++; curRow++;
        }        
    }    
      
    int curRow = 2;
    FormLayout layout = new FormLayout("3dlu, pref, 3dlu");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    ArrayList panels = PageInfoManager.getAvailablePages();
    ArrayList labels = new ArrayList(panels.size());
}
