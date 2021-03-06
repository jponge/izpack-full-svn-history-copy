/*
 * Created on Jun 14, 2005
 * 
 * $Id: PanelSelectionValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PanelSelectionValidator.java 
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

import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.stages.PanelModel;
import izpack.frontend.model.stages.PanelSelectionModel;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;

/**
 * @author Andy Gombos
 */
public class PanelSelectionValidator implements StageValidator
{
    public PanelSelectionValidator(PanelSelectionModel model)
    {
        this.model = model;
    }
    /* (non-Javadoc)
     * @see com.jgoodies.validation.ValidationCapable#validate()
     */
    public ValidationResult validate()
    {
        ValidationResult vr = new ValidationResult();
        
        //Check for no panels added
        if (model.size() == 0)
        {   
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have at least one panel selected",
                            model,
                            "Panels",
                            "dest"
                            ));
        }                
        
        tmp.configData = new PanelInfo("", "", "Install Panel", "", "", null, null);        
        
        if (!model.contains(tmp))
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have an installation panel to perform the install",
                            model,
                            "Panels",
                            "dest"));
        }
        
        tmp.configData = new PanelInfo("", "", "Welcome Panel", "", "", null, null);        
        
        if (!model.contains(tmp))
        {
            vr.add(new PropertyValidationMessage(
                            Severity.WARNING,
                            "should have a welcome panel",
                            model,
                            "Panels",
                            "dest"));
        }
        
        tmp.configData = new PanelInfo("", "", "Finish Panel", "", "", null, null);        
        tmp2.configData = new PanelInfo("", "", "Simple Finish Panel", "", "", null, null);
        
        if (! (model.contains(tmp) || model.contains(tmp2)) )
        {
            vr.add(new PropertyValidationMessage(
                            Severity.WARNING,
                            "should have a finishing panel",
                            model,
                            "Panels",
                            "dest"));
        }
        
        return vr;
    }

    private PanelModel tmp = new PanelModel(), tmp2 = new PanelModel();
    PanelSelectionModel model;
}
