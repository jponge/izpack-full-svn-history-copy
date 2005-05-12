/*
 * Created on Jul 30, 2004
 * 
 * $Id: IzPackStage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : IzPackStage.java 
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
package izpack.frontend.view.stages;

import izpack.frontend.controller.GUIController;
import izpack.frontend.controller.StageChangeEvent;
import izpack.frontend.controller.StageChangeListener;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.stages.StageOrder.StageContainer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;

import org.w3c.dom.Document;

import utils.UI;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * @author Andy Gombos
 */
public abstract class IzPackStage extends JPanel implements Stage, ActionListener, CaretListener
{   
    protected boolean addValidationListeners = true;

    public IzPackStage()
    {   
        registerStage(this);       
        
        //TODO Make the center component a fixed size
    }  
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.Stage#initializeStage()
     */
    public void initializeStage()
    {   
        if (addValidationListeners)
            createValidationListeners();
    }

    /**
     * 
     */
    private void createValidationListeners()
    {
        ArrayList components = getComponents(this);
        
        
        for (Iterator iterator = components.iterator(); iterator.hasNext();)
        {
            Component component = (Component) iterator.next();            

            //Validate every time a button is pressed
            if (component instanceof AbstractButton)
            {
                AbstractButton ab = (AbstractButton) component;

                ab.addActionListener(this);
            }
            
            //Validate every time the caret moves
            if (component instanceof JTextComponent)
            {
                JTextComponent jtc = (JTextComponent) component;                
            
                jtc.addCaretListener(this);
            }
        }
    }
    
    private ArrayList getComponents(Container base)
    {
        ArrayList list = new ArrayList(base.getComponentCount());
        Component components[] = base.getComponents();        
        
        for (int i = 0; i < components.length; i++)
        {   
            Container c = (Container) components[i];
            
            if (c.getComponentCount() > 1)
                list.addAll(getComponents(c));
            else
                list.add(components[i]);
        }
        
        return list;
    }


    /*
     * (non-Javadoc)
     * 
     * @see izpack.frontend.view.stages.Stage#getTopNavBar()
     */
    public final JPanel getTopNavBar()
    {
        JPanel base = new JPanel();
        final StageContainer previousCont = stageOrder.getPreviousStage(this.getClass());
        final StageContainer nextCont = stageOrder.getNextStage(this.getClass());
        
        JButton previous = null, next = null;
        
        if (previousCont != null)
        {
            previous = UI.getNavButton(previousCont.buttonText, UI.BACK);        
        
        	previous.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                fireStageChangeEvent(new StageChangeEvent(previousCont.stageClass));
            }
        	});        
        	
        	base.add(previous);
        }
        
        if (nextCont != null)
	    {        
	        next = UI.getNavButton(nextCont.buttonText, UI.FORWARD);
	        
	        next.addActionListener(new ActionListener()
	        {
	
	            public void actionPerformed(ActionEvent e)
	            {
	                fireStageChangeEvent(new StageChangeEvent(nextCont.stageClass));
	            }
	
	        });
	     
	        if (previous != null)
	            next.setPreferredSize(previous.getPreferredSize());
	        base.add(next);
	    }
        
        return base;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see izpack.frontend.view.stages.Stage#getBottomInfoBar()
     */
    public JPanel getBottomInfoBar()
    {
        JPanel bottom = new JPanel();
     
        vrm.setResult(validateStage());

        bottom.add(ValidationResultViewFactory.createReportList(vrm));

        bottom.setPreferredSize(new Dimension(700, 100));

        return bottom;
    }
    
    public abstract Document createInstallerData();
    
    public abstract ValidationResult validateStage();
    
    /**
     * Allow stages to communicate (call methods) easier.
     * Sort of a getInstance() for non-Singletons. 
     * 
     * @param stage The instance to register
     */
    public static void registerStage(Stage stage)
    {        
        stageList.add(stage);
    }

    /**
     * Return a stage instance given the type.
     * 
     * This could really use generics 
     * 
     * @param stage The stage type to find
     * @return The stage object
     */    
    public static IzPackStage getStage(Class stage)
    {
        for (Iterator iter = stageList.iterator(); iter.hasNext();)
        {
            IzPackStage element = (IzPackStage) iter.next();
            if (element.getClass().equals(stage))
            {
                return element;
            }
        }
        
        return null;
    }

    /**
	 * Get the lang-resources object.
	 * @return The language resource model.
	 */
	public LangResources langResources() {
		return GUIController.getInstance().langResources();
	}
	
	/**
	 * Get the application configuration.
	 * @return the application configuration model.
	 */
	public AppConfiguration appConfiguration() {
		return GUIController.getInstance().appConfiguration();
	}
	
	
	/*
	 * Monitor for changes to components of the stage, and validate the stage
	 * when things change
	 * 
	 * TODO This is a hack, and probably kills memory usage and performance
	 */
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("validate action");
        vrm.setResult(validateStage());
    }

    public void caretUpdate(CaretEvent e)
    {
        vrm.setResult(validateStage());
    }   
    
	/*
	 * 
	 * StageChange stuff
	 * 
	 */
	public void addStageChangeListener(StageChangeListener stl)
	{	 
	    changeListenerList.add(stl);
	}
	
	public void removeStageChangeListener(StageChangeListener stl)
	{
	    changeListenerList.remove(stl);
	}
	
	protected void fireStageChangeEvent(StageChangeEvent ste)
	{   
	    for (Iterator iter = changeListenerList.iterator(); iter.hasNext();)
        {
            StageChangeListener element = (StageChangeListener) iter.next();     
            
            element.changeStage(ste);
        }
	}
	
	private static ArrayList stageList = new ArrayList();
	private static ArrayList changeListenerList = new ArrayList();
	
	protected static LangResources lr = IzPackFrame.getInstance().langResources();
	protected static StageOrder stageOrder = new StageOrder();
	
	protected ValidationResultModel vrm = new DefaultValidationResultModel();
}
