/*
 * Created on May 24, 2005
 * 
 * $Id: GeneralInformationModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : GeneralInformationModel.java 
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

package izpack.frontend.model.stages;

import izpack.frontend.controller.AuthorManager;
import izpack.frontend.model.Author;
import izpack.frontend.model.SelectListModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XML;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;

import exceptions.UnhandleableException;

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
    
    public void resetModel()
    {        
        setAppName("");        
        setHeight(0);
        setWidth(0);
        setHomepage("");        
        setResizable(true);
        setVersion("");
        
        authors.clear();
        
        
        ArrayList authorListData = AuthorManager.loadAuthors();
        if (authorListData != null)
        {
            for (Iterator iter = authorListData.iterator(); iter.hasNext(); )
            {
               authors.add(iter.next());
            }        
        }
        
        langCodes.clear();
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#writeToXML()
     */
    public Element[] writeToXML(Document doc)
    {
        Element genInfoXML = createGeneralInfoPageXML(doc);
        Element langXML = createLanguageXML(doc);
        Element uiXML = createGUIXML(doc);

        return new Element[]{genInfoXML, langXML, uiXML};
    }

    /* (non-Javadoc)
     * @see izpack.frontend.model.stages.StageDataModel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document doc)
    {
        try
        {
            initGeneralInfoXML(doc);
            initLanguageXML(doc);
            initGUIXML(doc);
        }
        catch (XPathExpressionException e)
        {
            throw new UnhandleableException(e);
        }
    }

    private void initGUIXML(Document doc) throws XPathExpressionException
    {
        XPath xpath = XPathFactory.newInstance().newXPath();

        Node guiPrefs = (Node) xpath.evaluate("//guiprefs", doc,
                        XPathConstants.NODE);
        
        NamedNodeMap attributes = guiPrefs.getAttributes();
        
        setWidth(Integer.parseInt(attributes.getNamedItem("width").getNodeValue()));
        setHeight(Integer.parseInt(attributes.getNamedItem("height").getNodeValue()));

        String resizableStr = attributes.getNamedItem("resizable").getNodeValue();
        
        if (resizableStr.equalsIgnoreCase("yes") || resizableStr.equalsIgnoreCase("true"))
            setResizable(true);
        else if (resizableStr.equalsIgnoreCase("no") || resizableStr.equalsIgnoreCase("false"))
            setResizable(false);
        else
            setResizable(true);
    }

    private void initLanguageXML(Document doc) throws XPathExpressionException
    {
        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList langCodes = (NodeList) xpath.evaluate("//locale/langpack", doc,
                        XPathConstants.NODESET);
        
        for (int i = 0; i < langCodes.getLength(); i++)
        {
            String isoCode = langCodes.item(i).getAttributes().getNamedItem("iso3").getNodeValue();
            
            URL location = ClassLoader.getSystemResource("res/imgs/flags/" + isoCode + ".gif");
            
            //Handle a location if we don't know WTF it is
            if (location == null)
            {                
                location = ClassLoader.getSystemResource("res/imgs/flags/unknown.png");                
            }
            
            LangModel lang = new LangModel(isoCode, location);
            
            this.langCodes.addElement(lang);
        }
    }

    private void initGeneralInfoXML(Document doc) throws XPathExpressionException
    {
        XPath xpath = XPathFactory.newInstance().newXPath();

        Node appname = (Node) xpath.evaluate("//info/appname", doc,
                        XPathConstants.NODE);
        if (appname != null) setAppName(appname.getTextContent());

        Node appversion = (Node) xpath.evaluate("//info/appversion", doc,
                        XPathConstants.NODE);
        if (appversion != null) setVersion(appversion.getTextContent());

        Node url = (Node) xpath
                        .evaluate("//info/url", doc, XPathConstants.NODE);
        if (url != null) setHomepage(url.getTextContent());

        NodeList authors = (NodeList) xpath.evaluate("//info/authors/author",
                        doc, XPathConstants.NODESET);
        if (authors == null) return;

        //Remove all authors from the list because we're loading new ones
        this.authors.clear();
        
        for (int i = 0; i < authors.getLength(); i++)
        {
            Node author = authors.item(i);

            NamedNodeMap attributes = author.getAttributes();

            Author authorObj = new Author(attributes.getNamedItem("name")
                            .getNodeValue(), attributes.getNamedItem("email")
                            .getNodeValue());

            this.authors.add(authorObj);
        }
    }

    private Element createGeneralInfoPageXML(Document doc)
    {
        Element info = XML.createElement("info", doc);

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
        
        if (homepage != null && !homepage.equals(""))
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
    private Element createLanguageXML(Document doc)
    {
        Element root = XML.createElement("locale", doc);

        ListModel model = langCodes;

        for (int i = 0; i < model.getSize(); i++)
        {
            Element langElem = XML.createElement("langpack", doc);
            LangModel lang = (LangModel) model.getElementAt(i);
            langElem.setAttribute("iso3", lang.iso3Code);

            root.appendChild(langElem);
        }

        return root;
    }

    /*  
     * Structure
     * <guiprefs resizable="no" width="800" height="600"/>
     */
    private Element createGUIXML(Document doc)
    {
        Element guiprefs = XML.createElement("guiprefs", doc);

        guiprefs.setAttribute("resizable", resizable ? "yes" : "no");

        guiprefs.setAttribute("width", Integer.toString(width));
        guiprefs.setAttribute("height", Integer.toString(height));

        return guiprefs;
    }
    
    public class LangModel
    {
        public LangModel(String iso3Code, URL flag)
        {
            this.iso3Code = iso3Code;
            this.flag = flag;
        }
        
        public String iso3Code;
        public URL flag;
    }
}
