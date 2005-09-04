/*
 * Created on May 24, 2005
 * 
 * $Id: GeneralInfoValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : GeneralInfoValidator.java 
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
package izpack.frontend.controller.validators;

import izpack.frontend.model.stages.GeneralInformationModel;

import java.net.MalformedURLException;
import java.net.URL;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationCapable;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

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
