/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001-2003 Jonathan Halliday, Julien Ponge
 *
 *  File :               AutomatedInstaller.java
 *  Description :        The silent (headless) installer.
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
package com.izforge.izpack.installer;

import com.izforge.izpack.LocaleDatabase;
import com.izforge.izpack.util.Housekeeper;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Iterator;

import net.n3.nanoxml.*;

/**
 *  Runs the install process in text only (no GUI) mode.
 *
 * @author Jonathan Halliday <jonathan.halliday@arjuna.com>
 * @author Julien Ponge <julien@izforge.com>
 * @author Johannes Lehtinen <johannes.lehtinen@iki.fi>
 */
public class AutomatedInstaller extends InstallerBase
{
	/**
	 *  Constructing an instance triggers the install.
	 *
	 * @param inputFilename Name of the file containing the installation data.
	 * @exception Exception Description of the Exception
	 */
	public AutomatedInstaller(String inputFilename) throws Exception
	{
		super();

		File input = new File(inputFilename);

		// Loads the installation data
		AutomatedInstallData idata = new AutomatedInstallData();

		// Loads the installation data
		loadInstallData(idata);

		// Loads the xml data
		idata.xmlData = getXMLData(input);

		// Loads the langpack
		idata.localeISO3 = idata.xmlData.getAttribute("langpack");
		InputStream in = getClass().getResourceAsStream("/langpacks/" +
			idata.localeISO3 +
			".xml");
		LocaleDatabase langpack = new LocaleDatabase(in);

		doInstall(langpack, idata);
	}

  /**
	 * Runs the automated installation logic for each panel in turn.
	 *
	 * @param langpack currently unused, could support e.g. locale specific progress msgs.
	 * @param installdata the installation data.
	 * @throws Exception
	 */
  private void doInstall(LocaleDatabase langpack, AutomatedInstallData installdata)
     throws Exception
  {
		System.out.println("[ Starting automated installation ]");

		// walk the panels in order
		Iterator panelsIterator = installdata.panelsOrder.iterator();
		while(panelsIterator.hasNext())
    {
			String panelClassName = (String)panelsIterator.next();
			String automationHelperClassName = "com.izforge.izpack.panels."+panelClassName+"AutomationHelper";
			Class automationHelperClass = null;
			// determine if the panel supports automated install
			try {
				automationHelperClass = Class.forName(automationHelperClassName);
			} catch(ClassNotFoundException e) {
				// this is OK - not all panels have/need automation support.
			}

			// instantiate the automation logic for the panel
			PanelAutomation automationHelperInstance = null;
			if(automationHelperClass != null) {
				try {
				  automationHelperInstance = (PanelAutomation)automationHelperClass.newInstance();
			  } catch(Exception e) {
					System.err.println("ERROR: no default constructor for "+automationHelperClassName+", skipping...");
				}
			}

			// We add the XML data panel root
			XMLElement panelRoot = new XMLElement(panelClassName);
			installdata.xmlData.addChild(panelRoot);
			// We get its root xml markup
			panelRoot = installdata.xmlData.getFirstChildNamed(panelClassName);

			// execute the installation logic for the current panel, if it has any:
			if(automationHelperInstance != null) {
				try {
					automationHelperInstance.runAutomated(installdata, panelRoot);
				} catch(Exception e) {

				}
			}
    }

    System.out.println("[ Automated installation done ]");

    // Bye
    Housekeeper.getInstance().shutDown(0);
  }

	/**
   *  Loads the xml data for the automated mode.
   *
   * @param  input          The file containing the installation data.
   * @exception  Exception  thrown if there are problems reading the file.
   */
  public XMLElement getXMLData(File input) throws Exception
  {
    FileInputStream in = new FileInputStream(input);

    // Initialises the parser
    StdXMLParser parser = new StdXMLParser();
    parser.setBuilder(new StdXMLBuilder());
    parser.setReader(new StdXMLReader(in));
    parser.setValidator(new NonValidator());

    XMLElement rtn = (XMLElement)parser.parse();
    in.close();

    return rtn;
  }
}
