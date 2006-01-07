/*
 * Created on Nov 14, 2005 $Id: JFileChooserIconPreview.java Feb 8, 2004
 * izpack-frontend Copyright (C) 2005 Andy Gombos File :
 * JFileChooserIconPreview.java Description : TODO Add description Author's
 * email : gumbo@users.berlios.de Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package izpack.frontend.view.components;

import izpack.frontend.view.renderers.IconRenderer;
import izpack.frontend.view.win32.NativeIconAccessor;
import izpack.frontend.view.win32.NativeIconException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class JFileChooserIconPreview extends JPanel implements
                PropertyChangeListener
{
    private JComponent iconDisplay = new JPanel();
    private int maxImgWidth = 64;

    public JFileChooserIconPreview()
    {        
        JLabel label;
        
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(label = new JLabel("Preview:"), BorderLayout.NORTH);
        
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(iconDisplay, BorderLayout.CENTER);
    }

    private void configurePreview(JComponent preview)
    {
        preview.setBackground(Color.WHITE);
        preview.setOpaque(true);
        //preview.setPreferredSize(new Dimension(72, 144));        
        preview.setBorder(BorderFactory.createEtchedBorder());
        
        maxImgWidth = 64;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Icon icon = null;
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt
                        .getPropertyName()))
        {
            File newFile = (File) evt.getNewValue();
            if (newFile != null)
            {
                String filename = newFile.getAbsolutePath().toLowerCase();
                
                String internalFmtNames[] = ImageIO.getReaderFormatNames();
                
                for (String fmt : internalFmtNames)
                {
                    //If the internal Java stuff can read our image
                    if (filename.endsWith(fmt))
                    {
                        iconDisplay.removeAll();
                        iconDisplay.add(previewJavaIcon(newFile));
                    }
                }
                
                //If the file is a set of Windows Icons
                if (filename.endsWith("dll") || filename.endsWith("exe") || filename.endsWith("ico"))
                {
                    iconDisplay.removeAll();
                    iconDisplay.add(previewWindowsIcon(filename));
                }
                
                configurePreview((JComponent) iconDisplay.getComponent(0));
            }
         
            this.validate();
            this.invalidate();
            this.repaint();
        }
    }

    private JComponent previewJavaIcon(File file)    
    {
        
                try
                {
                    BufferedImage img = ImageIO.read(file);
                    
                    //TODO only scale when oversized
                    float width = img.getWidth();
                    float height = img.getHeight();
                    float scale = height / width;
                    width = maxImgWidth;
                    height = (width * scale); // height should be scaled
                                                // from new width
                    Icon icon = new ImageIcon(img.getScaledInstance(Math.max(1,
                                    (int) width),
                                    Math.max(1, (int) height),
                                    Image.SCALE_FAST));
                    
                    JLabel label = new JLabel(icon);
                    
                    return label;
                }
                catch (IOException e)
                {
                    // couldn't read image.
                }
        
        return new JLabel("");
    }
    
    public JComponent previewWindowsIcon(String filename)
    {
        try
        {
            NativeIconAccessor ni = new NativeIconAccessor(filename);
        
            int numIcons = ni.getNumIcons() > 25 ? 25 : ni.getNumIcons(); 
            
            JScrollPane scroller = new JScrollPane();
            JList displayer = new JList();
            
            scroller.setViewportView(displayer);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.setPreferredSize(new Dimension(92, 174));
            
            displayer.setCellRenderer(new IconRenderer());
            displayer.setModel(new DefaultListModel());
            
            DefaultListModel model = (DefaultListModel) displayer.getModel();            
            
            for (int i = 0; i < numIcons; i++)
            {   
                BufferedImage icon = ni.getIcon(i);               
                                
                //Scale if the image is more than maxImgWidth pixels wide
                if (icon.getWidth() > maxImgWidth)
                {
                    float ratio = ( (float) maxImgWidth ) / icon.getWidth();
                    int height = (int) (icon.getHeight() * ratio);                    
                    
                    model.addElement(new ImageIcon(
                                    icon.getScaledInstance(maxImgWidth, height, BufferedImage.SCALE_FAST)
                                    ));
                }
                else                
                    model.addElement(new ImageIcon(icon));            
            }
            
            ni.destroy();
            
            return scroller;
        }
        catch (NativeIconException nie)
        {
            //No icons in the file
            return new JLabel("<html>No icons<br>in file");
        }
    }    
}
