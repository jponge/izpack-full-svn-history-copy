/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001,2002 Julien Ponge
 *
 *  File :               PacksPanel.java
 *  Description :        A panel to select the packs to install.
 *  Author's email :     julien@izforge.com
 *  Author's Website :   http://www.izforge.com
 *
 *  Portions are Copyright (C) 2002 Marcus Wolschon
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

import com.izforge.izpack.*;
import com.izforge.izpack.gui.*;
import com.izforge.izpack.installer.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import net.n3.nanoxml.*;

/**
 *  The packs selection panel class.
 *
 * @author     Julien Ponge
 * @created    November 1, 2002
 */
public class PacksPanel extends IzPanel implements ActionListener
{
  /**  The layout. */
  private BoxLayout layout;

  /**  The info label. */
  private JLabel infoLabel;

  /**  The tip label. */
  private JLabel tipLabel;

  /**  The space label. */
  private JLabel spaceLabel;

  /**  The bytes of the current pack. */
  private int bytes = 0;

  /**  The checkboxes. */
  private HashMap checkBoxes = new HashMap();

  /**  The checkboxes. */
  private HashMap packsforBoxes = new HashMap();

  /**  The installation data. */
  protected InstallData idata;


  /**
   *  The constructor.
   *
   * @param  parent  The parent window.
   * @param  idata   The installation data.
   */
  public PacksPanel(InstallerFrame parent, InstallData idata)
  {
    super(parent, idata);
    this.idata = idata;

    // The 'super' layout
    GridBagLayout superLayout = new GridBagLayout();
    setLayout(superLayout);
    GridBagConstraints gbConstraints = new GridBagConstraints();
    gbConstraints.insets = new Insets(0, 0, 0, 0);
    gbConstraints.fill = GridBagConstraints.NONE;
    gbConstraints.anchor = GridBagConstraints.CENTER;

    // We initialize our 'real' layout
    JPanel centerPanel = new JPanel();
    layout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
    centerPanel.setLayout(layout);
    superLayout.addLayoutComponent(centerPanel, gbConstraints);
    add(centerPanel);

    // We create and put the labels

    centerPanel.add(Box.createVerticalStrut(10));

    infoLabel = new JLabel(parent.langpack.getString("PacksPanel.info"),
      parent.icons.getImageIcon("preferences"), JLabel.TRAILING);
    centerPanel.add(infoLabel);

    centerPanel.add(Box.createVerticalStrut(10));

    tipLabel = new JLabel(parent.langpack.getString("PacksPanel.tip"),
      parent.icons.getImageIcon("tip"), JLabel.TRAILING);
    centerPanel.add(tipLabel);

    centerPanel.add(Box.createVerticalStrut(20));

    // Adds each pack checkbox
    int size = idata.availablePacks.size();
    Pack pack;
    JCheckBox checkBox;
    for (int i = 0; i < size; i++)
    {
      pack = (Pack) idata.availablePacks.get(i);
      String caption = (pack.description != null) ? " " + pack.name + " : " + pack.description
         : " " + pack.name;
      checkBox = new JCheckBox(caption, true);
      checkBox.addActionListener(this);
      if (pack.required)
        checkBox.setEnabled(false);
      checkBoxes.put(pack, checkBox);
      packsforBoxes.put(checkBox, pack);
      centerPanel.add(checkBox);
    }

    centerPanel.add(Box.createVerticalStrut(20));

    spaceLabel = new JLabel(parent.langpack.getString("PacksPanel.space"));
    centerPanel.add(spaceLabel);

  }


  /**  Called when the panel becomes active.  */
  public void panelActivate()
  {
    try
    {
      // set the JCheckBoxes to the currently selected panels. The selection meight have changes in another panel
      java.util.Iterator iter = idata.availablePacks.iterator();
      bytes = 0;
      while (iter.hasNext())
      {
        Pack p = (Pack) iter.next();
        JCheckBox check = (JCheckBox) checkBoxes.get(p);
        if (check == null)
        {
          System.err.println("ERROR[PacksPanel]: internal error, no checkbox for Pack. ignoring");
          continue;
        }
        if (p.required)
        {
          check.setSelected(true);
          bytes += p.nbytes;
          continue;
        }
        if (idata.selectedPacks.contains(p))
          bytes += p.nbytes;

        check.setSelected(idata.selectedPacks.contains(p));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    showSpaceRequired();
  }


  /**  Sets the label text of space requiered for installation.  */
  private void showSpaceRequired()
  {
    StringBuffer result = new StringBuffer(parent.langpack.getString("PacksPanel.space"));
    result.append(Pack.toByteUnitsString(bytes));
    spaceLabel.setText(result.toString());
  }


  /**
   *  Actions-handling method.
   *
   * @param  e  The event.
   */
  public void actionPerformed(ActionEvent e)
  {
    // Initialisations
    Object source = e.getSource();
    JCheckBox checkBox = (JCheckBox) source;
    Pack pack = (Pack) packsforBoxes.get(checkBox);

    // We act depending of the user's choice
    if (checkBox.isSelected())
    {
      idata.selectedPacks.add(pack);
      bytes += pack.nbytes;
    }
    else
    {
      idata.selectedPacks.remove(idata.selectedPacks.indexOf(pack));
      bytes -= pack.nbytes;
    }
    showSpaceRequired();
  }


  /**
   *  Indicates wether the panel has been validated or not.
   *
   * @return    Always true.
   */
  public boolean isValidated()
  {
    return true;
  }


  /**
   *  Asks to make the XML panel data.
   *
   * @param  panelRoot  The XML tree to write the data in.
   */
  public void makeXMLData(XMLElement panelRoot)
  {
    // Selected packs markup
    XMLElement sel = new XMLElement("selected");

    // We add each selected pack to sel
    int size = idata.selectedPacks.size();
    for (int i = 0; i < size; i++)
    {
      XMLElement el = new XMLElement("pack");
      Pack pack = (Pack) idata.selectedPacks.get(i);
      Integer integer = new Integer(idata.availablePacks.indexOf(pack));
      el.setAttribute("index", integer.toString());
      sel.addChild(el);
    }

    // Joining
    panelRoot.addChild(sel);
  }


  /**
   *  Asks to run in the automated mode.
   *
   * @param  panelRoot  The root of the panel data.
   */
  public void runAutomated(XMLElement panelRoot)
  {
    // We get the selected markup
    XMLElement sel = panelRoot.getFirstChildNamed("selected");

    // We get the packs markups
    Vector pm = sel.getChildrenNamed("pack");

    // We select each of them
    int size = pm.size();
    idata.selectedPacks.clear();
    for (int i = 0; i < size; i++)
    {
      XMLElement el = (XMLElement) pm.get(i);
      Integer integer = new Integer(el.getAttribute("index"));
      int index = integer.intValue();
      idata.selectedPacks.add(idata.availablePacks.get(index));
    }
  }
}

