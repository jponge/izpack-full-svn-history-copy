/*
 * Created on Apr 18, 2005
 * 
 * $Id: PackFileModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackFileModel.java 
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

package izpack.frontend.model.files;

import izpack.frontend.model.OS;

/**
 * @author Andy Gombos
 */
public abstract class PackFileModel implements PackElement
{
    public String target = "", source = "", override = "";
    public OS os;
}
