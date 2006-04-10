/*
 * Created on Aug 29, 2005
 * 
 * $Id: NoEditingNecessary.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : NoEditingNecessary.java 
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

package izpack.frontend.view.stages.configure.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NoEditingNecessary extends JPanel implements ConfigurePanel
{
    public NoEditingNecessary()
    {
        add(new JLabel("No configuration is necessary for this panel"));
    }
    
    public Element[] createXML(Document doc)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }
}
