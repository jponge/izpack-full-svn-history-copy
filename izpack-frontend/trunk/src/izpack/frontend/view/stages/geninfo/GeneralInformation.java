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

import izpack.frontend.controller.validators.GeneralInfoValidator;
import izpack.frontend.model.stages.GeneralInformationModel;
import izpack.frontend.view.stages.IzPackStage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class GeneralInformation extends IzPackStage
{
    public GeneralInformation()
    {
        super();
        
        JTabbedPane tabs = new JTabbedPane();
		
		uiConfig = new UIConfig();
		languageSelect = new LanguageSelect();
		generalInfoPage = new GeneralInfoPanel();
		
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB1.Text"), generalInfoPage);		
        tabs.addTab(langResources().getText("UI.GeneralInformation.TAB2.Text"), languageSelect);
		tabs.addTab(langResources().getText("UI.GeneralInformation.TAB3.Text"), uiConfig);					
		
		add(tabs);		
    }
    
    public void initializeStage() 
	{			
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
        return getValidator().validate();
    }
    
    private UIConfig uiConfig;
    private LanguageSelect languageSelect;
    private GeneralInfoPanel generalInfoPage;

    /**
     * Return an empty panel to make the stage look correct in the frame
     */
    public JPanel getLeftNavBar()
    { 
        return new JPanel();
    }    

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        // TODO Auto-generated method stub
        return super.getBottomInfoBar();
    }
    
    public static GeneralInformationModel getDataModel()
    {
        if (model == null)
            model = new GeneralInformationModel();
        
        return model;
    }
    
    public static PresentationModel getValidatingModel()
    {
        if (presModel == null)
        {
            presModel = new PresentationModel(getDataModel());
            
            presModel.addBeanPropertyChangeListener(new PropertyChangeListener()
                            {

                                public void propertyChange(PropertyChangeEvent evt)
                                {
                                    System.out.println("Validating");                                    
                                    validationModel.setResult(getValidator().validate());
                                }                
                            });
        }
        
        return presModel;
    }    
    
    public static GeneralInfoValidator getValidator()
    {
        if (validator == null)
            validator = new GeneralInfoValidator(getDataModel());
        
        return validator;
    }
    
    private static GeneralInformationModel model = null;
    private static PresentationModel presModel = null;
    private static GeneralInfoValidator validator = null;
}
