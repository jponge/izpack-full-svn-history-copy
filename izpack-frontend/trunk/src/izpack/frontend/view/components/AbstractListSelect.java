/*
 * Created on Jun 26, 2004
 * 
 * $Id: PanelSelectPage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : PanelSelectPage.java 
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
package izpack.frontend.view.components;

import izpack.frontend.view.stages.panels.ConfigurePanel;
import izpack.frontend.view.stages.panels.IzPackPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public abstract class AbstractListSelect extends IzPackPanel implements ActionListener, FocusListener, ListSelectionListener, ConfigurePanel
{
    public AbstractListSelect()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void initComponents()
    {
        // TODO Auto-generated method stub
        FormLayout layout = new FormLayout(
                "pref:grow, 3dlu, pref, 3dlu, pref:grow",	//Columns
                "100dlu, pref, 3dlu, pref, 20dlu, pref, 3dlu, pref, 100dlu"); //Rows
        layout.setColumnGroups(new int[][] {{1,5}});              
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        
        CellConstraints cc = new CellConstraints();
        
        JButton buttons[] = createButtons();
        builder.add(buttons[0], cc.xy(3, 2));        
        builder.add(buttons[1], cc.xy(3, 4));
        builder.add(buttons[2], cc.xy(3, 6));
        builder.add(buttons[3], cc.xy(3, 8));
                
        //Wrap lists in scrollpanes
        srcPane = new JScrollPane();        	
        destPane = new JScrollPane();
        
        builder.add(srcPane, cc.xywh(1, 1, 1, 9));
        builder.add(destPane, cc.xywh(5, 1, 1, 9));              
        
        setBackground(Color.WHITE);
    }   
    
    public void initLists(SelectList src, SelectList dest)
    {
        srcList = src;
        destList = dest;
        
        srcList.invalidate();
        destList.setPreferredSize(srcList.getPreferredSize());
        
        srcList.addFocusListener(this);
        destList.addFocusListener(this);
        srcList.addListSelectionListener(this);
        destList.addListSelectionListener(this);
        
        srcPane.setViewportView(srcList);
        destPane.setViewportView(destList);
    }
    
    public abstract void initSrcList();
    
    public JButton[] createButtons()
    {
        String names[] = {"right", "left", "up", "down"};
        buttons = new JButton[names.length];
        
        for (int i = 0; i < names.length; i++)
        {
            JButton button = new JButton(new ImageIcon("res/imgs/"+ names[i] + "Arrow.png"));
            //button.setBorder(null);
            button.setBackground(Color.decode("0xEDF0F9"));
            button.setPreferredSize(new Dimension(50, 22));
            button.addActionListener(this);
            button.setName(names[i]);
            
            buttons[i] = button;           
        }
        
        return buttons;
    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{	
	    String name = ((JButton) e.getSource()).getName();
	    
	    if (name.equals("left"))
	    {        
	        destList.removeElements(destList.getSelectedIndices());	        
	    }
	    else if (name.equals("right"))
	    {
	        Object selected[] = srcList.getSelectedValues();
	        for (int i = 0; i < selected.length; i++)	            
	            destList.addElement(selected[i]);
	        
	        srcList.clearSelection();
	    }
	    else if (name.equals("up") && destListFocus)
	    {	        
	        int curIndex = destList.getSelectedIndex();
	        destList.moveElement(curIndex, -1);
	        destList.setSelectedIndex(curIndex - 1);
	    }
	    else if (name.equals("down") && destListFocus)
	    {
	        int curIndex = destList.getSelectedIndex();
	        destList.moveElement(curIndex, 1);
	        destList.setSelectedIndex(curIndex + 1);
	    }
	}
	
	/* (non-Javadoc)
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e)
    {
        if (e.getSource() == destList)
        {         
            destListFocus = true;
        }
        else if (e.getSource() == srcList)
        {            
            destListFocus = false;
        }        
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e)
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].setEnabled(true);            
        }
        
        JList list = (JList) e.getSource();        
        
        //If nothing is selected, we can't move anything around
        if (srcList.getSelectedIndex() == -1 && destList.getSelectedIndex() == -1)
        {
            buttons[0].setEnabled(false);
            buttons[1].setEnabled(false);
            buttons[2].setEnabled(false);
            buttons[3].setEnabled(false);            
        }
        
        //Up and down buttons
        if (list.getSelectedIndex() == 0)
            buttons[2].setEnabled(false);       
        if (list.getSelectedIndex() == list.getModel().getSize() - 1)        
            buttons[3].setEnabled(false);
        
        //Left and right buttons
        if (list.equals(srcList))
        {
            buttons[1].setEnabled(false);
            buttons[2].setEnabled(false);
            buttons[3].setEnabled(false);
        }
        else if (list.equals(destList))
            buttons[0].setEnabled(false);
    }
        
    
    //Unimplemented interface methods
    public void focusLost(FocusEvent e){}    
    	
    //Keep track if destList has focus last between the lists
	boolean destListFocus = false;
	SelectList srcList,	destList;
	JScrollPane srcPane, destPane;
	
    JPanel destJPanel;
    JButton buttons[];    
}
