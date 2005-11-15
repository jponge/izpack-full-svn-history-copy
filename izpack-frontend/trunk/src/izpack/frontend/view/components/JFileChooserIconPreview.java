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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JFileChooserIconPreview extends JPanel implements
                PropertyChangeListener
{
    private JLabel label;
    private int maxImgWidth;

    public JFileChooserIconPreview()
    {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(new JLabel("Preview:"), BorderLayout.NORTH);
        label = new JLabel();
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(72, 72));
        maxImgWidth = 64;
        label.setBorder(BorderFactory.createEtchedBorder());
        add(label, BorderLayout.CENTER);
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
                String path = newFile.getAbsolutePath();

                // TODO other icon types here
                if (path.endsWith(".ico") || path.endsWith(".png"))
                {
                    try
                    {
                        BufferedImage img = ImageIO.read(newFile);
                        float width = img.getWidth();
                        float height = img.getHeight();
                        float scale = height / width;
                        width = maxImgWidth;
                        height = (width * scale); // height should be scaled
                                                    // from new width
                        icon = new ImageIcon(img.getScaledInstance(Math.max(1,
                                        (int) width),
                                        Math.max(1, (int) height),
                                        Image.SCALE_SMOOTH));
                    }
                    catch (IOException e)
                    {
                        // couldn't read image.
                    }
                }
            }

            label.setIcon(icon);
            this.repaint();

        }
    }

}
