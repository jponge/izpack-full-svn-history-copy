/*
 * $Id: DisplayPageAction.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : DisplayPageAction.java 
 * Description : Display pages within swing applications
 * Author's email : dani@gueggs.net 
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