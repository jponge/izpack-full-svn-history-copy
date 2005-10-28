/*
 * Created on Nov 30, 2004
 * 
 * $Id: Licence.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : Licence.java 
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

import izpack.frontend.controller.LicenseLoader;
import izpack.frontend.model.LicenseModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.UI;
import utils.XML;

import com.jgoodies.validation.ValidationResult;

/**
 * Configure the license displayed for a LicensePanel
 * 
 * TODO - allow 'Custom' selection to load from a file, instead of copy and paste
 * 
 * @author Andy Gombos
 */
public class License extends JPanel implements ConfigurePanel
{
    public License()
    {
        initComboBox();
	    licenseDisp = new JTextArea();
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.getViewport().add(licenseDisp);
	    scrollPane.setPreferredSize(new Dimension(500, 350));
	    licenseDisp.setWrapStyleWord(true);	    
	    
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    add(licenseList);
	    add(Box.createVerticalStrut(15));
	    add(scrollPane);
	    
	    licenseDisp.setText("Select a license from the above, or load your own. \n You can also edit the provided licenses in this box.");
	    
	    licenseList.addActionListener(new ActionListener() 
	    {	        
		    /* (non-Javadoc)
	         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	         */
		    public void actionPerformed(ActionEvent e)
	        {
		        JComboBox licBox = (JComboBox) e.getSource();
	            Object o = licBox.getSelectedItem();
	             
	             if (o instanceof String && !initFromXML)
	             {	                 	           
	                 if ( ((String) o).equals("<html><font size='+1'>Custom</font>") )
	                 {
	                     //TODO implement saving licenses, with a custom delete component here
	                     // Custom - My license file             [x]
	                     
	                     //licenseDisp.setText("Paste your own license here");
	                     File license = UI.getFile(licenseDisp, "license");
	                     LicenseModel customModel = new LicenseModel();
	                     customModel.filename = license.getAbsolutePath();
	                     customModel.name = license.getName();
	                     
	                     //If license loaded successfully, put it in the combo box
	                     if(loadLicense(license))
	                         licBox.addItem(customModel);
	                 }
	                 else
	                 {	                 
	                     licBox.setSelectedIndex(0);
	                     licenseDisp.setText("License loading failed. Please choose another license, or select one from the above list.");
	                 }
	             }
	             
	             if (o instanceof LicenseModel && !initFromXML)
	             {
	                 LicenseModel lm = (LicenseModel) o;
	                 File license = new File("res/licenses/" + lm.filename);
	             	 loadLicense(license);	                 
	             }
	        }
        });
    }
    
    private void initComboBox()
    {
        licenses = LicenseLoader.loadLicences();
	    licenseList = new JComboBox();
	
        licenseList.addItem("<html><font size='+1'><u>GPL Compatible</u></font>");        
	    
	    for (LicenseModel license : licenses)
        {
            if (license.gplCompatible)
                licenseList.addItem(license);
        }	    
	    
	    licenseList.addItem("<html><font size='+1'><u>GPL Incompatible</u></font>");
	    
        for (LicenseModel license : licenses)
	    {
            if (!license.gplCompatible)
                licenseList.addItem(license);
	    }	    
	    
	    licenseList.addItem("<html><font size='+1'>Custom</font>");
    }
    
    private JComboBox licenseList;
    private JTextArea licenseDisp;
    private ArrayList<LicenseModel> licenses;
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#createXML()
     */
    public Element createXML(Document doc)
    {
        LicenseModel lic = (LicenseModel) licenseList.getSelectedItem();
        
        return XML.createResourceTree("LicencePanel.licence", lic.filename, doc);        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        System.out.println("Configuring from XML");
        initFromXML = true;
        
        String src = XML.getResourceValue(xmlFile, "LicencePanel.licence");
        
        //We have no value to initialize from. Might as well use the default values for the editor
        if (src == null)
        {            
            initFromXML = false;
            return;
        }
        
        if (!loadLicense(new File(src)))
        {
            licenseDisp.setText("Unable to load the license specified in the install file. Please choose one from the above list," + 
                            "or custom to choose your own.");
        }
        
        //Choose the right ComboBox option, or custom if we can't find a distributed license
        for (LicenseModel license : licenses)
        {
            if (license.filename.equals(src))
                licenseList.setSelectedItem(license);                
        }
        
        if ( !(licenseList.getSelectedItem() instanceof LicenseModel) )
        {
            //Select Custom heading
            LicenseModel loadedLicense = new LicenseModel();
            loadedLicense.filename = src;
            loadedLicense.name = "Custom license from XML file (" + new File(src).getName() + ")";
            licenseList.addItem(loadedLicense);
            licenseList.setSelectedItem(loadedLicense);            
        }
        
        initFromXML = false;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#validatePage()
     */
    public ValidationResult validatePanel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param f
     */
    private boolean loadLicense(File f)
    {
        try
        {
            //Read license file into JTextArea
             BufferedReader br = new BufferedReader(new FileReader(f));
             StringBuffer buf = new StringBuffer(1000);
             String tmp;
             
            while ( (tmp = br.readLine()) != null)
            {
                buf.append(tmp + "\n");
            }
            
            licenseDisp.setText(buf.toString());
            licenseDisp.setCaretPosition(0);
            
            return true;
        }
        catch (FileNotFoundException fnfe)
        {
            licenseDisp.setText(fnfe.getLocalizedMessage());
        }
        catch (IOException ioe)
        {
            licenseDisp.setText(ioe.getLocalizedMessage());            
        }
        
        return false;
    }
    
    private boolean initFromXML = false;
}
