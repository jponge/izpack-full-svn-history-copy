/*
 * Created on Jun 28, 2004
 * 
 * $Id: ImageLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ImageLabel.java 
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
package izpack.frontend.view.renderers;

import izpack.frontend.model.PanelInfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

/**
 * @author Andy Gombos
 * 
 * Provide a component that displays an icon and text, but with consistent alignment
 */
public class ImageLabel extends DefaultListCellRenderer
{    
    public ImageLabel()
    {
        renderer = new GradientPanel();
        
        renderer.setBackground(new Color(163, 198, 252));
        
        textContainer = new JLabel();     
        iconContainer = new ImageIcon("res/imgs/folder.png");
        
        renderer.setLayout(new FlowLayout());
        ((FlowLayout) renderer.getLayout()).setAlignment(FlowLayout.LEFT);
        
        renderer.add(new JLabel(iconContainer));
        renderer.add(textContainer);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof PanelInfo)
        {
            PanelInfo model = (PanelInfo) value;
            
            textContainer.setText("<html>" +
                      "<b>&nbsp;" + model.getName() + "</b>" +
                      "<p> <font color=#969696>&nbsp;" + model.getShortDesc() + "</font>");
            
            renderer.setToolTipText(model.getLongDesc());
            
            if (isSelected)            
            {   
                renderer.setBorder(selected);
            }
            else            
            {                                
                renderer.setBorder(unselected);
            }
            
            return renderer;
        }
        
        return null;
    }
        
    private GradientPanel renderer;
    private JLabel textContainer;  
    private ImageIcon iconContainer;
    
    private static Border unselected = new SoftBevelBorder(SoftBevelBorder.RAISED);
    private static Border selected = new SoftBevelBorder(SoftBevelBorder.LOWERED);
    
    public class GradientPanel extends JPanel
    {
        @Override
        public void paintComponent(Graphics g)
        {   
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g;
            
            Rectangle b = getBounds();
            GradientPaint paint = new GradientPaint(0, 0, new Color(163, 198, 252), 
                            b.width, b.height*.75f, new Color(239, 246, 250), true); 
            
            g2.setPaint(paint);
            
            g2.fillRect(0, 0, b.width, b.height);
        }
    }    
}
