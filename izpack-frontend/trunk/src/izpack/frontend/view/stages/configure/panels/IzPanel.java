/*
 * $Id: Page.java Feb 8, 2004 izpack-frontend Copyright (C) 2001-2003
 * IzPack Development Group
 * 
 * File : Page.java 
 * Description : Interface that specifies how a Page has to look like
 * applications. Author's email : dani@gueggs.net
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

import java.util.Observer;


/**
 * Defines how pages have to look like.
 * 
 * @author Daniel Guggi
 */
public interface IzPanel extends Observer {
	/**
	 * Add a element to the private elements collection.
	 * @param name The name of the element.
	 * @param elem The element itself.
	 * @return The element.
	 */
    public Object addElement(String name, Object elem);

    
    /**
     * Get an element by its name.
     * @param name The name of the Element.
     * @return The Element.
     */
    public Object getElement(String name);

    /**
     * Initialize the page.
     */
    public void initComponents();

    /**
     * Set the name for this page.
     * @param name The name.
     */
    public void setName(String name);
    
    /**
     * Retrieve the name for this page.
     * @return The name of the page.
     */
    public String getName();    
}





