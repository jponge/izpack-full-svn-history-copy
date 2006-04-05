/*
 * Created on Mar 31, 2005
 * 
 * $Id: LangLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LangLabel.java 
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

package izpack.frontend.view.renderers;

import izpack.frontend.model.LanguageMap;
import izpack.frontend.model.stages.GeneralInformationModel.LangModel;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

/**
 * @author Andy Gombos
 */
public class LangLabel extends DefaultListCellRenderer
{
    public LangLabel()
    {
        displayLabel = new JLabel("", SwingConstants.LEFT);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof LangModel)
        {
            LangModel model = (LangModel) value;
            displayLabel.setText(langMap.getLanguage(model.iso3Code));
            displayLabel.setIcon(new ImageIcon(model.flag));
            
            if (isSelected)
                displayLabel.setBorder(selected);
            else
                displayLabel.setBorder(unselected);
            
            return displayLabel;
        }        
        
        return null;
    }    
    
    static Border unselected = new SoftBevelBorder(SoftBevelBorder.RAISED);
    static Border selected = new SoftBevelBorder(SoftBevelBorder.LOWERED);
    
    LanguageMap langMap = LanguageMap.getInstance();
    JLabel displayLabel;    
}
