/*
 * $Id: FrameListener.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FrameListener.java 
 * Description : FrameListener for swing application.
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


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * FrameListener to shutdown the application.
 * 
 * @author Daniel Guggi
 */
public class FrameListener implements WindowListener {
	/**
	 * Blank
	 */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * Blank
	 */
	public void windowClosed(WindowEvent e) {

	}

	/**
	 * Exits applications
     * 
     * Do things like save application state here
	 */
	public void windowClosing(WindowEvent e) {
	    try
        {
            RecentFileManager.getInstance().saveRecentFiles();
        }
        catch (IOException e1)
        {
            //Ignore, because saving the recent files isn't worth displaying to the user
            //Maybe disk space is full, or no permissions, or something
        }	    	    
	    
		System.exit(0);
	}

	/**
	 * Blank
	 */
	public void windowDeiconified(WindowEvent e) {

	}

	/**
	 * Blank
	 */
	public void windowIconified(WindowEvent e) {

	}

	/**
	 * Blank
	 */
	public void windowOpened(WindowEvent e) {

	}

	/**
	 * Blank
	 */
	public void windowDeactivated(WindowEvent e) {		
	}

}
