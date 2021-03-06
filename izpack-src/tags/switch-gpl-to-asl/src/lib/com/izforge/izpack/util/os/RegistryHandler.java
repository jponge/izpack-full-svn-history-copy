/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2005 Klaus Bartz
 *
 *  File :               RegistryHandler.java
 *  Description :        The handler for registry related
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coi.tools.os.win.NativeLibException;
import com.coi.tools.os.win.RegistryImpl;
import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.installer.ResourceManager;
import com.izforge.izpack.util.Debug;
import com.izforge.izpack.util.OSClassHelper;

/**
 * This class represents a registry handler in a operating system independent way. 
 * OS specific subclasses are used to implement the necessary mapping from
 * this generic API to the classes that reflect the system dependent AIP.
 * 
 * @author Klaus Bartz
 * 
 */
public class RegistryHandler extends OSClassHelper
{

    public static final String UNINSTALL_ROOT = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\";

    public static final Map ROOT_KEY_MAP = new HashMap();

    protected String uninstallName = null;

    private static final String UNINSTALLER_ICON = "UninstallerIcon";

    private static RegistryHandler defaultHandler = null;
    static
    {
        ROOT_KEY_MAP.put("HKCR", new Integer(RegistryImpl.HKEY_CLASSES_ROOT));
        ROOT_KEY_MAP.put("HKEY_CLASSES_ROOT", new Integer(RegistryImpl.HKEY_CLASSES_ROOT));
        ROOT_KEY_MAP.put("HKCU", new Integer(RegistryImpl.HKEY_CURRENT_USER));
        ROOT_KEY_MAP.put("HKEY_CURRENT_USER", new Integer(RegistryImpl.HKEY_CURRENT_USER));
        ROOT_KEY_MAP.put("HKLM", new Integer(RegistryImpl.HKEY_LOCAL_MACHINE));
        ROOT_KEY_MAP.put("HKEY_LOCAL_MACHINE", new Integer(RegistryImpl.HKEY_LOCAL_MACHINE));
        ROOT_KEY_MAP.put("HKU", new Integer(RegistryImpl.HKEY_USERS));
        ROOT_KEY_MAP.put("HKEY_USERS", new Integer(RegistryImpl.HKEY_USERS));
        ROOT_KEY_MAP.put("HKPD", new Integer(RegistryImpl.HKEY_PERFORMANCE_DATA));
        ROOT_KEY_MAP.put("HKEY_PERFORMANCE_DATA", new Integer(RegistryImpl.HKEY_PERFORMANCE_DATA));
        ROOT_KEY_MAP.put("HKCC", new Integer(RegistryImpl.HKEY_CURRENT_CONFIG));
        ROOT_KEY_MAP.put("HKEY_CURRENT_CONFIG", new Integer(RegistryImpl.HKEY_CURRENT_CONFIG));
        ROOT_KEY_MAP.put("HKDDS", new Integer(RegistryImpl.HKEY_DYN_DATA));
        ROOT_KEY_MAP.put("HKEY_DYN_DATA", new Integer(RegistryImpl.HKEY_DYN_DATA));

    }

    /**
     * Default constructor.
     */
    public RegistryHandler()
    {
        super();
    }

    /**
     * Creates an registry handler which uses an oblect of the given class as worker.
     * 
     * @param className
     *            full qualified class name of the class which should be used as worker
     */
    public RegistryHandler(String className)
    {
        super(className);
        setDefault();
    }

    /**
     * Set this object as default handler if it is not done earlier.
     */
    private synchronized void setDefault()
    {
        if (defaultHandler == null) defaultHandler = this;
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
     */
    public void setValue(String key, String value, String contents) throws NativeLibException
    {
        return;
    }

    public void setValue(String key, String value, String[] contents) throws NativeLibException
    {
        return;
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
        return;
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
        return;
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
     * @throws NativeLibException
     */
    public Object getValue(String key, String value, Object defaultVal) throws NativeLibException
    {
        return (null);
    }

    /**
     * Returns whether a key exist or not.
     * 
     * @param key
     *            key to be evaluated
     * @return whether a key exist or not
     * @throws NativeLibException
     */
    public boolean keyExist(String key) throws NativeLibException
    {
        return (false);
    }

    /**
     * Returns whether a the given value under the given key exist or not.
     * 
     * @param key
     *            key to be used as path for the value
     * @param value
     *            value name to be evaluated
     * @return whether a the given value under the given key exist or not
     * @throws NativeLibException
     */
    public boolean valueExist(String key, String value) throws NativeLibException
    {
        return (false);
    }

    /**
     * Returns all keys which are defined under the given key.
     * 
     * @param key
     *            key to be used as path for the sub keys
     * @return all keys which are defined under the given key
     * @throws NativeLibException
     */
    public String[] getSubkeys(String key) throws NativeLibException
    {
        return (null);
    }

    /**
     * Returns all value names which are defined under the given key.
     * 
     * @param key
     *            key to be used as path for the value names
     * @return all value names which are defined under the given key
     * @throws NativeLibException
     */
    public String[] getValueNames(String key) throws NativeLibException
    {
        return (null);
    }

    /**
     * Returns the contents of the key/value pair if value exist, else an exception is raised.
     * 
     * @param key
     *            the registry key which should be used
     * @param value
     *            the registry value from which the contents should be requested
     * @return requested value if exist, else an exception
     * @throws NativeLibException
     */
    public Object getValue(String key, String value) throws NativeLibException
    {
        return (null);
    }

    /**
     * Creates the given key in the registry.
     * 
     * @param key
     *            key to be created
     * @throws NativeLibException
     */
    public void createKey(String key) throws NativeLibException
    {
        return;
    }

    /**
     * Sets the root for the next registry access.
     * 
     * @param i
     *            an integer which refers to a HKEY
     * @throws NativeLibException
     */
    public void setRoot(int i) throws NativeLibException
    {
        return;
    }

    /**
     * Return the root as integer (HKEY_xxx).
     * 
     * @return the root as integer
     * @throws NativeLibException
     */
    public int getRoot() throws NativeLibException
    {
        return (0);
    }

    /**
     * Activates logging of registry changes.
     * 
     * @throws NativeLibException
     */
    public void activateLogging() throws NativeLibException
    {
        return;
    }

    /**
     * Suspends logging of registry changes.
     * 
     * @throws NativeLibException
     */
    public void suspendLogging() throws NativeLibException
    {
        return;
    }

    /**
     * Resets logging of registry changes.
     * 
     * @throws NativeLibException
     */
    public void resetLogging() throws NativeLibException
    {
        return;
    }

    public List getLoggingInfo() throws NativeLibException
    {
        return (null);
    }

    public void setLoggingInfo(List info) throws NativeLibException
    {
        return;
    }

    public void addLoggingInfo(List info) throws NativeLibException
    {
        return;
    }

    public void rewind() throws NativeLibException
    {
        return;
    }

    public String getUninstallName()
    {
        if (uninstallName != null) return (uninstallName);
        if (installdata == null) return (null);
        return (installdata.getVariable("APP_NAME") + " " + installdata.getVariable("APP_VER"));
    }

    public boolean isProductRegistered() throws NativeLibException
    {
        String uninstallName = getUninstallName();
        if (uninstallName == null) return (false);
        String keyName = UNINSTALL_ROOT + uninstallName;
        int oldVal = getRoot();
        setRoot(RegistryImpl.HKEY_LOCAL_MACHINE);
        boolean retval = keyExist(keyName);
        setRoot(oldVal);
        return (retval);
    }

    public void setUninstallName(String name)
    {
        uninstallName = name;
    }

    public void registerUninstallKey() throws NativeLibException
    {
        String uninstallName = getUninstallName();
        if (uninstallName == null) return;
        String keyName = UNINSTALL_ROOT + uninstallName;
        String cmd = "\"" + installdata.getVariable("JAVA_HOME") + "\\bin\\javaw.exe\" -jar \""
                + installdata.getVariable("INSTALL_PATH") + "\\uninstaller\\uninstaller.jar\"";

        int oldVal = getRoot();
        setRoot(RegistryImpl.HKEY_LOCAL_MACHINE);
        setValue(keyName, "DisplayName", uninstallName);
        setValue(keyName, "UninstallString", cmd);
        // Try to write the uninstaller icon out.
        try
        {
            InputStream input = ResourceManager.getInstance().getInputStream(UNINSTALLER_ICON);
            String iconPath = installdata.getVariable("INSTALL_PATH") + File.separator
                    + "Uninstaller" + File.separator + "UninstallerIcon.ico";
            FileOutputStream out = new FileOutputStream(iconPath);
            byte[] buffer = new byte[5120];
            long bytesCopied = 0;
            int bytesInBuffer;
            while ((bytesInBuffer = input.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesInBuffer);
                bytesCopied += bytesInBuffer;
            }
            input.close();
            out.close();
            setValue(keyName, "DisplayIcon", iconPath);
        }
        catch (Exception exception)
        { // May be no icon resource defined; ignore it
            Debug.trace(exception);
        }
        setRoot(oldVal);
    }

    /**
     * @param idata
     */
    public boolean verify(AutomatedInstallData idata) throws Exception
    {
        super.verify(idata);
        return (true);

    }

    /**
     * Returns whether an action with this handler should be performed or not.
     * 
     * @return always true
     */
    public boolean doPerform()
    {
        return true;
    }

    /**
     * Returns the default handler which is the first created registry handler.
     * 
     * @return Returns the default handler.
     */
    public RegistryHandler getDefaultHandler()
    {
        return defaultHandler;
    }

}