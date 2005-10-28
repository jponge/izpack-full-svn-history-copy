/*
 * Created on Jul 3, 2004
 * 
 * $Id: GeneralInformation.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : GeneralInformation.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package izpack.frontend.view.stages.geninfo;

import izpack.frontend.controller.validators.GeneralInfoValidator;
import izpack.frontend.controller.validators.StageValidator;
import izpack.frontend.model.stages.GeneralInformationModel;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.stages.IzPackStage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */
public class GeneralInformation extends IzPackStage
{
    public GeneralInformation()
    {
        super();
        
        JTabbedPane tabs = new JTabbedPane();
		
		uiConfig = new UIConfig(this);
		languageSelect = new LanguageSelect(this);
		generalInfoPage = new GeneralInfoPanel(this);
		
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
    public Element[] createInstallerData(Document doc)
    {           
        return model.writeToXML(doc);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validateStage()
    {
        ValidationResult vr = getValidator().validate();
        validationModel.setResult(vr);
        
        return vr;
    }
    
    private static UIConfig uiConfig;
    private static LanguageSelect languageSelect;
    private static GeneralInfoPanel generalInfoPage;

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
    
    public StageDataModel getDataModel()
    {
        if (model == null)
            model = new GeneralInformationModel();
        
        return model;
    }
    
    public PresentationModel getValidatingModel()
    {
        if (presModel == null)
        {
            presModel = new PresentationModel(getDataModel());
            
            presModel.addBeanPropertyChangeListener(new PropertyChangeListener()
                            {
                                public void propertyChange(PropertyChangeEvent evt)
                                {                                    
                                    ValidationResult result = getValidator().validate();
                                    validationModel.setResult(result);
                                    ValidationComponentUtils.updateComponentTreeValidationBackground(generalInfoPage, result);
                                }                
                            });

            //Watch the language panel changes
            //I don't know if there is a more binding-intensive way to do this
            //But I already changed the model over to be bound, so it will stay that way :)
            model.getLangCodes()
                            .addListDataListener(new ListDataListener()
                            {

                                public void intervalAdded(ListDataEvent e)
                                {                                                                     
                                    validationModel.setResult(getValidator().validate());
                                }

                                public void intervalRemoved(ListDataEvent e)
                                {                                                                     
                                    validationModel.setResult(getValidator().validate());
                                }

                                public void contentsChanged(ListDataEvent e)
                                {                                                                     
                                    validationModel.setResult(getValidator().validate());
                                }
                            });
        }
        
        return presModel;
    }    
    
    public StageValidator getValidator()
    {
        if (validator == null)
            validator = new GeneralInfoValidator((GeneralInformationModel) getDataModel());
        
        return validator;
    }
    
    private static GeneralInformationModel model = null;
    private static PresentationModel presModel = null;
    private static GeneralInfoValidator validator = null;

    public void initializeStageFromXML(Document doc)
    {
        model.initFromXML(doc);        
    }
}
