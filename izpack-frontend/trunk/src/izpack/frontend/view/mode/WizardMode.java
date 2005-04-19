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

import izpack.frontend.controller.StageChangeEvent;
import izpack.frontend.controller.StageChangeListener;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.Stage;
import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Show Panel Select and General Information stages
 * 
 * Show selected Panels sequentially, blocking until validate = true
 *
 * @author Andy Gombos
 */
public class WizardMode extends JFrame implements StageChangeListener
{
    public static void main(String[] args)
    {
        new WizardMode();
    }
    
    public WizardMode()
    {   
        setLayout(new BorderLayout());
        
        iconPanel = createIconPanel();
        IzPackStage geninfo = new GeneralInformation();        
        IzPackStage sel = new PanelSelection();
        geninfo.initializeStage();
        sel.initializeStage();
        
        geninfo.addStageChangeListener(this);
        
        iconPanel.add(geninfo.getTopNavBar());
        
        getContentPane().add(iconPanel, BorderLayout.NORTH);
        
        base.setLayout(layout);
        
        base.add(geninfo, geninfo.getClass().toString());
        base.add(sel, sel.getClass().toString());        
        
        getContentPane().add(base, BorderLayout.CENTER);        
        
        setPreferredSize(new Dimension(700, 700));
        pack();        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    }
    
    private JPanel createIconPanel()
    {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        
        JLabel izPackIcon = new JLabel("<html><font size=+2>IzPack</font>", new ImageIcon("res/imgs/synaptic.png"), SwingConstants.LEFT);
        navPanel.add(izPackIcon);
        
        navPanel.add(Box.createHorizontalGlue());
        
        return navPanel;
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.controller.StageChangeListener#changeStage(izpack.frontend.controller.StageChangeEvent)
     */
    public void changeStage(StageChangeEvent e)
    {
        System.out.println("Next to: " + e.getStageClass());
        
        IzPackStage stage = IzPackStage.getStage(e.getStageClass());
        iconPanel.remove(iconPanel.getComponentCount() - 1);
        iconPanel.repaint();
        System.out.println(stage.getTopNavBar());
        iconPanel.add(stage.getTopNavBar());
        layout.show(base, e.getStageClass().toString());
        pack();        
    }    
    
    JPanel base = new JPanel(), iconPanel;    
    CardLayout layout = new CardLayout();    
}
