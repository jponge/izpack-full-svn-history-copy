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

import java.util.ArrayList;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * @author Andy Gombos
 */
public class GeneralInformationModel extends Model
{

    public GeneralInformationModel()
    {
        super();
        authors = new ArrayListModel();
        langCodes = new ArrayListModel();
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

    public ArrayListModel getLangCodes()
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

    public void setLangCodes(ArrayListModel langCodes)
    {
        ArrayListModel oldLangCodes = this.langCodes;
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
    ArrayListModel langCodes;

    //GUI Prefs
    boolean resizable;
    int width, height;
}
