/*
 * Created on Sep 6, 2005
 * 
 * $Id: LanguageMap.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LanguageMap.java 
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
