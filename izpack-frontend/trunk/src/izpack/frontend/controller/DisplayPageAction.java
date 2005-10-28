/*
 * $Id: DisplayPageAction.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : DisplayPageAction.java 
 * Description : Display pages within swing applications
 * Author's email : dani@gueggs.net 
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

package izpack.frontend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Display a Page when on Action.
 * 
 * @author Daniel Guggi
 */
public class DisplayPageAction implements ActionListener
{
	/**
	 * The page to display.
	 */
	private String pageName = null;
	
	/**
	 * Setup.
	 * @param name The page name.
	 */
	public DisplayPageAction(String name) {
		this.pageName = name;
	}
	
	/** Display the page with the name <code>this.pageName;</code>. */
	public void actionPerformed(ActionEvent e)
	{
		//IzPackFrame.getInstance().displayPage(pageName);
	}
}