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
		generalInfoPage = new GeneralInfoPage();
		
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB1.Text"), generalInfoPage);		
        tabs.addTab(langResources().getText("UI.GeneralInformation.TAB2.Text"), languageSelect);
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB3.Text"), uiConfig);					
		
		add(tabs);
	}

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createXMLSaveData()
     */
    public Document createXMLSaveData()
    {   
        if (! (languageSelect.validatePage() && uiConfig.validatePage()))
            return null;
        
        Document d = XML.getDocument();
        Element root = XML.createElement("root");
        Element e = languageSelect.createXML();
        Element ee = uiConfig.createXML();
        
               
        d.appendChild(root);
        root.appendChild(e);
        root.appendChild(ee);
        Element eee = XML.createElement("blank");
        ee.appendChild(eee);
        root.appendChild(ee);
        XML.writeXML("F:\\lang.xml", d);
        
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#readXMLSaveData(org.w3c.dom.Document)
     */
    public void readXMLSaveData(Document data)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#createInstallerData()
     */
    public Document createInstallerData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public boolean validateStage()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    UIConfig uiConfig;
    LanguageSelect languageSelect;
    GeneralInfoPage generalInfoPage;
}
