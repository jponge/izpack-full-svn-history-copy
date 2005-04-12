/*
 * Created on Jul 3, 2004
 * 
 * $Id: GeneralInformation.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : GeneralInformation.java 
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

import izpack.frontend.view.stages.IzPackStage;

import javax.swing.JTabbedPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.validation.ValidationResult;

import utils.XML;

/**
 * @author Andy Gombos
 */
public class GeneralInformation extends IzPackStage
{
    public void initializeStage() 
	{	
		JTabbedPane tabs = new JTabbedPane();
		
		uiConfig = new UIConfig();
		languageSelect = new LanguageSelect();
		generalInfoPage = new GeneralInfoPanel();
		
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB1.Text"), generalInfoPage);		
        tabs.addTab(langResources().getText("UI.GeneralInformation.TAB2.Text"), languageSelect);
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB3.Text"), uiConfig);					
		
		add(tabs);
	}
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {           
        Element root = XML.createRootElement("installation");
        Document rootDoc = root.getOwnerDocument();
        
        root.setAttribute("version", "1.0");        
        
        Element genInfoXML = generalInfoPage.createXML();
        Element langXML = languageSelect.createXML();
        Element uiXML = uiConfig.createXML();               
        
        root.appendChild(rootDoc.importNode(genInfoXML, true));
        root.appendChild(rootDoc.importNode(langXML, true));
        root.appendChild(rootDoc.importNode(uiXML, true));
        
        return rootDoc;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = new ValidationResult();
        
        vr.addAllFrom(uiConfig.validatePanel());
        vr.addAllFrom(languageSelect.validatePanel());
        vr.addAllFrom(generalInfoPage.validatePanel());
        
        return vr;
    }
    
    private UIConfig uiConfig;
    private LanguageSelect languageSelect;
    private GeneralInfoPanel generalInfoPage;
}
