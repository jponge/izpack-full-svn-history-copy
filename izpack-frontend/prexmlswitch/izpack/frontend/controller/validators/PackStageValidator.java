/*
 * Created on Jun 14, 2005
 * 
 * $Id: PackStageValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PackStageValidator.java 
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
