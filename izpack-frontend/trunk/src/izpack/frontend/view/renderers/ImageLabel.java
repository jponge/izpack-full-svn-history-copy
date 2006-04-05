/*
 * Created on Jun 28, 2004
 * 
 * $Id: ImageLabel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : ImageLabel.java 
 * Description : TODO Add description
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

package izpack.frontend.view.renderers;

import izpack.frontend.model.PanelInfo;
import izpack.frontend.model.stages.PanelModel;

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
        if (value instanceof PanelModel)
        {
            PanelModel pm = (PanelModel) value;
            PanelInfo model = pm.configData;
            
            if (model == null)
                System.out.println(pm);
            
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
        
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
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
