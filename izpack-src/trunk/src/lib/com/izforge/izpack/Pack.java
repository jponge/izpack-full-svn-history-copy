/*
 * IzPack - Copyright 2001-2006 Julien Ponge, All Rights Reserved.
 * 
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.izforge.izpack.compiler.PackInfo;

/**
 * Represents a Pack.
 * 
 * @author Julien Ponge
 */
public class Pack implements Serializable
{

    static final long serialVersionUID = -5458360562175088671L;

    public boolean loose;

    /** The pack name. */
    public String name;

    /** The langpack id */
    public String id;

    /** An association of this pack to zero or more installation groups. An
     * installation group is just a named collection of packs to allow for
     * different pack collections to be selected, for example: minimal,
     * default, all.
     */
    public Set installGroups = new HashSet();

    /** The group the pack is associated with. The pack group identifies
     * packs with common functionality to allow for grouping of packs in a
     * tree in the TargetPanel for example.
     */
    public String group;

    /** The pack description. */
    public String description;

    /** The target operation system of this pack */
    public List osConstraints = null;

    /** The list of packs this pack depends on */
    public List dependencies = null;

    /** Reverse dependencies(childs) */
    public List revDependencies = null;

    /** True if the pack is required. */
    public boolean required;

    /** The bumber of bytes contained in the pack. */
    public long nbytes;

    /** Whether this pack is suggested (preselected for installation). */
    public boolean preselected;

    /** The color of the node. This is used for the dependency graph algorithms */
    public int color;

    /** white colour */
    public final static int WHITE = 0;

    /** grey colour */
    public final static int GREY = 1;

    /** black colour */
    public final static int BLACK = 2;

    /**
     * The constructor.
     * 
     * @param name The pack name.
     * @param description The pack description.
     * @param osConstraints the OS constraint (or null for any OS)
     * @param required Indicates wether the pack is required or not.
     * @param preselected This pack will be selected automatically.
     */
    public Pack(String name, String id, String description, List osConstraints, List dependencies,
            boolean required, boolean preselected, boolean loose)
    {
        this.name = name;
        this.id = id;
        this.description = description;
        this.osConstraints = osConstraints;
        this.dependencies = dependencies;
        this.required = required;
        this.preselected = preselected;
        this.loose = loose;
        nbytes = 0;
        color = PackInfo.WHITE;
    }

    /**
     * To a String (usefull for JLists).
     * 
     * @return The String representation of the pack.
     */
    public String toString()
    {
        return name + " (" + description + ")";
    }

    /** getter method */
    public List getDependencies()
    {
        return dependencies;
    }

    /**
     * This adds a reverse dependency. With a reverse dependency we imply a child dependency or the
     * dependents on this pack
     * 
     * @param name The name of the pack that depents to this pack
     */
    public void addRevDep(String name)
    {
        if (revDependencies == null) revDependencies = new ArrayList();
        revDependencies.add(name);
    }

    /**
     * Creates a text list of all the packs it depend on
     * 
     * @return the created text
     */
    public String depString()
    {
        String text = "";
        if (dependencies == null) return text;
        String name;
        for (int i = 0; i < dependencies.size() - 1; i++)
        {
            name = (String) dependencies.get(i);
            text += name + ",";
        }
        name = (String) dependencies.get(dependencies.size() - 1);
        text += name;
        return text;

    }

    /** Used of conversions. */
    private final static double KILOBYTES = 1024.0;

    /** Used of conversions. */
    private final static double MEGABYTES = 1024.0 * 1024.0;

    /** Used of conversions. */
    private final static double GIGABYTES = 1024.0 * 1024.0 * 1024.0;

    /** Used of conversions. */
    private final static DecimalFormat formatter = new DecimalFormat("#,###.##");

    /**
     * Convert bytes into appropiate mesaurements.
     * 
     * @param bytes A number of bytes to convert to a String.
     * @return The String-converted value.
     */
    public static String toByteUnitsString(long bytes)
    {
        if (bytes < KILOBYTES)
            return String.valueOf(bytes) + " bytes";
        else if (bytes < (MEGABYTES))
        {
            double value = bytes / KILOBYTES;
            return formatter.format(value) + " KB";
        }
        else if (bytes < (GIGABYTES))
        {
            double value = bytes / MEGABYTES;
            return formatter.format(value) + " MB";
        }
        else
        {
            double value = bytes / GIGABYTES;
            return formatter.format(value) + " GB";
        }
    }
}
