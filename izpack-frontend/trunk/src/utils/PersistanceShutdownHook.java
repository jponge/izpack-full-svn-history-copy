/*
 * Created on Oct 15, 2005
 * 
 * $Id: PersistanceShutdownHook.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PersistanceShutdownHook.java 
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

package utils;

import izpack.frontend.controller.AuthorManager;
import izpack.frontend.controller.RecentFileManager;

import java.io.IOException;

public class PersistanceShutdownHook extends Thread
{
    @Override
    public void run()
    {
        try
        {
            AuthorManager.writeAuthors();
            RecentFileManager.getInstance().saveRecentFiles();
        }
        catch (IOException e)
        {
            //Ignore, not critical files
        }
        
    }
}
