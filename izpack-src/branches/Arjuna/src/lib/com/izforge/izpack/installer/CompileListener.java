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
package com.izforge.izpack.installer;

/**
 *  Interface for monitoring compilation progress.
 *
 * This is used by <code>CompilePanel</code>, <code>CompileWorker</code> and
 * <code>CompilePanelAutomationHelper</code> to display the progress of the
 * compilation.
 *
 * @author     Tino Schwarze
 * @created    May 2003
 */
public interface CompileListener
{
  /**
   *  An error was encountered.
   *
   * This is called when the compiler was not found or could
   * not compile a file.
   *
   * @param message  A short error text.
   * @param cmdline The complete command line of the failed command.
   * @param stdout The stdout of the failed command.
   * @param stderr The stderr of the failed command.
   * @return whether to abort compilation (true = abort, false = continue)
   */
  public boolean errorCompile (
     String message, String[] cmdline, String stdout, String stderr);

  /**  The compiler starts. 
   *
   * @param noOfJobs   The number of jobs to compile.
   *
   */
  public void startCompilation (int noOfJobs);

  /**  The compiler stops.  */
  public void stopCompilation ();

  /**
   *  Normal progress indicator.
   *
   * @param  val  The progression value.
   * @param  msg  The progression message.
   */
  public void progressCompile (int val, String msg);

  /**
   *  Job changing.
   *
   * @param  min       The new mnimum progress.
   * @param  max       The new maximum progress.
   * @param  jobName   The job name.
   * @param  jobNo     The job number.
   */
  public void changeCompileJob (int min, int max, String jobName, int jobNo);

}

