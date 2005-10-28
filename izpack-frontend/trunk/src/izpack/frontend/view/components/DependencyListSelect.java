/*
 * Created on Apr 27, 2005
 * 
 * $Id: DependencyListSelect.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : DependencyListSelect.java 
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

package izpack.frontend.view.components;

import izpack.frontend.model.PackModel;
import izpack.frontend.model.SelectListModel;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.validation.ValidationResult;

/**
 * @author Andy Gombos
 */
public class DependencyListSelect extends AbstractListSelect
{
    public DependencyListSelect(ArrayList packs)
    {
        super();
        
        src = new SelectList();
        dest = new SelectList();
        
        this.packs = packs;
        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
        for (Iterator iter = packs.iterator(); iter.hasNext();)
        {
            PackModel pm = (PackModel) iter.next();
            ( (SelectListModel) src.getModel() ).addElement(pm.getName());
        }
    }
    
    SelectList src, dest;
    ArrayList packs;
    
    /*
     *	Not implemented... 
     */
    public Element createXML()
    {        
        return null;
    }

    public void initFromXML(Document xmlFile)
    {
    }
    
    public ValidationResult validatePanel()
    {
        return null;
    }
}
