/*
 * Created on Apr 12, 2005
 * 
 * $Id: FileSet.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : FileSet.java 
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
package izpack.frontend.model.files;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

/**
 * @author Andy Gombos
 */
public class FileSet extends PackFileModel implements ListModel
{
    public boolean caseSensitive = true, defaultExcludes = true;
    private ArrayList files = new ArrayList();

    /* (non-Javadoc)
     * @see izpack.frontend.model.files.PackElement#writeXML()
     */
    public Document writeXML()
    {
        Document doc = XML.getNewDocument();        
        
        Element fset = XML.createElement("file", doc);
        fset.setAttribute("src", source);
        fset.setAttribute("targetdir", target);
        
        if (!os.equals(""))
            fset.setAttribute("os", os);
        
        if (!override.equals(""))
            fset.setAttribute("override", override);
        
        fset.setAttribute("casesensitive", yesNoBoolean(caseSensitive));
        fset.setAttribute("defaultexcludes", yesNoBoolean(defaultExcludes));
        
        for (Iterator iter = files.iterator(); iter.hasNext();)
        {
            Set element = (Set) iter.next();            
            
            Element set = element.writeXML(doc);
            fset.appendChild(set);
        }        
        
        doc.appendChild(fset);
        return doc;
    }
    
    private String yesNoBoolean(boolean b)
    {
        return b ? "yes" : "no";
    }
    
    public ArrayList getSetList()
    {
        return files;
    }
    
    public void setSetList(ArrayList setList)
    {
        for (Iterator iter = setList.iterator(); iter.hasNext();)
        {
            Set element = (Set) iter.next();
            addSet(element);
        }
    }
    
    public void addSet(Set set)
    {
        int index = files.size();
        
        System.out.println(index);
        
        files.add(index, set);
        fireElementAddedEvent(index);
    }
    
    public void removeSet(Set set)
    {
        if (set == null)
            return;
        
        int index = files.indexOf(set);
        files.remove(index);
        fireElementRemovedEvent(index);
    }
    
    public abstract static class Set
    {
        private Set(String set)
        {
            this.set = set;
        }
        
        public abstract String toString();
        
        public abstract Element writeXML(Document d);
        
        protected String set;
    }
    
    public static class Include extends Set
    {   
        public Include(String set)
        {
            super(set);
        }
        
        public String toString()
        {
            return "Include:  " + set;
        }
        
        public Element writeXML(Document d)
        {
            Element include = XML.createElement("include", d);
            include.setAttribute("name", set);
            
            return include;
        }
    }
    
    public static class Exclude extends Set
    {
        public Exclude(String set)
        {
            super(set);
        }
        
        //So we don't have to write our own renderer
        public String toString()
        {
            return "Exclude:  " + set;
        }
        
        public Element writeXML(Document d)
        {
            Element exclude = XML.createElement("exclude", d);
            exclude.setAttribute("name", set);
            
            return exclude;
        }
    }

    /*    
     * ListModel stuff
     */
    
    
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {       
        return files.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index)
    {        
        return files.get(index);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
     */
    public void addListDataListener(ListDataListener l)
    {
        dataListeners.add(l);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
     */
    public void removeListDataListener(ListDataListener l)
    {     
        dataListeners.remove(l);
    }
    
    private void fireElementAddedEvent(int index)
    {
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
        
        for (Iterator iter = dataListeners.iterator(); iter.hasNext();)
        {
            ListDataListener l = (ListDataListener) iter.next();
            l.intervalAdded(lde);
        }
    }
    
    private void fireElementRemovedEvent(int index)
    {
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
        
        for (Iterator iter = dataListeners.iterator(); iter.hasNext();)
        {
            ListDataListener l = (ListDataListener) iter.next();
            l.intervalRemoved(lde);
        }
    }
    
    private ArrayList dataListeners = new ArrayList();
}
