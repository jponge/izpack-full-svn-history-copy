/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2003 Jonathan Halliday, Julien Ponge
 *
 *  File :               CompilePanelAutomationHelper.java
 *  Description :        Automation support functions for CompilePanel.
 *  Author's email :     jonathan.halliday@arjuna.com
 *  Author's Website :   http://www.arjuna.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.izforge.izpack.panels;

import net.n3.nanoxml.XMLElement;
import com.izforge.izpack.installer.*;

/**
 * Functions to support automated usage of the CompilePanel
 *
 * @author Jonathan Halliday
 */
public class CompilePanelAutomationHelper implements PanelAutomation
{
	// state var for thread sync.
	private boolean done = false;

	/**
	 * Null op - this panel type has no state to serialize.
	 *
	 * @param installData unused.
	 * @param panelRoot unused.
	 */
	public void makeXMLData(AutomatedInstallData installData, XMLElement panelRoot)
	{
		// do nothing.
	}

	/**
	 *  Perform the installation actions.
	 *
	 * @param panelRoot The panel XML tree root.
	 */
	public void runAutomated(AutomatedInstallData idata, XMLElement panelRoot)
	{
    System.out.println ("CompilePanel doesn't yet support automated installation!");
	}

	/**
	 * Reports progress on System.out
	 *
	 * @see InstallListener#startUnpack
	 */
	public void startUnpack()
	{
	}

	/**
	 * Reports the error to System.err
	 *
	 * @param error
	 * @see InstallListener#errorUnpack
	 */
	public void errorUnpack(String error)
	{
	}

	/**
	 * Sets state variable for thread sync.
	 *
	 * @see InstallListener#stopUnpack
	 */
	public void stopUnpack()
	{
		done = true;
	}

	/**
	 * Null op.
	 *
	 * @param val
	 * @param msg
	 * @see InstallListener#progressUnpack
	 */
	public void progressUnpack(int val, String msg)
	{
		// silent for now. sould log individual files here, if we had a verbose mode?
	}

	/**
	 * Reports progress to System.out
	 *
	 * @param min unused
	 * @param max unused
	 * @param packName The currently installing pack.
	 * @see InstallListener#changeUnpack
	 */
	public void changeUnpack(int min, int max, String packName)
	{
	}
}
