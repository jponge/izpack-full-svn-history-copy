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
package izpack.frontend.view.pages.configure;

import izpack.frontend.model.License;
import izpack.frontend.model.LicenseLoader;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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

/**
 * @author Andy Gombos
 */
public class Licence extends JPanel
{
    public Licence()
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
	    
	    
	    licenseList.addActionListener(new ActionListener() 
	    {	        
		    /* (non-Javadoc)
	         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	         */
		    public void actionPerformed(ActionEvent e)
	        {
	             Object o = ( (JComboBox) e.getSource()).getSelectedItem();
	             
	             if (o instanceof String)
	             {
	                 ( (JComboBox) e.getSource()).setSelectedIndex(0);
	                 licenseDisp.setText("");
	             }
	             
	             if (o instanceof License)
	             {
	                try
                    {
                        //Read license file into JTextArea
                         BufferedReader br = new BufferedReader(new FileReader("res/licenses/" + ( (License) o).filename));
                         StringBuffer buf = new StringBuffer(1000);
                         String tmp;
                         
                        while ( (tmp = br.readLine()) != null)
                        {
                            buf.append(tmp + "\n");
                        }
                        
                        licenseDisp.setText(buf.toString());
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        licenseDisp.setText(fnfe.getLocalizedMessage());
                    }
                    catch (IOException ioe)
                    {
                        // TODO Auto-generated catch block
                        ioe.printStackTrace();
                    }
	                 
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
	        License lic = (License) iter.next();
	        
	        licenseList.addItem(lic);
	    }
	    
	    licenseList.addItem("<html><font size='+1'><u>GPL Incompatible</u></font>");
	    
	    for (Iterator iter = licenses[1].iterator(); iter.hasNext();)
	    {
	        License lic = (License) iter.next();
	        
	        licenseList.addItem(lic);
	    }
    }
    
    private JComboBox licenseList;
    private JTextArea licenseDisp;
    private ArrayList licenses[];
}
