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

import izpack.frontend.view.components.YesNoCheckBox;
import izpack.frontend.view.stages.panels.ConfigurePanel;
import izpack.frontend.view.stages.panels.IzPackPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */
public class UIConfig extends IzPackPanel implements ConfigurePanel, ActionListener, FocusListener
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
        
        width = new JTextField(5);
        width.addFocusListener(this);
        height = new JTextField(5);
        height.addFocusListener(this);
        
        resizable = new YesNoCheckBox("Allow window resizes");
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
        
        configureValidation();
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
                
        guiprefs.setAttribute("resizable", resizable.getState());        
        
        guiprefs.setAttribute("width", width.getText());
        guiprefs.setAttribute("height", height.getText());        
        
        return guiprefs;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }
    
    public ValidationResult validatePanel()
    {
        return validateSizeFields();
    }
    
    private void configureValidation()
    {     
        ValidationComponentUtils.setMandatory(height, true);
        ValidationComponentUtils.setMandatory(width, true);
        
        ValidationComponentUtils.setMessageKey(height, "GUIPrefs.height");
        ValidationComponentUtils.setMessageKey(width, "GUIPrefs.width");
        
        ValidationComponentUtils.updateComponentTreeValidationBackground(this, ValidationResult.EMPTY);
    }
    
    private ValidationResult validateSizeFields()
    {   
        ValidationResult vr = new ValidationResult();
        
        if (!ValidationUtils.isDigit(width.getText()))
        {            
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must be an integer",
                            width,
                            "GUIPrefs",
                            "width"                            
                            ));            
        }
        
        if (!ValidationUtils.isDigit(height.getText()))
        {            
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must be an integer",
                            height,
                            "GUIPrefs",
                            "height"                            
                            ));
            
            ValidationComponentUtils.setErrorBackground(height);
        }
        
        ValidationComponentUtils.updateComponentTreeValidationBackground(this, vr);
        return vr;
    }   
    
    public void focusGained(FocusEvent e){}

    /*
     *  Validate components upon losing focus
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e)
    {
        if (e.getSource() instanceof JTextField)
            validateSizeFields();
    }
    
    /*
     * Validate on enter pressed
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JTextField)
            validateSizeFields();        
    }
    
    YesNoCheckBox resizable;
    JTextField width, height;
}
