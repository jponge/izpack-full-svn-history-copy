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

import izpack.frontend.view.components.AbstractListSelect;
import izpack.frontend.view.components.LangLabel;
import izpack.frontend.view.components.SelectList;

import java.net.URL;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

/**
 * @author Andy Gombos
 */
public class LanguageSelect extends AbstractListSelect
{
    public LanguageSelect()
    {
        super();
        
        src = new SelectList();
        dest = new SelectList();
        
        langMap = new TreeMap();
        initLangMap();
        
        initSrcList();
        
        initLists(src, dest);
    }
    
    public void initSrcList()
	{
        for (Iterator iter = langMap.keySet().iterator(); iter.hasNext();)
        {
            String isoCode = (String) iter.next();
            
            URL location = ClassLoader.getSystemResource("res/imgs/flags/" + isoCode + ".gif");
            
            LangLabel langLabel = new LangLabel((String) langMap.get(isoCode), new ImageIcon(location), SwingConstants.LEFT);            
            langLabel.setISO3Code(isoCode);
            
            src.addElement(langLabel);
        }
	}
    
    /*
     * Values from the IzPack documentation
     */
    private void initLangMap()
    {
        langMap.put("cat", "Catalunyan");
        langMap.put("dan", "Danish");
        langMap.put("deu", "German");
        langMap.put("eng", "English");
        langMap.put("fin", "Finnish");
        langMap.put("fra", "French");
        langMap.put("hun", "Hungarian");
        langMap.put("ita", "Italian");
        langMap.put("jpn", "Japanese");
        langMap.put("mys", "Malaysian");
        langMap.put("ned", "Nederlands");
        langMap.put("pol", "Polish");
        langMap.put("por", "Portuguese (Brazilian)");
        langMap.put("rom", "Romanian");
        langMap.put("rus", "Russian");
        langMap.put("spa", "Spanish");
        langMap.put("svk", "Slovakian");
        langMap.put("swe", "Swedish");                                                                                                                                                                                                                                                                                        langMap.put("ukr", "Ukrainian");
    }
    
    SelectList src, dest;
    TreeMap langMap;
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#createXML()
     * 
     * Structure:
     * <locale>
     * 	<langpack iso3="eng" />
     * </locale>
     */
    public Element createXML()
    {
        Element root = XML.createElement("locale");
        ListModel model = dest.getModel();
        
        for (int i = 0; i < model.getSize(); i++)
        {
            Element langElem = XML.createElement("langpack");
            LangLabel lLabel = (LangLabel) model.getElementAt(i);
            langElem.setAttribute("iso3", lLabel.getISO3Code());
            
            root.appendChild(langElem);
        }
        
        return root;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }
}
