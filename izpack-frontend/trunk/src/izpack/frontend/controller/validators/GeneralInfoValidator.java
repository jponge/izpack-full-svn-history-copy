/*
 * Created on May 24, 2005
 * 
 * $Id: GeneralInfoValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : GeneralInfoValidator.java 
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

package izpack.frontend.controller.validators;

import izpack.frontend.model.stages.GeneralInformationModel;

import java.net.MalformedURLException;
import java.net.URL;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * Validates the GeneralInfo model.
 * 
 * TODO Internationalize
 * @author Andy Gombos
 */
public class GeneralInfoValidator implements StageValidator
{
    public GeneralInfoValidator(GeneralInformationModel model)
    {
        this.model = model;
    }
    
    public ValidationResult validate()
    {
        PropertyValidationSupport infoSupport = new PropertyValidationSupport(model, "Info");
        
        if (ValidationUtils.isBlank(model.getAppName()))
        {
            infoSupport.addError("application name", "is mandatory");            
        }
        
        if (ValidationUtils.isBlank(model.getVersion()))
        {
            infoSupport.addError("application version", "is mandatory");
        }        
        
        //A simple way to check if a homepage URL is valid
        try
        {            
            if (model.getHomepage() != null && model.getHomepage().length() != 0)
                new URL(model.getHomepage());
        }
        catch (MalformedURLException murle)
        {
            infoSupport.addError("homepage", "is not a valid URL: " + murle.getLocalizedMessage());                            
        }
        
        PropertyValidationSupport localeSupport = new PropertyValidationSupport(model, "Locale");
        
        if (model.getLangCodes().size() == 0)
        {   
            localeSupport.addError("langpacks", "must have at least one language added");
        }
        
        PropertyValidationSupport guiPrefsSupport = new PropertyValidationSupport(model, "GUIPrefs");
        
        //Check for 0 sizes
        //Warn for small sizes
        //TODO find good values for these
        if (model.getWidth() == 0)
        {
            guiPrefsSupport.addError("width", "must not be 0");
        }
        else if (model.getWidth() < 400)
        {
            guiPrefsSupport.addWarning("width",
                            "may be too small to display correctly");
        }
        
        if (model.getHeight() == 0)
        {            
            guiPrefsSupport.addError("height", "must not be 0");        
        }
        else if (model.getHeight() < 400)
        {
            guiPrefsSupport.addWarning("height", "may be too small to display correctly");
        }
        
        ValidationResult vr = new ValidationResult();
        
        vr.addAllFrom(infoSupport.getResult());
        vr.addAllFrom(localeSupport.getResult());
        vr.addAllFrom(guiPrefsSupport.getResult());
        
        return vr;
    }
    
    private GeneralInformationModel model;
}
