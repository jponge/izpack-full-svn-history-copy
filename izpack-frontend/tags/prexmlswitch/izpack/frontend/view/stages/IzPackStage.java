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
import izpack.frontend.controller.validators.StageValidator;
import izpack.frontend.model.AppConfiguration;
import izpack.frontend.model.LangResources;
import izpack.frontend.model.stages.StageDataModel;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.stages.StageOrder.StageContainer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.w3c.dom.Document;

import utils.UI;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * @author Andy Gombos
 */
public abstract class IzPackStage extends JPanel implements Stage
{   
    protected boolean addValidationListeners = true;

    public ValidationResultModel getValidationModel()
    {
        return validationModel;
    }
    public void setValidationModel(ValidationResultModel validationModel)
    {
        this.validationModel = validationModel;
    }
    protected IzPackStage()
    {   
        registerStage(this);       
        
        //TODO Make the center component a fixed size
    }

    /*
     * (non-Javadoc)
     * 
     * @see izpack.frontend.view.stages.Stage#getTopNavBar()
     */
    public final JPanel getTopNavBar()
    {
        JPanel base = new JPanel();
        
        System.out.println("Current class: " + this.getClass());
        final StageContainer previousCont = stageOrder.getPreviousStage(this.getClass());
        final StageContainer nextCont = stageOrder.getNextStage(this.getClass());
        
        if (previousCont != null)
            System.out.println("Prev: " + previousCont.stageClass);
        if (nextCont != null)
            System.out.println("Next: " + nextCont.stageClass);
        
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
        
        validationModel.setResult(validateStage());
        
        bottom.add(ValidationResultViewFactory.createReportList(validationModel));

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
    public static void registerStage(IzPackStage stage)
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
        for (IzPackStage element : stageList)
        {            
            if (element.getClass().equals(stage))
            {
                return element;
            }
        }
        
        return null;
    }
    
    public static ArrayList<IzPackStage> getAllStages()
    {
        return stageList;
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
        for (StageChangeListener listener : changeListenerList)
        {
            listener.changeStage(ste);
        }
	}
	
	/*
	 * These need to be static, but abstract methods can't be...
	 */
	public PresentationModel getValidatingModel()
	{
        return null;
    } 
	
	public StageValidator getValidator()
	{
	    return null;
	}
	
	public abstract StageDataModel getDataModel();
	
	private static ArrayList<IzPackStage> stageList = new ArrayList<IzPackStage>();
	private static ArrayList<StageChangeListener> changeListenerList = new ArrayList<StageChangeListener>();
	
	protected static LangResources lr = IzPackFrame.getInstance().langResources();
	protected static StageOrder stageOrder = new StageOrder();
	
	protected ValidationResultModel validationModel = new DefaultValidationResultModel();
}
