/*
 * Created on Mar 30, 2005
 * 
 * $Id: LanguageSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LanguageSelect.java 
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
