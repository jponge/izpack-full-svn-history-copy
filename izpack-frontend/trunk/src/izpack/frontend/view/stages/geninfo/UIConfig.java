/*
 * Created on Mar 30, 2005
 * 
 * $Id: UIConfig.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : UIConfig.java 
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
package izpack.frontend.view.stages.geninfo;

import izpack.frontend.view.components.validating.VTextField;
import izpack.frontend.view.stages.panels.ConfigurePanel;
import izpack.frontend.view.stages.panels.IzPackPanel;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class UIConfig extends IzPackPanel implements ConfigurePanel
{

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.Page#initComponents()
     * 
     * TODO Implement LAF preferences 
     */
    public void initComponents()
    {
        FormLayout layout = new FormLayout("pref, 3dlu, pref, 30dlu",
        "pref, 3dlu, pref, 3dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        
        width = new VTextField(5);
        height = new VTextField(5);
        
        resizable = new JCheckBox("Allow window resizes");
        builder.add(resizable, new CellConstraints(1, 1, 4, 1));
        builder.nextRow(2);        
        builder.add(new JLabel("Width"));
        builder.setColumn(3);
        builder.add(width);
        builder.nextRow(2);
        builder.setColumn(1);
        builder.add(new JLabel("Height"));
        builder.setColumn(3);
        builder.add(height);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#createXML()
     * 
     * Structure
     * <guiprefs resizable="no" width="800" height="600"/>
     */
    public Element createXML()
    {
        Document doc = XML.getDocument();
        Element guiprefs = doc.createElement("guiprefs");
        
        if (resizable.isSelected())
            guiprefs.setAttribute("resizable", "yes");
        else
            guiprefs.setAttribute("resizable", "no");
        
        guiprefs.setAttribute("width", width.getText());
        guiprefs.setAttribute("height", height.getText());
        
        System.out.println(guiprefs);
        return guiprefs;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }
    
    public boolean validatePage()
    {
        boolean valid = true;
        
        try
        {
            int tmp = Integer.parseInt(width.getText());            
        }
        catch (NumberFormatException nfe)
        {
            width.setInvalid();
            valid = false;
        }
        
        try
        {
            int tmp = Integer.parseInt(height.getText());
        }
        catch (NumberFormatException nfe)
        {
            height.setInvalid();
            valid = false;
        }
        
        return width.isValid() && height.isValid() && valid;
    }
    
    JCheckBox resizable;
    VTextField width, height;
}
