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

  /**  The pack progress bar. */
  protected JProgressBar packProgressBar;

  /**  The operation label . */
  protected JLabel overallLabel;

  /**  The overall progress bar. */
  protected JProgressBar overallProgressBar;

  /**  True if the compilation has been done. */
  private volatile boolean validated = false;

  /**  The compilation worker. Does all the work. */
  private CompileWorker worker;

  /**  Number of jobs to compile. Used for progress indication. */
  private int noOfJobs;

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
    // put everything but the heading into it's own panel
    // (to center it vertically)
    JPanel subpanel = new JPanel ();
    JLabel compilerLabel = new JLabel();
    compilerComboBox = new JComboBox();
    JLabel argumentsLabel = new JLabel();
    this.argumentsComboBox = new JComboBox();
    this.startButton = ButtonFactory.createButton (parent.langpack.getString ("CompilePanel.start"), idata.buttonsHColor);
    this.tipLabel = new JLabel(parent.langpack.getString ("CompilePanel.tip"),
        parent.icons.getImageIcon ("tip"), JLabel.TRAILING);
    this.opLabel = new JLabel();
    packProgressBar = new JProgressBar();
    this.overallLabel = new JLabel();
    this.overallProgressBar = new JProgressBar();

    setLayout(new GridBagLayout());

    Font font = heading.getFont ();
    font = font.deriveFont (Font.BOLD, font.getSize()*2.0f);
    heading.setFont(font);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setText(parent.langpack.getString ("CompilePanel.heading"));
    heading.setVerticalAlignment(SwingConstants.TOP);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.1;
    add(heading, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.CENTER;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 0.9;
    add (subpanel, gridBagConstraints);

    subpanel.setLayout(new GridBagLayout());

    int row = 0;

    compilerLabel.setHorizontalAlignment(SwingConstants.LEFT);
    compilerLabel.setLabelFor(compilerComboBox);
    compilerLabel.setText(parent.langpack.getString ("CompilePanel.choose_compiler"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    //gridBagConstraints.weighty = 0.1;
    subpanel.add(compilerLabel, gridBagConstraints);

    compilerComboBox.setEditable(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    //gridBagConstraints.weighty = 0.1;

    Iterator it = this.worker.getAvailableCompilers().iterator();

    while (it.hasNext())
      compilerComboBox.addItem ((String)it.next());
    
    subpanel.add(compilerComboBox, gridBagConstraints);

    argumentsLabel.setHorizontalAlignment(SwingConstants.LEFT);
    argumentsLabel.setLabelFor(argumentsComboBox);
    argumentsLabel.setText(parent.langpack.getString ("CompilePanel.additional_arguments"));
    //argumentsLabel.setToolTipText("");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    //gridBagConstraints.weighty = 0.1;
    subpanel.add(argumentsLabel, gridBagConstraints);

    argumentsComboBox.setEditable(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    //gridBagConstraints.weighty = 0.1;

    it = this.worker.getAvailableArguments ().iterator();

    while (it.hasNext())
      argumentsComboBox.addItem ((String)it.next());
    
    subpanel.add(argumentsComboBox, gridBagConstraints);

    // leave some space above the label
    gridBagConstraints.insets = new Insets (10, 0, 0, 0);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.NONE;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    subpanel.add(tipLabel, gridBagConstraints);

    opLabel.setText(" ");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    subpanel.add(opLabel, gridBagConstraints);

    packProgressBar.setValue(0);
    packProgressBar.setString(parent.langpack.getString ("CompilePanel.progress.initial"));
    packProgressBar.setStringPainted(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.SOUTH;
    subpanel.add(packProgressBar, gridBagConstraints);

    overallLabel.setText (parent.langpack.getString ("CompilePanel.progress.overall"));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    subpanel.add(overallLabel, gridBagConstraints);

    overallProgressBar.setValue(0);
    overallProgressBar.setString("");
    overallProgressBar.setStringPainted(true);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridy = row++;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.SOUTH;
    subpanel.add(overallProgressBar, gridBagConstraints);

    startButton.setText(parent.langpack.getString ("CompilePanel.start"));
    startButton.addActionListener (this);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.gridy = row++;
    gridBagConstraints.fill = GridBagConstraints.NONE;
    // leave some space above the button
    gridBagConstraints.insets = new Insets (5, 0, 0, 0);
    subpanel.add(startButton, gridBagConstraints);
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
  public void startCompilation (int noOfJobs)
  {
    this.noOfJobs = noOfJobs;
    overallProgressBar.setMaximum (noOfJobs);
  }


  /**  The compiler stops.  */
  public void stopCompilation ()
  {
    parent.releaseGUI();
    parent.lockPrevButton();

    packProgressBar.setString(parent.langpack.getString("CompilePanel.progress.finished"));
    packProgressBar.setEnabled(false);
    packProgressBar.setValue (packProgressBar.getMaximum());

    overallProgressBar.setValue (this.noOfJobs);
    String no_of_jobs = Integer.toString (this.noOfJobs);
    overallProgressBar.setString (no_of_jobs + " / " + no_of_jobs);
    overallProgressBar.setEnabled (false);

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
    //Debug.trace ("progress: " + val + " " + msg);
    packProgressBar.setValue(val + 1);
    opLabel.setText(msg);
  }


  /**
   *  Job changing.
   *
   * @param  min       The new mnimum progress.
   * @param  max       The new maximum progress.
   * @param  jobName   The job name.
   * @param  jobNo     The job number.
   */
  public void changeCompileJob (int min, int max, String jobName, int jobNo)
  {
    packProgressBar.setValue(0);
    packProgressBar.setMinimum(min);
    packProgressBar.setMaximum(max);
    packProgressBar.setString(jobName);

    opLabel.setText ("");

    overallProgressBar.setValue (jobNo);
    overallProgressBar.setString (Integer.toString (jobNo) + " / " + Integer.toString (this.noOfJobs));
  }


  /**  Called when the panel becomes active.  */
  public void panelActivate()
  {
    // We clip the panel
    Dimension dim = parent.getPanelsContainerSize();
    dim.width = dim.width - (dim.width / 4);
    dim.height = 150;
    setMinimumSize(dim);
    setMaximumSize(dim);
    setPreferredSize(dim);
    
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

