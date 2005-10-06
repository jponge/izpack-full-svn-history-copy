/*
 * Created on Mar 23, 2005
 * 
 * $Id: XInfo.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : XInfo.java 
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
package izpack.frontend.view.stages.configure.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.IO;
import utils.UI;
import utils.XML;

import com.jgoodies.validation.ValidationResult;

/**
 * Panel to set text parsed by the variable substitutor. See XInfo documentation for more information.
 * 
 * @author Andy Gombos
 */
public class XInfo extends JPanel implements ConfigurePanel, ActionListener
{    
    public XInfo()
    {
        xinfo = new JTextArea();
        filebox = new JTextField("Text file to be parsed: ");
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.getViewport().add(xinfo);
	    scrollPane.setPreferredSize(new Dimension(500, 350));
	    xinfo.setWrapStyleWord(true);	    
	    
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    
	    JButton browse = new JButton("Browse");
        browse.addActionListener(this);
        
	    add(filebox);
	    add(Box.createVerticalStrut(5));
	    add(browse);	    
	    add(Box.createVerticalStrut(15));
	    add(scrollPane);
    }
    
    JTextArea xinfo;
    JTextField filebox;

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {     
        File textFile = UI.getFile(this, "text");
        loadFile(textFile);
    }
    
    private void loadFile(File textFile)
    {
        filebox.setText(textFile.getAbsolutePath());
        xinfo.setText(IO.loadFileIntoString(textFile));
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#createXML()
     * 
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     */
    public Element createXML(Document doc)
    {   
        return XML.createResourceTree("XInfoPanel.info", filebox.getText(), doc);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#initFromXML(org.w3c.dom.Document)
     * 
     * Structure:
     * <resources>
     * 	<res ...>
     * </resources>
     */
    public void initFromXML(Document document)
    {
        String filename = XML.getResourceValue(document, "XInfoPanel.info");
        
        //Check to see if there's anything to load from
        if (filename != null)
        {
            loadFile(new File(filename));
        }
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.pages.configure.ConfigurePage#validatePage()
     */
    public ValidationResult validatePanel()
    {
        return null;        
    }
}
