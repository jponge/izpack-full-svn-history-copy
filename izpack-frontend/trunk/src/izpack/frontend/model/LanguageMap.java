/*
 * Created on Sep 6, 2005
 * 
 * $Id: LanguageMap.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : LanguageMap.java 
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
package izpack.frontend.model;

import java.util.TreeMap;

public class LanguageMap extends TreeMap<String, String>
{
    /*
     * Values from the IzPack documentation
     */

    private LanguageMap()
    {
        put("cat", "Catalunyan");
        put("dan", "Danish");
        put("deu", "German");
        put("eng", "English");
        put("fin", "Finnish");
        put("fra", "French");
        put("hun", "Hungarian");
        put("ita", "Italian");
        put("jpn", "Japanese");
        put("mys", "Malaysian");
        put("ned", "Nederlands");
        put("pol", "Polish");
        put("por", "Portuguese (Brazilian)");
        put("rom", "Romanian");
        put("rus", "Russian");
        put("spa", "Spanish");
        put("svk", "Slovakian");
        put("swe", "Swedish");
        put("ukr", "Ukrainian");

    }

    public static LanguageMap getInstance()
    {
        return instance;
    }

    private static LanguageMap instance = new LanguageMap();
}
