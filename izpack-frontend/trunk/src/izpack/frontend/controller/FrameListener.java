/*
 * $Id: FrameListener.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FrameListener.java 
 * Description : FrameListener for swing application.
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
