/*
 * Created on Jul 3, 2004
 * 
 * $Id: Wizard.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Wizard.java 
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
package izpack.frontend.view.mode;

import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.XML;
import utils.XMLAggregator;

/**
 * Show Panel Select and General Information stages
 * 
 * Show selected Panels sequentially, blocking until validate = true
 *
 * @author Andy Gombos
 */
public class WizardMode extends JFrame implements ActionListener
{
    public static void main(String[] args)
    {
        new WizardMode();
    }
    
    public WizardMode()
    {
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout());
        
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout());
        getContentPane().add(jp, BorderLayout.NORTH);
        
        JButton b = new JButton("Next");
        b.addActionListener(this);
        jp.add(b);
        
        JButton bb = new JButton("Exit");
        bb.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {   
                Document d1 = genInfo.createInstallerData();
                Document d2 = ps.createInstallerData();
                
                Document both = XML.getNewDocument();
                
                Node d1root = both.importNode(d1.getFirstChild(), true);
                Node d2root = both.importNode(d2.getFirstChild(), true);
                
                Element topRoot = XML.createElement("toproot", both);
                both.appendChild(topRoot);
                
                topRoot.appendChild(d1root);
                topRoot.appendChild(d2root);
                
                XML.printXML(both);
                System.out.println("---------------------");
                topRoot = XMLAggregator.mergeElements(topRoot);
                XML.printXML(both);
                
                dispose();
            }});
        jp.add(bb);
        
        genInfo = new GeneralInformation();
        genInfo.initializeStage();
        ps = new PanelSelection();
        ps.initializeStage();
        
        
        base.setLayout(layout);
        base.add(genInfo, "A");
        base.add(ps, "B");
        
        getContentPane().add(base, BorderLayout.CENTER);
        
        pack();
        setVisible(true);
    }
    
    //static IzPackFrame iz = IzPackFrame.getInstance();
    PanelSelection ps;
    GeneralInformation genInfo;
    JPanel base = new JPanel();
    CardLayout layout = new CardLayout();
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {        
        layout.next(base);        
    }
}
