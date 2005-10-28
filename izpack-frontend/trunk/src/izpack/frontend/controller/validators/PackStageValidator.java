/*
 * Created on Jun 14, 2005
 * 
 * $Id: PackStageValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackStageValidator.java 
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

import izpack.frontend.model.PackModel;
import izpack.frontend.model.stages.PackStageModel;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;

/**
 * @author Andy Gombos
 */
public class PackStageValidator
{
    public PackStageValidator(PackStageModel model)
    {
        this.model = model;
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.IzPackStage#validateStage()
     */
    public ValidationResult validate()
    {   
        ValidationResult vr = new ValidationResult();        
        
        int lastRow = 0;
        
        for (int i = 0; i < model.getRowCount(); i++)
        {
            if (model.getValueAt(i, 0) == null)
            {
                lastRow = --i;
                break;
            }
        }
        
        if (lastRow < 0)
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "must have at least one pack created",
                            model,
                            "Packs",
                            "table"
                            ));      
        }
        
        for (int row = 0; row < model.getRowCount(); row++)
        {
            Object rowObj = model.getValueAt(row, 0);
            
            if (rowObj == null)
                continue;
            
            PackModel pm = (PackModel) rowObj;            
          
            
            for (int i = 0; i < model.getRowCount(); i++)
            {
                if (pm.getFilesModel().getValueAt(i, 0) == null)
                {
                    lastRow = --i;
                    break;
                }
            }
            
            //TODO Make a better error message
	        if (lastRow < 0)
	        {
	            vr.add(new PropertyValidationMessage(
	                            Severity.ERROR,
	                            "must have at least one file added",
	                            model,
	                            "Pack(#" + ( row + 1) + ")",
	                            "files"
	                            ));      
	        }
        }
        
        return vr;
    }
    
    private PackStageModel model;
}
