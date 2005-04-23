/*
 * Created on Apr 10, 2005
 * 
 * $Id: Pack.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : Pack.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package izpack.frontend.model;

import izpack.frontend.model.files.ElementModel;
import izpack.frontend.model.files.PackElement;
import izpack.frontend.model.files.PackFileModel;
import izpack.frontend.model.files.Parsable;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Document;

/**
 * @author Andy Gombos
 */
public class PackModel implements ElementModel
{
    public PackModel()
    {
        model = new DefaultTableModel(15, 1);
    }
    
    private String name = "", desc = "", id = "", os = "";
    private boolean required = true, preselected = true, loose = false;
    
    /**
     * @return Returns the desc.
     */
    public String getDesc()
    {
        return desc;
    }
    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }
    /**
     * @return Returns the loose.
     */
    public boolean isLoose()
    {
        return loose;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @return Returns the os.
     */
    public String getOS()
    {
        return os;
    }
    /**
     * @return Returns the preselected.
     */
    public boolean isPreselected()
    {
        return preselected;
    }
    /**
     * @return Returns the required.
     */
    public boolean isRequired()
    {
        return required;
    }
    /**
     * @param desc The desc to set.
     */
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * @param loose The loose to set.
     */
    public void setLoose(boolean loose)
    {
        this.loose = loose;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param os The os to set.
     */
    public void setOS(String os)
    {
        this.os = os;
    }
    /**
     * @param preselected The preselected to set.
     */
    public void setPreselected(boolean preselected)
    {
        this.preselected = preselected;
    }
    /**
     * @param required The required to set.
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }
    
    public Document writePack()
    {
        return null;
    }
    
    public void addFiles(PackElement elem)
    {
        elements.add(elem);
    }
    
    public void removeFile(PackElement elem)
    {
        elements.remove(elem);
    }
    
    public DefaultTableModel getFilesModel()
    {
        return model;
    }    
    
    public void printFiles()
    {
        System.out.println("Pack: " + name + " " + model.getRowCount());        
        
        for(int i = 0; i < model.getRowCount(); i++)
        {
            Object data = model.getValueAt(i, 0);
            
            if (data != null)
            {
                if (data instanceof PackFileModel)                
                    System.out.println("\t" + ((PackFileModel) data ).source );
                if (data instanceof Parsable)                
                    System.out.println("\t" + ((Parsable) data ).targetfile );                
            }
        }
    }
    
    DefaultTableModel model;
    ArrayList elements = new ArrayList();

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.ElementModel#writeXML()
     */
    public Document writeXML()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
