/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2005 Klaus Bartz
 *
 *  File :               Win_RegistryHandler.java
 *  Description :        The OS specific handler for registry related
 *                       stuff at installation time (Active only on windows). 
 *  Author's email :     bartzkau@users.berlios.de
 *  Website :            http://www.izforge.com
 * 
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
package com.izforge.izpack.util.os;

import java.util.List;

import com.coi.tools.os.izpack.Registry;
import com.coi.tools.os.win.NativeLibException;

/**
 * This is the Microsoft Windows specific implementation of <code>RegistryHandler</code>.
 * 
 * @author bartzkau
 *  
 */
public class Win_RegistryHandler extends RegistryHandler
{

    Registry regWorker = null;

    /**
     * Default constructor.
     */
    public Win_RegistryHandler()
    {
        super("com.coi.tools.os.izpack.Registry");
        if (good()) regWorker = (Registry) worker;
    }

    /**
     * Sets the given contents to the given registry value. If a sub key or the registry value does
     * not exist, it will be created. The return value is a String array which contains the names of
     * the keys and values which are created. REG_SZ is used as registry value type.
     * 
     * @param key
     *            the registry key which should be used or created
     * @param value
     *            the registry value into which the contents should be set
     * @param contents
     *            the contents for the value
     * @return an string array which contains the names of the keys and values which are created
     * @throws NativeLibException
     * @throws NativeLibException
     */
    public void setValue(String key, String value, String contents) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setValue(key, value, contents);
    }

    /**
     * Sets the given contents to the given registry value. If a sub key or the registry value does
     * not exist, it will be created. The return value is a String array which contains the names of
     * the keys and values which are created. REG_MULTI_SZ is used as registry value type.
     * 
     * @param key
     *            the registry key which should be used or created
     * @param value
     *            the registry value into which the contents should be set
     * @param contents
     *            the contents for the value
     * @return an string array which contains the names of the keys and values which are created
     * @throws NativeLibException
     */
    public void setValue(String key, String value, String[] contents) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setValue(key, value, contents);
    }

    /**
     * Sets the given contents to the given registry value. If a sub key or the registry value does
     * not exist, it will be created. The return value is a String array which contains the names of
     * the keys and values which are created. REG_BINARY is used as registry value type.
     * 
     * @param key
     *            the registry key which should be used or created
     * @param value
     *            the registry value into which the contents should be set
     * @param contents
     *            the contents for the value
     * @return an string array which contains the names of the keys and values which are created
     * @throws NativeLibException
     */
    public void setValue(String key, String value, byte[] contents) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setValue(key, value, contents);
    }

    /**
     * Sets the given contents to the given registry value. If a sub key or the registry value does
     * not exist, it will be created. The return value is a String array which contains the names of
     * the keys and values which are created. REG_DWORD is used as registry value type.
     * 
     * @param key
     *            the registry key which should be used or created
     * @param value
     *            the registry value into which the contents should be set
     * @param contents
     *            the contents for the value
     * @return an string array which contains the names of the keys and values which are created
     * @throws NativeLibException
     */
    public void setValue(String key, String value, long contents) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setValue(key, value, contents);
    }

    /**
     * Returns the contents of the key/value pair if value exist, else the given default value.
     * 
     * @param key
     *            the registry key which should be used
     * @param value
     *            the registry value from which the contents should be requested
     * @param defaultVal
     *            value to be used if no value exist in the registry
     * @return requested value if exist, else the default value
     * @throws Exception
     */
    public Object getValue(String key, String value, Object defaultVal) throws NativeLibException
    {
        if (!good()) return (null);
        if (valueExist(key, value)) return (getValue(key, value));
        return (defaultVal);
    }

    /**
     * Returns whether a key exist or not.
     * 
     * @param key
     *            key to be evaluated
     * @return whether a key exist or not
     * @throws Exception
     */
    public boolean keyExist(String key) throws NativeLibException
    {
        if (!good()) return (false);
        return (regWorker.keyExist(key));
    }

    /**
     * Returns whether a the given value under the given key exist or not.
     * 
     * @param key
     *            key to be used as path for the value
     * @param value
     *            value name to be evaluated
     * @return whether a the given value under the given key exist or not
     * @throws Exception
     */
    public boolean valueExist(String key, String value) throws NativeLibException
    {
        if (!good()) return (false);
        return (regWorker.valueExist(key, value));
    }

    /**
     * Returns all keys which are defined under the given key.
     * 
     * @param key
     *            key to be used as path for the sub keys
     * @return all keys which are defined under the given key
     * @throws Exception
     */
    public String[] getSubkeys(String key) throws NativeLibException
    {
        if (!good()) return (null);
        return (regWorker.getSubkeys(key));
    }

    /**
     * Returns all value names which are defined under the given key.
     * 
     * @param key
     *            key to be used as path for the value names
     * @return all value names which are defined under the given key
     * @throws Exception
     */
    public String[] getValueNames(String key) throws NativeLibException
    {
        if (!good()) return (null);
        return (regWorker.getValueNames(key));
    }

    /**
     * Returns the contents of the key/value pair if value exist, else an exception is raised.
     * 
     * @param key
     *            the registry key which should be used
     * @param value
     *            the registry value from which the contents should be requested
     * @return requested value if exist, else an exception
     * @throws Exception
     */
    public Object getValue(String key, String value) throws NativeLibException
    {
        if (!good()) return (null);
        return (regWorker.getValue(key, value));
    }

    /**
     * Creates the given key in the registry.
     * 
     * @param key
     *            key to be created
     * @throws Exception
     */
    public void createKey(String key) throws NativeLibException
    {
        if (!good()) return;
        regWorker.createKey(key);
    }

    /**
     * Sets the root for the next registry access.
     * 
     * @param i
     *            an integer which refers to a HKEY
     * @throws Exception
     */
    public void setRoot(int i) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setRoot(i);
    }

    /**
     * Return the root as integer (HKEY_xxx).
     * 
     * @return the root as integer
     * @throws Exception
     */
    public int getRoot() throws NativeLibException
    {
        if (!good()) return (0);
        return (regWorker.getRoot());
    }

    /**
     * Activates logging of registry changes.
     * 
     * @throws Exception
     */
    public void activateLogging() throws NativeLibException
    {
        if (!good()) return;
        regWorker.activateLogging();
    }

    /**
     * Suspends logging of registry changes.
     * 
     * @throws Exception
     */
    public void suspendLogging() throws NativeLibException
    {
        if (!good()) return;
        regWorker.suspendLogging();
    }

    /**
     * Resets logging of registry changes.
     * 
     * @throws Exception
     */
    public void resetLogging() throws NativeLibException
    {
        if (!good()) return;
        regWorker.resetLogging();
    }

    public List getLoggingInfo() throws NativeLibException
    {
        if (!good()) return (null);
        return (regWorker.getLoggingInfo());
    }

    public void setLoggingInfo(List info) throws NativeLibException
    {
        if (!good()) return;
        regWorker.setLoggingInfo(info);
    }

    public void addLoggingInfo(List info) throws NativeLibException
    {
        if (!good()) return;
        regWorker.addLoggingInfo(info);
    }

    public void rewind() throws NativeLibException
    {
        if (!good()) return;
        regWorker.rewind();
    }

}