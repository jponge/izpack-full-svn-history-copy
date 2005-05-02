/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001-2004 Julien Ponge, Volker Friedritz
 *
 *  File : ImgPacksPanel.java
 *  Description : A panel to select the packs to install.
 *  Author's email : julien@izforge.com
 *  Author's email : volker.friedritz@gmx.de
 *  Author's Website : http://www.izforge.com
 *  
 *  Portions are Copyright (C) 2002 Marcus Wolschon
 *  Portions are Copyright (C) 2002 Jan Blok (jblok@profdata.nl - PDM - www.profdata.nl)
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.izforge.izpack.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.ResourceManager;
import com.izforge.izpack.util.IoHelper;

/**
 * The ImgPacks panel class. Allows the packages selection with a small picture
 * displayed for every pack. This new version combines the old PacksPanel and
 * the ImgPacksPanel so that the positive characteristics of both are combined.
 * This class handles only the layout and some related stuff. Common stuff are
 * handled by the base class.
 * 
 * @author Julien Ponge
 * @author Volker Friedritz
 * @author Klaus Bartz
 */
public class ImgPacksPanel extends PacksPanelBase
{

    /**
     * 
     */
    private static final long serialVersionUID = 3977858492633659444L;

    /** The images to display. */
    private ArrayList images;

    /** The description scroll. */
    private JScrollPane descriptionScroller;

    /** The img label. */
    private JLabel imgLabel;

    /** The current image index. */
    private int index = 0;

    /** The layout. */
    private GridBagLayout layout;

    /** The layout constraints. */
    private GridBagConstraints gbConstraints;

    /**
     * The constructor.
     * 
     * @param parent
     *            The parent window.
     * @param idata
     *            The installation data.
     */
    public ImgPacksPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.izforge.izpack.panels.PacksPanelBase#createNormalLayout()
     */
    protected void createNormalLayout()
    {
        preLoadImages();
        layout = new GridBagLayout();
        gbConstraints = new GridBagConstraints();
        setLayout(layout);

        // Create constraint for first component as standard constraint.
        parent.buildConstraints(gbConstraints, 0, 0, 1, 1, 0.25, 0.0);
        gbConstraints.insets = new Insets(5, 5, 5, 5);
        gbConstraints.anchor = GridBagConstraints.WEST;
        // Create the info label.
        createLabel("PacksPanel.info", "preferences", layout, gbConstraints);

        // Create the snap label.
        parent.buildConstraints(gbConstraints, 1, 0, 1, 1, 0.50, 0.0);
        createLabel("ImgPacksPanel.snap", "tip", layout, gbConstraints);

        // Create packs table with a scroller.
        tableScroller = new JScrollPane();
        parent.buildConstraints(gbConstraints, 0, 1, 1, 2, 0.50, 0.0);
        gbConstraints.fill = GridBagConstraints.BOTH;
        packsTable = createPacksTable(250, tableScroller, layout, gbConstraints);

        // Create the image label with a scroller.
        imgLabel = new JLabel((ImageIcon) images.get(0));
        JScrollPane imgScroller = new JScrollPane(imgLabel);
        imgScroller.setPreferredSize(getPreferredSizeFromImages());
        parent.buildConstraints(gbConstraints, 1, 1, 1, 1, 0.5, 1.0);
        layout.addLayoutComponent(imgScroller, gbConstraints);
        add(imgScroller);

        // Create a vertical strut.

        Component strut = Box.createVerticalStrut(20);
        parent.buildConstraints(gbConstraints, 1, 2, 1, 3, 0.0, 0.0);
        layout.addLayoutComponent(strut, gbConstraints);
        add(strut);

        // Create the dependency area with a scroller.
        if (dependenciesExist)
        {
            JScrollPane depScroller = new JScrollPane();
            depScroller.setPreferredSize(new Dimension(250, 40));
            parent.buildConstraints(gbConstraints, 0, 3, 1, 1, 0.50, 0.50);
            dependencyArea = createTextArea("ImgPacksPanel.dependencyList", depScroller, layout,
                    gbConstraints);
        }

        // Create the description area with a scroller.
        descriptionScroller = new JScrollPane();
        descriptionScroller.setPreferredSize(new Dimension(200, 60));

        parent.buildConstraints(gbConstraints, 1, 3, 1, 1, 0.50, 0.50);
        descriptionArea = createTextArea("PacksPanel.description", descriptionScroller, layout,
                gbConstraints);
        // Create the tip label.
        parent.buildConstraints(gbConstraints, 0, 4, 2, 1, 0.0, 0.0);
        createLabel("PacksPanel.tip", "tip", layout, gbConstraints);
        // Create the space label.
        parent.buildConstraints(gbConstraints, 0, 5, 2, 1, 0.0, 0.0);
        spaceLabel = createPanelWithLabel("PacksPanel.space", layout, gbConstraints);
        if (IoHelper.supported("getFreeSpace"))
        { // Create the free space label only if free space is supported.
            parent.buildConstraints(gbConstraints, 0, 6, 2, 1, 0.0, 0.0);
            freeSpaceLabel = createPanelWithLabel("PacksPanel.freespace", layout, gbConstraints);
        }

    }

    /** Pre-loads the images. */
    private void preLoadImages()
    {
        int size = idata.availablePacks.size();
        images = new ArrayList(size);
        for (int i = 0; i < size; i++)
            try
            {
                URL url = ResourceManager.getInstance().getURL("ImgPacksPanel.img." + i);
                ImageIcon img = new ImageIcon(url);
                images.add(img);
            }
            catch (Exception err)
            {
                err.printStackTrace();
            }
    }

    /**
     * Try to find a good preferredSize for imgScroller by checking all loaded
     * images' width and height.
     */
    private Dimension getPreferredSizeFromImages()
    {
        int maxWidth = 80, maxHeight = 60;
        ImageIcon icon;

        for (Iterator it = images.iterator(); it.hasNext();)
        {
            icon = (ImageIcon) it.next();
            maxWidth = Math.max(maxWidth, icon.getIconWidth());
            maxHeight = Math.max(maxHeight, icon.getIconHeight());
        }

        maxWidth = Math.min(maxWidth + 20, idata.guiPrefs.width - 150);
        maxHeight = Math.min(maxHeight + 20, idata.guiPrefs.height - 150);

        return new Dimension(maxWidth, maxHeight);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e)
    {
        super.valueChanged(e);
        int i = packsTable.getSelectedRow();
        if (i >= 0) imgLabel.setIcon((ImageIcon) images.get(i));

    }

}
