/*
 * IzPack - Copyright 2001-2006 Julien Ponge, All Rights Reserved.
 * 
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 * 
 * Copyright 2003 Jonathan Halliday
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

package com.izforge.izpack.installer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.n3.nanoxml.NonValidator;
import net.n3.nanoxml.StdXMLBuilder;
import net.n3.nanoxml.StdXMLParser;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;

import com.izforge.izpack.ExecutableFile;
import com.izforge.izpack.LocaleDatabase;
import com.izforge.izpack.Panel;
import com.izforge.izpack.util.Housekeeper;
import com.izforge.izpack.util.OsConstraint;

/**
 * Runs the install process in text only (no GUI) mode.
 * 
 * @author Jonathan Halliday <jonathan.halliday@arjuna.com>
 * @author Julien Ponge <julien@izforge.com>
 * @author Johannes Lehtinen <johannes.lehtinen@iki.fi>
 */
public class AutomatedInstaller extends InstallerBase
{

    // there are panels which can be instantiated multiple times
    // we therefore need to select the right XML section for each
    // instance
    private TreeMap panelInstanceCount;

    /** The automated installation data. */
    private AutomatedInstallData idata = new AutomatedInstallData();

    /** The result of the installation. */
    private boolean result = false;
    
    /**
     * Constructing an instance triggers the install.
     * 
     * @param inputFilename Name of the file containing the installation data.
     * @exception Exception Description of the Exception
     */
    public AutomatedInstaller(String inputFilename) throws Exception
    {
        super();

        File input = new File(inputFilename);

        // Loads the installation data
        loadInstallData(this.idata);

        // Loads the xml data
        this.idata.xmlData = getXMLData(input);

        // Loads the langpack
        this.idata.localeISO3 = this.idata.xmlData.getAttribute("langpack", "eng");
        InputStream in = getClass().getResourceAsStream("/langpacks/" + this.idata.localeISO3 + ".xml");
        this.idata.langpack = new LocaleDatabase(in);
        this.idata.setVariable(ScriptParser.ISO3_LANG, this.idata.localeISO3);

        // create the resource manager singleton
        ResourceManager.create(this.idata);

        // Load custom langpack if exist.
        addCustomLangpack(this.idata);

        this.panelInstanceCount = new TreeMap();
    }

    /**
     * Writes the uninstalldata.
     * 
     * Unfortunately, Java doesn't allow multiple inheritance, so <code>AutomatedInstaller</code>
     * and <code>InstallerFrame</code> can't share this code ... :-/
     * 
     * TODO: We should try to fix this in the future.
     */
    private boolean writeUninstallData()
    {
        try
        {
            // We get the data
            UninstallData udata = UninstallData.getInstance();
            List files = udata.getFilesList();
            ZipOutputStream outJar = this.idata.uninstallOutJar;

            if (outJar == null) 
                return true; // it is allowed not to have an installer

            System.out.println("[ Writing the uninstaller data ... ]");

            // We write the files log
            outJar.putNextEntry(new ZipEntry("install.log"));
            BufferedWriter logWriter = new BufferedWriter(new OutputStreamWriter(outJar));
            logWriter.write(this.idata.getInstallPath());
            logWriter.newLine();
            Iterator iter = files.iterator();
            while (iter.hasNext())
            {
                logWriter.write((String) iter.next());
                if (iter.hasNext()) logWriter.newLine();
            }
            logWriter.flush();
            outJar.closeEntry();

            // We write the uninstaller jar file log
            outJar.putNextEntry(new ZipEntry("jarlocation.log"));
            logWriter = new BufferedWriter(new OutputStreamWriter(outJar));
            logWriter.write(udata.getUninstallerJarFilename());
            logWriter.newLine();
            logWriter.write(udata.getUninstallerPath());
            logWriter.flush();
            outJar.closeEntry();

            // Write out executables to execute on uninstall
            outJar.putNextEntry(new ZipEntry("executables"));
            ObjectOutputStream execStream = new ObjectOutputStream(outJar);
            iter = udata.getExecutablesList().iterator();
            execStream.writeInt(udata.getExecutablesList().size());
            while (iter.hasNext())
            {
                ExecutableFile file = (ExecutableFile) iter.next();
                execStream.writeObject(file);
            }
            execStream.flush();
            outJar.closeEntry();

            // Cleanup
            outJar.flush();
            outJar.close();
            return true;
        }
        catch (Exception err)
        {
            err.printStackTrace();
            return false;
        }
    }

    /**
     * Runs the automated installation logic for each panel in turn.
     * 
     * @throws Exception
     */
    protected void doInstall() throws Exception
    {
        // TODO: i18n
        System.out.println("[ Starting automated installation ]");

        try
        {
            // assume that installation will succeed
            this.result = true;
            
            // walk the panels in order
            Iterator panelsIterator = this.idata.panelsOrder.iterator();
            while (panelsIterator.hasNext())
            {
                Panel p = (Panel) panelsIterator.next();
                String praefix = "com.izforge.izpack.panels.";
                if (p.className.compareTo(".") > -1)
                // Full qualified class name
                    praefix = "";
                if (!OsConstraint.oneMatchesCurrentSystem(p.osConstraints)) continue;
    
                String panelClassName = p.className;
                String automationHelperClassName = praefix + panelClassName + "AutomationHelper";
                Class automationHelperClass = null;
                // determine if the panel supports automated install
                try
                {
                    automationHelperClass = Class.forName(automationHelperClassName);
                }
                catch (ClassNotFoundException e)
                {
                    // this is OK - not all panels have/need automation support.
                    continue;
                }
    
                // instantiate the automation logic for the panel
                PanelAutomation automationHelperInstance = null;
                if (automationHelperClass != null)
                {
                    try
                    {
                        automationHelperInstance = (PanelAutomation) automationHelperClass
                                .newInstance();
                    }
                    catch (Exception e)
                    {
                        System.err.println("ERROR: no default constructor for "
                                + automationHelperClassName + ", skipping...");
                        continue;
                    }
                }
    
                // We get the panels root xml markup
                Vector panelRoots = this.idata.xmlData.getChildrenNamed(panelClassName);
                int panelRootNo = 0;
    
                if (this.panelInstanceCount.containsKey(panelClassName))
                {
                    // get number of panel instance to process
                    panelRootNo = ((Integer) this.panelInstanceCount.get(panelClassName)).intValue();
                }
    
                XMLElement panelRoot = (XMLElement) panelRoots.elementAt(panelRootNo);
    
                this.panelInstanceCount.put(panelClassName, new Integer(panelRootNo + 1));
    
                // execute the installation logic for the current panel, if it has
                // any:
                if (automationHelperInstance != null)
                {
                    try
                    {
                        if (! automationHelperInstance.runAutomated(this.idata, panelRoot))
                        {
                            // make installation fail instantly
                            this.result = false;
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        System.err.println("ERROR: automated installation failed for panel "
                                + panelClassName);
                        e.printStackTrace();
                        this.result = false;
                        continue;
                    }
    
                }
    
            }
    
            // this does nothing if the uninstaller was not included
            writeUninstallData();
    
            if (this.result)
                System.out.println("[ Automated installation done ]");
            else
                System.out.println("[ Automated installation FAILED! ]");
        }
        catch (Exception e)
        {
            this.result = false;
            System.err.println(e.toString());
            e.printStackTrace();
            System.out.println("[ Automated installation FAILED! ]");
        }
        finally
        {    
            // Bye
            Housekeeper.getInstance().shutDown(this.result ? 0 : 1);
        }
    }

    /**
     * Loads the xml data for the automated mode.
     * 
     * @param input The file containing the installation data.
     * 
     * @return The root of the XML file.
     * 
     * @exception Exception thrown if there are problems reading the file.
     */
    public XMLElement getXMLData(File input) throws Exception
    {
        FileInputStream in = new FileInputStream(input);

        // Initialises the parser
        StdXMLParser parser = new StdXMLParser();
        parser.setBuilder(new StdXMLBuilder());
        parser.setReader(new StdXMLReader(in));
        parser.setValidator(new NonValidator());

        XMLElement rtn = (XMLElement) parser.parse();
        in.close();

        return rtn;
    }
    
    /**
     * Get the result of the installation.
     * 
     * @return True if the installation was successful.
     */
    public boolean getResult()
    {
        return this.result;
    }
}
