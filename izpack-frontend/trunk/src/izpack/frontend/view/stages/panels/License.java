/*
 * Created on Nov 30, 2004
 * 
 * $Id: Licence.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Licence.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
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
package izpack.frontend.view.stages.panels;

import izpack.frontend.model.LicenseLoader;
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
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.validation.ValidationResult;

import utils.UI;
import utils.XML;

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
	             
	             if (o instanceof String)
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
	                     licenseDisp.setText("");
	                 }
	             }
	             
	             if (o instanceof LicenseModel)
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
	    
	    for (Iterator iter = licenses[0].iterator(); iter.hasNext();)
	    {
	        LicenseModel lic = (LicenseModel) iter.next();
	        
	        licenseList.addItem(lic);
	    }
	    
	    licenseList.addItem("<html><font size='+1'><u>GPL Incompatible</u></font>");
	    
	    for (Iterator iter = licenses[1].iterator(); iter.hasNext();)
	    {
	        LicenseModel lic = (LicenseModel) iter.next();
	        
	        licenseList.addItem(lic);
	    }	    
	    
	    licenseList.addItem("<html><font size='+1'>Custom</font>");
    }
    
    private JComboBox licenseList;
    private JTextArea licenseDisp;
    private ArrayList licenses[];
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#createXML()
     */
    public Element createXML()
    {
        LicenseModel lic = (LicenseModel) licenseList.getSelectedItem();       
        
        return XML.createResourceTree("LicencePanel.licence", lic.filename);        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        String src = XML.getResourceValue(xmlFile, "LicencePanel.licence");
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
}
