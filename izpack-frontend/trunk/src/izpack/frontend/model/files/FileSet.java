/*
 * Created on Apr 12, 2005
 * 
 * $Id: FileSet.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : FileSet.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
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

package izpack.frontend.model.files;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
    public Element writeXML(Document doc)
    {   
        Element fset = XML.createElement("fileset", doc);
        fset.setAttribute("dir", source);
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
        
        return fset;
    }
    
    public void initFromXML(Node elementNode)
    {
        NamedNodeMap attributes = elementNode.getAttributes();
        
        source = attributes.getNamedItem("dir").getNodeValue();
        
        target = attributes.getNamedItem("targetdir").getNodeValue();
        
        if (attributes.getNamedItem("os") != null)
            os = attributes.getNamedItem("os").getNodeValue();       
        
        if (attributes.getNamedItem("override") != null)
            override = attributes.getNamedItem("override").getNodeValue();
        
        if (attributes.getNamedItem("casesensitive") != null)
            caseSensitive = yesNoBoolean(attributes.getNamedItem("casesensitive").getNodeValue());
        
        if (attributes.getNamedItem("defaultexcludes") != null)
            defaultExcludes = yesNoBoolean(attributes.getNamedItem("defaultexcludes").getNodeValue());
    }

    
    private String yesNoBoolean(boolean b)
    {
        return b ? "yes" : "no";
    }
    
    private boolean yesNoBoolean(String b)
    {
        if (b.equalsIgnoreCase("yes"))
            return true;
        else
            return false;
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
        
        public abstract void initFromXML(Node node);
        
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

        @Override
        public void initFromXML(Node node)
        {
            // TODO Auto-generated method stub
            
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

        @Override
        public void initFromXML(Node node)
        {
            // TODO Auto-generated method stub
            
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
