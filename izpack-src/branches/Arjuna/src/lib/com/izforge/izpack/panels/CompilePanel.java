/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001-2003 Julien Ponge, Tino Schwarze
 *
 *  File :               CompilePanel.java
 *  Description :        A panel to compile files after installation
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
package com.izforge.izpack.panels;

import com.izforge.izpack.installer.*;
import com.izforge.izpack.gui.*;
import com.izforge.izpack.util.Debug;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.n3.nanoxml.*;

/**
 *  The compile panel class.
 *
 * This class allows .java files to be compiled after installation.
 *
 * Parts of the code have been taken from InstallPanel.java and
 * modified a lot.
 * 
 * @author     Tino Schwarze
 * @author     Julien Ponge
 * @created    May 2003
 */
public class CompilePanel extends IzPanel implements ActionListener, CompileListener
{
  /**  The combobox for compiler selection. */
  protected JComboBox compilerComboBox;

  /**  The combobox for compiler argument selection. */
  protected JComboBox argumentsComboBox;

  /**  The start button. */
  protected JButton startButton;

  /**  The tip label. */
  protected JLabel tipLabel;

  /**  The operation label . */
  protected JLabel opLabel;

  /**  The progress bar. */
  protected JProgressBar progressBar;

  /**  True if the compilation has been done. */
  private volatile boolean validated = false;

  private CompileWorker worker;

  /**
   *  The constructor.
   *
   * @param  parent  The parent window.
   * @param  idata   The installation data.
   */
  public CompilePanel(InstallerFrame parent, InstallData idata) 
    throws IOException
  {
    super(parent, idata);

    this.worker = new CompileWorker (idata, this);

    GridBagConstraints gridBagConstraints;

    JLabel heading = new JLabel();
    JLabel compilerLabel = new JLabel();
    compilerComboBox = new JComboBox();
    JLabel argumentsLabel = new JLabel();
    argumentsComboBox = new JComboBox();
    startButton = ButtonFactory.createButton (parent.langpack.getString ("CompilePanel.start"), idata.buttonsHColor);
    tipLabel = new JLabel(parent.langpack.getString ("CompilePanel.tip"),
        parent.icons.getImageIcon ("tip"), JLabel.TRAILING);
    opLabel = new JLabel();
    progressBar = new JProgressBar();

    setLayout(new GridBagLayout());

    Font font = heading.getFont ();
    font = font.deriveFont (Font.BOLD, font.getSize()*2.0f);
    heading.setFont(font);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setText(parent.langpack.getString ("CompilePanel.heading"));
    heading.setVerticalAlignment(SwingConstants.TOP);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    gridBagConstraints.weighty = 0.1;
    add(heading, gridBagConstraints);

    compilerLabel.setHorizontalAlignment(SwingConstants.LEFT);
    compilerLabel.setLabelFor(compilerComboBox);
    compilerLabel.setText(parent.langpack.getString ("CompilePanel.choose_compiler"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weighty = 0.1;
    add(compilerLabel, gridBagConstraints);

    compilerComboBox.setEditable(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weighty = 0.1;

    Iterator it = this.worker.getAvailableCompilers().iterator();

    while (it.hasNext())
      compilerComboBox.addItem ((String)it.next());
    
    add(compilerComboBox, gridBagConstraints);

    argumentsLabel.setHorizontalAlignment(SwingConstants.LEFT);
    argumentsLabel.setLabelFor(argumentsComboBox);
    argumentsLabel.setText(parent.langpack.getString ("CompilePanel.additional_arguments"));
    //argumentsLabel.setToolTipText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.weighty = 0.1;
    add(argumentsLabel, gridBagConstraints);

    argumentsComboBox.setEditable(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.weighty = 0.1;

    it = this.worker.getAvailableArguments ().iterator();

    while (it.hasNext())
      argumentsComboBox.addItem ((String)it.next());
    
    add(argumentsComboBox, gridBagConstraints);

    startButton.setText(parent.langpack.getString ("CompilePanel.start"));
    startButton.addActionListener (this);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weighty = 0.1;
    add(startButton, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.NONE;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    add(tipLabel, gridBagConstraints);

    opLabel.setText(" ");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    add(opLabel, gridBagConstraints);

    progressBar.setValue(0);
    progressBar.setString(parent.langpack.getString ("CompilePanel.progress.initial"));
    progressBar.setStringPainted(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.SOUTH;
    add(progressBar, gridBagConstraints);
  }


  /**
   *  Indicates wether the panel has been validated or not.
   *
   * @return    The validation state.
   */
  public boolean isValidated()
  {
    return validated;
  }

  /**
   *  Action function, called when the start button is pressed.
   */
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource() == this.startButton)
    {
      // disable all controls
      this.startButton.setEnabled (false);

      this.worker.setCompiler ((String)this.compilerComboBox.getSelectedItem ());
      this.compilerComboBox.setEnabled (false);

      this.worker.setCompilerArguments ((String)this.argumentsComboBox.getSelectedItem ());
      this.argumentsComboBox.setEnabled (false);

      parent.blockGUI();
      worker.startThread ();
    }
  }

  /**
   *  An error was encountered.
   *
   * @param  error  The error text.
   */
  public void errorCompile (String error)
  {
    opLabel.setText(error);
    idata.installSuccess = false;
    JOptionPane.showMessageDialog(this, error.toString(),
      parent.langpack.getString("CompilePanel.error"),
      JOptionPane.ERROR_MESSAGE);
  }


  /**  The compiler starts.  */
  public void startCompilation ()
  {
  }

  /**  The compiler stops.  */
  public void stopCompilation ()
  {
    parent.releaseGUI();
    parent.lockPrevButton();
    progressBar.setString(parent.langpack.getString("CompilePanel.progress.finished"));
    progressBar.setEnabled(false);
    progressBar.setValue (progressBar.getMaximum());
    opLabel.setText(" ");
    opLabel.setEnabled(false);
    validated = true;
    if (idata.panels.indexOf(this) != (idata.panels.size() - 1))
      parent.unlockNextButton();
  }


  /**
   *  Normal progress indicator.
   *
   * @param  val  The progression value.
   * @param  msg  The progression message.
   */
  public void progressCompile (int val, String msg)
  {
    Debug.trace ("progress: " + val + " " + msg);
    progressBar.setValue(val + 1);
    opLabel.setText(msg);
  }


  /**
   *  Job changing.
   *
   * @param  min       The new mnimum progress.
   * @param  max       The new maximum progress.
   * @param  jobName   The job name.
   */
  public void changeCompileJob (int min, int max, String jobName)
  {
    progressBar.setValue(0);
    progressBar.setMinimum(min);
    progressBar.setMaximum(max);
    progressBar.setString(jobName);
    opLabel.setText ("");
  }


  /**  Called when the panel becomes active.  */
  public void panelActivate()
  {
    // We clip the panel
    /* XXX: what's that good for?
    Dimension dim = parent.getPanelsContainerSize();
    dim.width = dim.width - (dim.width / 4);
    dim.height = 150;
    setMinimumSize(dim);
    setMaximumSize(dim);
    setPreferredSize(dim);
    */
    parent.lockNextButton();
  }

  /** Create XML data for automated installation. */
  public void makeXMLData (XMLElement panelRoot)
  {
    // just save the compiler chosen and the arguments
    XMLElement compiler = new XMLElement ("compiler");
    compiler.setContent (this.worker.getCompiler());
    panelRoot.addChild (compiler);

    XMLElement args = new XMLElement ("arguments");
    args.setContent (this.worker.getCompilerArguments());
    panelRoot.addChild (args);
  }

}

