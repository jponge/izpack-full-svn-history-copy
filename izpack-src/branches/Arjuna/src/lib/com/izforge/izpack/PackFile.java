/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001 Johannes Lehtinen
 *
 *  File :               Pack.java
 *  Description :        Contains informations about a pack file.
 *  Author's email :     johannes.lehtinen@iki.fi
 *  Author's Website :   http://www.iki.fi/jle/
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
package com.izforge.izpack;

import java.io.Serializable;

/**
 *  Encloses information about a packed file. This class abstracts the way file
 *  data is stored to package.
 *
 * @author     Johannes Lehtinen <johannes.lehtinen@iki.fi>
 * @created    October 26, 2002
 */
public class PackFile implements Serializable
{
  /**  The full path name of the target file */
  public String targetPath = null;

  /**  The target operation system of this file */
  public String os = null;

  /**  The length of the file in bytes */
  public long length = 0;

  /**  The last-modification time of the file. */
  public long mtime = -1;

  public static final int OVERRIDE_FALSE = 0;
  public static final int OVERRIDE_TRUE = 1;
  public static final int OVERRIDE_ASK_FALSE = 2;
  public static final int OVERRIDE_ASK_TRUE = 3;

  /**  Whether or not this file is going to override any existing ones */
  public int override = OVERRIDE_FALSE;


  /**  Constructs a new uninitialized instance. */
  public PackFile() { }


  /**
   *  Constructs and initializes a new instance.
   *
   * @param  length      the length of the file
   * @param  targetPath  Description of the Parameter
   */
  public PackFile(String targetPath, long length)
  {
    this.targetPath = targetPath;
    this.length = length;
  }


  /**
   *  Constructs and initializes a new instance.
   *
   * @param  targetPath  the path to install the file to
   * @param  targetOs    OS parameter
   * @param  length      the length of the file
   * @param  mtime       the last modification time of the file
   * @param  override    what to do when the file already exists
   */
  public PackFile(String targetPath, String targetOs, 
                  long length, long mtime, int override)
  {
    this.targetPath = targetPath;
    this.length = length;
    this.os = targetOs;
    this.override = override;
  }
}

