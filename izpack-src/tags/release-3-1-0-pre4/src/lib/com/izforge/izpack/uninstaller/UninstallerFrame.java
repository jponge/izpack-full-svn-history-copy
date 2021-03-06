/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001,2002 Julien Ponge
 *
 *  File :               UninstallerFrame.java
 *  Description :        The uninstaller frame class.
 *  Author's email :     julien@izforge.com
 *  Author's Website :   http://www.izforge.com
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
package com.izforge.izpack.uninstaller;

import com.izforge.izpack.*;
import com.izforge.izpack.gui.*;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 *  The uninstaller frame class.
 *
 * @author     Julien Ponge
 * @created    November 1, 2002
 */
public class UninstallerFrame extends JFrame
{
  /**  The icons database. */
  private IconsDatabase icons;

  /**  The language pack. */
  private LocaleDatabase langpack;

  /**  The warning label. */
  private JLabel warningLabel;

  /**  The target destroy checkbox. */
  private JCheckBox targetDestroyCheckbox;

  /**  The progress bar. */
  private JProgressBar progressBar;

  /**  The destroy button. */
  private JButton destroyButton;

  /**  The quit button. */
  private JButton quitButton;

  /**  The layout. */
  private GridBagLayout layout;

  /**  the layout constraints. */
  private GridBagConstraints gbConstraints;

  /**  The buttons hover color. */
  private Color buttonsHColor = new Color(230, 230, 230);

  /**  The installation path. */
  private String installPath;


  /**
   *  The constructor.
   *
   * @exception  Exception  Description of the Exception
   */
  public UninstallerFrame() throws Exception
  {
    super("IzPack - Uninstaller");

    // Initializations
    langpack = new LocaleDatabase(getClass().getResourceAsStream("/langpack.xml"));
    getInstallPath();
    icons = new IconsDatabase();
    loadIcons();
    UIManager.put("OptionPane.yesButtonText", langpack.getString("installer.yes"));
    UIManager.put("OptionPane.noButtonText", langpack.getString("installer.no"));
    UIManager.put("OptionPane.cancelButtonText", langpack.getString("installer.cancel"));

    // Sets the frame icon
    setIconImage(icons.getImageIcon("JFrameIcon").getImage());

    // We build the GUI & show it
    buildGUI();
    addWindowListener(new WindowHandler());
    pack();
    centerFrame(this);
    setResizable(false);
    setVisible(true);
  }


  /**  Builds the GUI.  */
  private void buildGUI()
  {
    // We initialize our layout
    JPanel contentPane = (JPanel) getContentPane();
    layout = new GridBagLayout();
    contentPane.setLayout(layout);
    gbConstraints = new GridBagConstraints();
    gbConstraints.insets = new Insets(5, 5, 5, 5);

    // We prepare our action handler
    ActionsHandler handler = new ActionsHandler();

    // Prepares the glass pane to block gui interaction when needed
    JPanel glassPane = (JPanel) getGlassPane();
    glassPane.addMouseListener(
      new MouseAdapter()
      {
      });
    glassPane.addMouseMotionListener(
      new MouseMotionAdapter()
      {
      });
    glassPane.addKeyListener(
      new KeyAdapter()
      {
      });

    // We put our components

    warningLabel = new JLabel(langpack.getString("uninstaller.warning"),
      icons.getImageIcon("warning"),
      JLabel.TRAILING);
    buildConstraints(gbConstraints, 0, 0, 2, 1, 1.0, 0.0);
    gbConstraints.anchor = GridBagConstraints.WEST;
    gbConstraints.fill = GridBagConstraints.NONE;
    layout.addLayoutComponent(warningLabel, gbConstraints);
    contentPane.add(warningLabel);

    targetDestroyCheckbox = new JCheckBox(langpack.getString("uninstaller.destroytarget")
       + installPath,
      false);
    buildConstraints(gbConstraints, 0, 1, 2, 1, 1.0, 0.0);
    layout.addLayoutComponent(targetDestroyCheckbox, gbConstraints);
    contentPane.add(targetDestroyCheckbox);
    gbConstraints.fill = GridBagConstraints.HORIZONTAL;

    progressBar = new JProgressBar();
    progressBar.setStringPainted(true);
    progressBar.setString(langpack.getString("InstallPanel.begin"));
    buildConstraints(gbConstraints, 0, 2, 2, 1, 1.0, 0.0);
    layout.addLayoutComponent(progressBar, gbConstraints);
    contentPane.add(progressBar);

    destroyButton = ButtonFactory.createButton(langpack.getString("uninstaller.uninstall"),
      icons.getImageIcon("delete"),
      buttonsHColor);
    destroyButton.addActionListener(handler);
    buildConstraints(gbConstraints, 0, 3, 1, 1, 0.5, 0.0);
    gbConstraints.fill = GridBagConstraints.NONE;
    gbConstraints.anchor = GridBagConstraints.WEST;
    layout.addLayoutComponent(destroyButton, gbConstraints);
    contentPane.add(destroyButton);

    quitButton = ButtonFactory.createButton(langpack.getString("installer.quit"),
      icons.getImageIcon("stop"),
      buttonsHColor);
    quitButton.addActionListener(handler);
    buildConstraints(gbConstraints, 1, 3, 1, 1, 0.5, 0.0);
    gbConstraints.anchor = GridBagConstraints.EAST;
    layout.addLayoutComponent(quitButton, gbConstraints);
    contentPane.add(quitButton);

  }


  /**
   *  Centers a window on screen.
   *
   * @param  frame  The window to center.
   */
  private void centerFrame(Window frame)
  {
    Dimension frameSize = frame.getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((screenSize.width - frameSize.width) / 2,
      (screenSize.height - frameSize.height) / 2 - 10);
  }


  /**
   *  Sets the parameters of a GridBagConstraints object.
   *
   * @param  gbc  The constraints object.
   * @param  gx   The x coordinates.
   * @param  gy   The y coordinates.
   * @param  gw   The width.
   * @param  wx   The x wheight.
   * @param  wy   The y wheight.
   * @param  gh   Description of the Parameter
   */
  private void buildConstraints(GridBagConstraints gbc,
                                int gx, int gy, int gw, int gh, double wx, double wy)
  {
    gbc.gridx = gx;
    gbc.gridy = gy;
    gbc.gridwidth = gw;
    gbc.gridheight = gh;
    gbc.weightx = wx;
    gbc.weighty = wy;
  }


  /**
   *  Gets the installation path from the log file.
   *
   * @exception  Exception  Description of the Exception
   */
  private void getInstallPath() throws Exception
  {
    InputStream in = getClass().getResourceAsStream("/install.log");
    InputStreamReader inReader = new InputStreamReader(in);
    BufferedReader reader = new BufferedReader(inReader);
    installPath = reader.readLine();
    reader.close();
  }


  /**
   *  Loads the icons.
   *
   * @exception  Exception  Description of the Exception
   */
  private void loadIcons() throws Exception
  {
    // Initialisations
    icons = new IconsDatabase();
    URL url;
    ImageIcon img;

    // We load it
    url = getClass().getResource("/img/delete.gif");
    img = new ImageIcon(url);
    icons.put("delete", img);

    url = getClass().getResource("/img/stop.gif");
    img = new ImageIcon(url);
    icons.put("stop", img);

    url = getClass().getResource("/img/warning.gif");
    img = new ImageIcon(url);
    icons.put("warning", img);

    url = getClass().getResource("/img/JFrameIcon.gif");
    img = new ImageIcon(url);
    icons.put("JFrameIcon", img);
  }


  /**  Blocks GUI interaction.  */
  public void blockGUI()
  {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    getGlassPane().setVisible(true);
    getGlassPane().setEnabled(true);
  }


  /**  Releases GUI interaction.  */
  public void releaseGUI()
  {
    getGlassPane().setEnabled(false);
    getGlassPane().setVisible(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }


  /**
   *  The window events handler.
   *
   * @author     julien
   * @created    November 1, 2002
   */
  class WindowHandler extends WindowAdapter
  {
    /**
     *  We can't avoid the exit here ... so don't call exit.
     *
     * @param  e  The event.
     */
    public void windowClosing(WindowEvent e)
    {
      System.exit(0);
    }
  }


  /**
   *  The destroyer handler.
   *
   * @author     julien
   * @created    November 1, 2002
   */
  class DestroyerHandler implements DestroyerListener
  {
    /**
     *  The destroyer starts.
     *
     * @param  min  The minimum value of the progress.
     * @param  max  The maximum value of the progress.
     */
    public void destroyerStart(int min, int max)
    {
      progressBar.setMinimum(min);
      progressBar.setMaximum(max);
      blockGUI();
    }


    /**  The destroyer stops.  */
    public void destroyerStop()
    {
      progressBar.setString(langpack.getString("InstallPanel.finished"));
      targetDestroyCheckbox.setEnabled(false);
      destroyButton.setEnabled(false);
      releaseGUI();
    }


    /**
     *  The destroyer progresses.
     *
     * @param  pos      The actual position.
     * @param  message  The message.
     */
    public void destroyerProgress(int pos, String message)
    {
      progressBar.setValue(pos);
      progressBar.setString(message);
    }


    /**
     *  The destroyer encountered an error.
     *
     * @param  error  The error message.
     */
    public void destroyerError(String error)
    {
      progressBar.setString(error);
      JOptionPane.showMessageDialog(null, error.toString(),
        langpack.getString("installer.error"),
        JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   *  The actions events handler.
   *
   * @author     julien
   * @created    November 1, 2002
   */
  class ActionsHandler implements ActionListener
  {
    /**
     *  Action handling method.
     *
     * @param  e  The event.
     */
    public void actionPerformed(ActionEvent e)
    {
      Object src = e.getSource();
      if (src == quitButton)
        System.exit(0);
      else
        if (src == destroyButton)
      {
        Destroyer destroyer = new Destroyer(installPath,
          targetDestroyCheckbox.isSelected(),
          new DestroyerHandler());
        destroyer.start();
      }
    }
  }
}

