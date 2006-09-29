package com.izforge.izpack.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Shows a splash screen.  This implementation supports a transparent background.
 */
public class SplashScreen extends JWindow
{

    /**
     * A writable off screen image.
     */
    protected BufferedImage bufImage;

    /**
     * True if initialization thread is running.
     */
    protected boolean isAlive;
    protected Image image;

    /**
     * Constructor for the SplashScreen object. Starts initialization and showing of the splash screen immediately.
     *
     * @param image the image to put in the splash screen.
     */
    public SplashScreen(Image image)
    {
        this.image = image;
        run();
    }

    /**
     * Starts the initialization thread of the SplashScreen.
     */
    public void run()
    {
        isAlive = true;

        // Figure out how to center it on the screen.
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle((screenSize.width / 2 - imageWidth / 2), (screenSize.height / 2 - imageHeight / 2),
                                       imageWidth, imageHeight);
        // the critical lines, create a screen shot
        try
        {
            bufImage = new Robot().createScreenCapture(rect);
        }
        catch(AWTException e)
        {
            e.printStackTrace();
        }
        // obtain the graphics context from the BufferedImage
        Graphics2D g2D = bufImage.createGraphics();
        // Draw the image over the screen shot
        g2D.drawImage(image, 0, 0, this);
        // draw the modified BufferedImage back into the same space
        setBounds(rect);
        // present our work :)
        setVisible(true);

        isAlive = false;
    }

    /**
     * Disposes of the SplashScreen. To be called shortly before the main application is ready to be displayed.
     *
     * @throws IllegalStateException Is thrown if the initialization thread has not yet reached it's end.
     */
    public void close() throws IllegalStateException
    {
        if(!isAlive)
        {
            dispose();
        }
        else
        {
            // better not dispose a SplashScreen that has not been painted on screen yet.
            throw new IllegalStateException("SplashScreen not yet fully initialized.");
        }
    }

    /**
     *  Overrides the paint() method of JWindow.
     *
     *  @param  g  The graphics context
     */
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(bufImage, 0, 0, this);
    }
}
