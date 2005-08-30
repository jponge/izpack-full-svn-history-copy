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
import izpack.frontend.view.stages.StageOrder;
import izpack.frontend.view.stages.configure.PanelConfigurator;
import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.packs.Pack;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utils.XML;

/**
 * Show Panel Select and General Information stages
 * 
 * Show selected Panels sequentially, blocking until validate = true
 *
 * @author Andy Gombos
 */
public class WizardMode extends JFrame implements StageChangeListener, WindowListener
{   
    public static void main(String[] args)
    {
        new WizardMode();
    }
    
    public WizardMode()
    {   
        long start = System.currentTimeMillis();
        
        setLayout(new BorderLayout());
        
        base.setLayout(layout);        
        getContentPane().add(base, BorderLayout.CENTER);
        
        iconPanel = createIconPanel();
        
        IzPackStage geninfo = 	createStage(GeneralInformation.class, base);
        
        createStage(PanelSelection.class, base);
        createStage(Pack.class, base);
        createStage(PanelConfigurator.class, base);        
        
        addWindowListener(this);
        
        iconPanel.add(geninfo.getTopNavBar());
        
        infoPanel.add(geninfo.getBottomInfoBar());
        
        getContentPane().add(iconPanel, BorderLayout.NORTH);
        getContentPane().add(infoPanel, BorderLayout.SOUTH);
        
        setPreferredSize(new Dimension(700, 700));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        long stop = System.currentTimeMillis();
        
        long startupTime = stop - start;
        System.out.println(startupTime + "ms " + (startupTime / 1000f) + "s " + (startupTime / 1000 / 60f) + "min");
    }
    
    /*
     * Create a stage instance based on the class. Also initializes and registers the stage listener
     */
    private IzPackStage createStage(Class stageClass, JPanel base)
    {
        try
        {
            IzPackStage instance = (IzPackStage) stageClass.newInstance();
            
            instance.initializeStage();
            instance.addStageChangeListener(this);   
            
            base.add(instance, stageClass.toString());
            
            return instance;
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /*
     * Create a panel with the 'IzPack' logo for the top left corner
     */
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
     * 
     * Change the stage that is visible
     * The next visible stage is defined by the stage that created the event
     * 
     * This is because the stages create the navigation bar at the top. Perhaps not the best design
     */
    public void changeStage(StageChangeEvent e)
    {        
        if (e.isConsumed())
            return;
        
        e.consume();
        
        IzPackStage stage = IzPackStage.getStage(e.getStageClass());
        
        //Remove the navigation bar from the top
        iconPanel.remove(iconPanel.getComponentCount() - 1);
        iconPanel.repaint();
        
        //Add the new nav panel
        iconPanel.add(stage.getTopNavBar());
        
        infoPanel.removeAll();
        infoPanel.add(stage.getBottomInfoBar());
        
        pack();
        
        layout.show(base, e.getStageClass().toString());
    }    
    
    JPanel base = new JPanel(), iconPanel, infoPanel = new JPanel();    
    CardLayout layout = new CardLayout();
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e)
    {
                
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent e)
    {
        // TODO Auto-generated method stub
        
    }    
}
