/*
 * Created on Mar 30, 2005
 * 
 * $Id: LanguageSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LanguageSelect.java 
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

import izpack.frontend.model.LanguageMap;
import izpack.frontend.model.SelectListModel;
import izpack.frontend.model.stages.GeneralInformationModel;
import izpack.frontend.model.stages.GeneralInformationModel.LangModel;
import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.SelectList;
import izpack.frontend.view.renderers.LangLabel;
import izpack.frontend.view.stages.IzPackStage;

import java.net.URL;
import java.util.Iterator;

import javax.swing.ListModel;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;

/**
 * @author Andy Gombos
 */
public class LanguageSelect extends AbstractListSelect
{
    public LanguageSelect(IzPackStage parentStage)
    {
        super();
        
        src = new SelectList();        
        dest = new SelectList();
        
        model = (GeneralInformationModel) parentStage.getDataModel();
        
        src.setCellRenderer(new LangLabel());
        dest.setCellRenderer(new LangLabel());        
                
        //Bind the model to the list
        SelectionInList sil = new SelectionInList((ListModel) 
                        model.getLangCodes());        
        Bindings.bind(dest, sil);
        
        initSrcList();
        
        initLists(src, dest);
    }    
    
    public void initSrcList()
	{
        LanguageMap langMap = LanguageMap.getInstance();
        
        for (Iterator iter = langMap.keySet().iterator(); iter.hasNext();)
        {
            String isoCode = (String) iter.next();
            
            URL location = ClassLoader.getSystemResource("res/imgs/flags/" + isoCode + ".gif");
            
            LangModel lang = model.new LangModel(isoCode, location);            
            
            ( (SelectListModel) src.getModel() ).addElement(lang);
        }
	}    
        
    SelectList src, dest;
    
    GeneralInformationModel model;
}
