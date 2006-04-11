/*
 * Created on Jul 3, 2004 $Id: Wizard.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos 
 * File : Wizard.java 
 * Description : TODO Add description 
 * 
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

package izpack.frontend.view.mode;

import izpack.frontend.actions.ActionHandler;
import izpack.frontend.controller.StageChangeEvent;
import izpack.frontend.controller.StageChangeListener;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.compile.Compile;
import izpack.frontend.view.stages.configure.PanelConfigurator;
import izpack.frontend.view.stages.geninfo.GeneralInformation;
import izpack.frontend.view.stages.packs.Pack;
import izpack.frontend.view.stages.panelselect.PanelSelection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.w3c.dom.Document;

import utils.UI;
import utils.XML;
import exceptions.DocumentCreationException;
import exceptions.UnhandleableException;

/**
 * Show Panel Select and General Information stages Show selected Panels
 * sequentially, blocking until validate = true
 * 
 * @author Andy Gombos
 */
public class WizardMode extends JFrame implements StageChangeListener,
                WindowListener
{
    public WizardMode(WelcomeScreen launcherInstance)
    {
        launcher = launcherInstance;
        actionHandler = launcher.actionHandler;        

        long start = System.currentTimeMillis();

        setLayout(new BorderLayout());

        base.setLayout(layout);
        getContentPane().add(base, BorderLayout.CENTER);

        iconPanel = createIconPanel();

        IzPackStage geninfo = createStage(GeneralInformation.class, base);

        createStage(PanelSelection.class, base);
        createStage(Pack.class, base);
        createStage(PanelConfigurator.class, base);
        createStage(Compile.class, base);

        addWindowListener(this);

        iconPanel.add(geninfo.getTopNavBar());

        infoPanel.add(geninfo.getBottomInfoBar());

        leftNavBar.add(geninfo.getLeftNavBar());

        getContentPane().add(iconPanel, BorderLayout.NORTH);
        getContentPane().add(infoPanel, BorderLayout.SOUTH);
        getContentPane().add(leftNavBar, BorderLayout.WEST);

        createMenuBar();

        setPreferredSize(new Dimension(700, 700));
        pack();

        long stop = System.currentTimeMillis();

        long startupTime = stop - start;
        System.out.println("Startup time: " + startupTime + "ms " + (startupTime / 1000f) + "s "
                        + (startupTime / 1000 / 60f) + "min");
    }

    /*
     * (non-Javadoc)
     * 
     * @see izpack.frontend.controller.StageChangeListener#changeStage(izpack.frontend.controller.StageChangeEvent)
     *      Change the stage that is visible The next visible stage is defined
     *      by the stage that created the event This is because the stages
     *      create the navigation bar at the top. Perhaps not the best design
     */
    public void changeStage(StageChangeEvent e)
    {
        if (e.isConsumed()) return;

        e.consume();

        IzPackStage stage = IzPackStage.getStage(e.getStageClass());

        stage.initializeStage();

        // Remove the navigation bar from the top
        iconPanel.remove(iconPanel.getComponentCount() - 1);
        iconPanel.repaint();

        // Add the new nav panel
        iconPanel.add(stage.getTopNavBar());

        infoPanel.removeAll();
        infoPanel.add(stage.getBottomInfoBar());

        leftNavBar.removeAll();
        leftNavBar.add(stage.getLeftNavBar());

        pack();

        layout.show(base, e.getStageClass().toString());
    }

    public void initializeFromXML(String xmlFile)
                    throws DocumentCreationException
    {        
        Document d = XML.createDocument(xmlFile);

        ArrayList<IzPackStage> stages = IzPackStage.getAllStages();
        for (IzPackStage stage : stages)
        {
            stage.initializeStageFromXML(d);
        }
        
        changeStage(new StageChangeEvent(GeneralInformation.class));
    }
    
    public void windowClosing(WindowEvent e)
    {
        //TODO Make welcome screen visible
        launcher.setVisible(true);
    }
    
    public void windowActivated(WindowEvent e) {}    
    public void windowClosed(WindowEvent e) {}    
    public void windowDeactivated(WindowEvent e) {}    
    public void windowDeiconified(WindowEvent e) {}    
    public void windowIconified(WindowEvent e) {}    
    public void windowOpened(WindowEvent e) {}
    
    /*
     * Create a panel with the 'IzPack' logo for the top left corner
     */
    private JPanel createIconPanel()
    {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));

        JLabel izPackIcon = new JLabel("<html><font size=+2>IzPack</font>",
                        new ImageIcon("res/imgs/synaptic.png"),
                        SwingConstants.LEFT);
        navPanel.add(izPackIcon);

        navPanel.add(Box.createHorizontalGlue());

        return navPanel;
    }

    private void createMenuBar()
    {   
        //TODO Internationalize
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setName("open");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setName("save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setName("saveas");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setName("exit");

        openItem.addActionListener(actionHandler);
        saveItem.addActionListener(actionHandler);
        saveAsItem.addActionListener(actionHandler);
        exitItem.addActionListener(actionHandler);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        
        this.setJMenuBar(menuBar);
    }

    /*
     * Create a stage instance based on the class. Also initializes and
     * registers the stage listener
     */
    private IzPackStage createStage(Class stageClass, JPanel base)
    {
        try
        {
            IzPackStage instance = (IzPackStage) stageClass.newInstance();

            instance.addStageChangeListener(this);

            base.add(instance, stageClass.toString());

            return instance;
        }
        catch (InstantiationException e)
        {
            UI.showError("Unable to create stage " + stageClass.getSimpleName()
                            + ". Exiting", "Initialization failure");
            System.exit(-1);
        }
        catch (IllegalAccessException e)
        {
            // Shouldn't ever happen
            throw new UnhandleableException(e);
        }

        return null;
    }

    JPanel base = new JPanel(), iconPanel, infoPanel = new JPanel(),
                    leftNavBar = new JPanel();

    CardLayout layout = new CardLayout();    

    private static WelcomeScreen launcher;
    private static ActionHandler actionHandler; 
}
