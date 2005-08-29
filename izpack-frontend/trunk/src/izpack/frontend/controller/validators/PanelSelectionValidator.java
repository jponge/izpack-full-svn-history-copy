/*
 * Created on Jun 14, 2005
 * 
 * $Id: PanelSelectionValidator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelectionValidator.java 
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
        
        return vr;
    }

    PanelSelectionModel model;
}
