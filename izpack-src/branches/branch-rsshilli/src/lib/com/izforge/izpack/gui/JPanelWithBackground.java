package com.izforge.izpack.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.image.ImageObserver;

public class JPanelWithBackground extends JPanel
{
    protected Image backgroundImage = null;
    protected int width;
    protected int height;

    /* The JPanel constructors with the backgroundImage as an extra parameter*/
    public JPanelWithBackground(Image backgroundImage)
    {
        super();
        this.backgroundImage = backgroundImage;
        cacheImageSize();
    }

    public JPanelWithBackground(boolean isDoubleBuffered, Image backgroundImage)
    {
        super(isDoubleBuffered);
        this.backgroundImage = backgroundImage;
        cacheImageSize();
    }

    public JPanelWithBackground(LayoutManager layout, Image backgroundImage)
    {
        super(layout);
        this.backgroundImage = backgroundImage;
        cacheImageSize();
    }

    public JPanelWithBackground(LayoutManager layout, boolean isDoubleBuffered, Image backgroundImage)
    {
        super(layout, isDoubleBuffered);
        this.backgroundImage = backgroundImage;
        cacheImageSize();
    }

    /**
     * Returns the background image
     *
     * @return Background image
     */
    public Image getBackgroundImage()
    {
        return backgroundImage;
    }

    /**
     * Sets the background image
     *
     * @param backgroundImage Background image
     */
    public void setBackgroundImage(Image backgroundImage)
    {
        this.backgroundImage = backgroundImage;
        cacheImageSize();
    }

    /**
     * Overrides the painting to display a background image
     */
    protected void paintComponent(Graphics g)
    {
        if(backgroundImage != null)
        {
            g.drawImage(backgroundImage, 0, 0, this);
        } else {
            super.paintComponent(g);
        }

    }

    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    private void cacheImageSize()
    {
        ImageObserver observer = new ImageObserver()
        {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
            {
                if(width > 0 && height > 0)
                {
                    JPanelWithBackground.this.width = width;
                    JPanelWithBackground.this.height = height;
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };
        width = backgroundImage.getWidth(observer);
        height = backgroundImage.getHeight(observer);
    }

}
