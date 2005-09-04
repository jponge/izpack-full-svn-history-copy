/*
 * Created on May 24, 2005
 * 
 * $Id: GeneralInformationModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : GeneralInformationModel.java 
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
package izpack.frontend.model.stages;

import izpack.frontend.model.Author;
import izpack.frontend.model.SelectListModel;
import izpack.frontend.view.renderers.LangLabel;

import java.util.ArrayList;

import javax.swing.ListModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * @author Andy Gombos
 */
public class GeneralInformationModel extends Model implements StageDataModel
{

    public GeneralInformationModel()
    {
        super();
        authors = new ArrayListModel();
        langCodes = new SelectListModel();
    }

    public String getAppName()
    {
        return appName;
    }

    public ArrayListModel getAuthors()
    {
        return authors;
    }
    public int getHeight()
    {
        return height;
    }

    public String getHomepage()
    {
        return homepage;
    }

    public SelectListModel getLangCodes()
    {
        return langCodes;
    }

    public String getVersion()
    {
        return version;
    }

    public int getWidth()
    {
        return width;
    }

    public boolean isResizable()
    {
        return resizable;
    }

    public void setAppName(String appName)
    {
        System.out.println("Set app name: " + appName);
        String oldAppName = this.appName;
        this.appName = appName;
        firePropertyChange("appName", oldAppName, appName);
    }

    public void setAuthors(ArrayListModel authors)
    {
        ArrayList oldAuthors = this.authors;
        this.authors = authors;
        firePropertyChange("authors", oldAuthors, authors);
    }

    public void setHeight(int height)
    {
        int oldHeight = this.height;
        this.height = height;
        firePropertyChange("height", oldHeight, height);
    }

    public void setHomepage(String homepage)
    {
        System.out.println("Set homepage: " + homepage);
        String oldHomepage = this.homepage;
        this.homepage = homepage;
        firePropertyChange("homepage", oldHomepage, homepage);
    }

    public void setLangCodes(SelectListModel langCodes)
    {
        SelectListModel oldLangCodes = this.langCodes;
        this.langCodes = langCodes;
        firePropertyChange("langCodes", oldLangCodes, langCodes);
    }

    public void setResizable(boolean resizable)
    {
        boolean oldResizable = this.resizable;
        this.resizable = resizable;
        firePropertyChange("resizable", oldResizable, resizable);
    }

    public void setVersion(String version)
    {
        System.out.println("Set version: " + version);
        String oldVersion = this.version;
        this.version = version;
        firePropertyChange("version", oldVersion, version);
    }

    public void setWidth(int width)
    {
        int oldWidth = this.width;
        this.width = width;
        firePropertyChange("width", oldWidth, width);
    }

    /*
     * Fields for model data 
     */

    //GeneralInfoPanel
    String appName, version, homepage;

    ArrayListModel authors;

    //LanguageSelect
    SelectListModel langCodes;

    //GUI Prefs
    boolean resizable;
    int width, height;
    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#writeToXML()
     */
    public Document writeToXML()
    {
        Element root = XML.createRootElement("installation");
        Document rootDoc = root.getOwnerDocument();
        
        root.setAttribute("version", "1.0");        
        
        Element genInfoXML = createGeneralInfoPageXML();
        Element langXML = createLanguageXML();
        Element uiXML = createGUIXML();               
        
        root.appendChild(rootDoc.importNode(genInfoXML, true));
        root.appendChild(rootDoc.importNode(langXML, true));
        root.appendChild(rootDoc.importNode(uiXML, true));
        
        return rootDoc;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document doc)
    {
        // TODO Auto-generated method stub
        
    }
    
    private Element createGeneralInfoPageXML()
    {
        Element info = XML.createRootElement("info");
        Document doc = info.getOwnerDocument();
        
        Element appname = XML.createElement("appname", doc);
        Element appversion = XML.createElement("appversion", doc);
        Element url = XML.createElement("url", doc);
        Element authorsElem = XML.createElement("authors", doc);
        
        appname.setTextContent(appName);
        appversion.setTextContent(version);
        url.setTextContent(homepage);
        
        for (int i = 0; i < this.authors.size(); i++)
        {
            Author auth = (Author) authors.get(i);
            Element author = XML.createElement("author", doc);
            author.setAttribute("name", auth.getName());
            author.setAttribute("email", auth.getEmail());
            
            authorsElem.appendChild(author);
        }
        
        info.appendChild(appname);
        info.appendChild(appversion);
        info.appendChild(url);
        info.appendChild(authorsElem);
        
        return info;
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#createXML()
     * 
     * Structure:
     * <locale>
     * 	<langpack iso3="eng" />
     * </locale>
     */
    private Element createLanguageXML()
    {
        Element root = XML.createRootElement("locale");
        Document rootDoc = root.getOwnerDocument();
        
        ListModel model = langCodes;
        
        for (int i = 0; i < model.getSize(); i++)
        {
            Element langElem = XML.createElement("langpack", rootDoc);
            LangLabel lLabel = (LangLabel) model.getElementAt(i);
            langElem.setAttribute("iso3", lLabel.getISO3Code());
            
            root.appendChild(langElem);
        }
        
        return root;
    }
    
    /*  
     * Structure
     * <guiprefs resizable="no" width="800" height="600"/>
     */
    private Element createGUIXML()
    {
        Document doc = XML.getDocument();
        Element guiprefs = doc.createElement("guiprefs");
                
        guiprefs.setAttribute("resizable", resizable ? "yes" : "no");        
        
        guiprefs.setAttribute("width", Integer.toString(width));
        guiprefs.setAttribute("height", Integer.toString(height));        
        
        return guiprefs;
    }
}
