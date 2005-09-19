/*
 * Created on Aug 25, 2005
 * 
 * $Id: ConfigurationStageModel.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ConfigurationStageModel.java 
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
package izpack.frontend.model.stages;

import izpack.frontend.controller.PanelInfoManager;
import izpack.frontend.model.PanelInfo;
import izpack.frontend.view.stages.configure.panels.ConfigurePanel;
import izpack.frontend.view.stages.configure.panels.NoEditorCreated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jgoodies.binding.beans.Model;

public class ConfigurationStageModel extends Model implements StageDataModel, ListDataListener
{

    public ConfigurationStageModel(PanelSelectionModel psm)
    {
        super();
        
        //panels = new ArrayListModel(psm);
        panels = psm;
        editors = new ArrayList<ConfigurePanel>();
        
        panels.addListDataListener(this);        
    }

    //TODO fix this so the elements are inserted properly
    public Element[] writeToXML(Document doc)
    {   
        if (editors == null)
            return new Element[]{};
        
        ArrayList<Element> editorXML = new ArrayList<Element>(editors.size());
        
        //TODO Create the interesting XML data
        for (ConfigurePanel editor : editors)
        {
            Element xml = editor.createXML(doc);
            
            if (xml != null)
            {   
                editorXML.add(xml);
            }
        }        
        
        return editorXML.toArray(new Element[0]);
    }

    public void initFromXML(Document doc)
    {        
        HashMap<String, PanelInfo> panels = PanelInfoManager.getAvailablePanelMap();        
        Set keys = panels.keySet();
        
        XPath xpath = XPathFactory.newInstance().newXPath();

        for (Object key : keys)
        {
            try
            {                
                PanelInfo panelInfo = panels.get(key);
                
                Boolean panelPresent = (Boolean) xpath.evaluate("//panels/panel[@classname=\'" + panelInfo.getClassname() + "\']",
                                doc, XPathConstants.BOOLEAN);
                
                if (panelPresent.booleanValue())
                {                       
                    ConfigurePanel editor = (ConfigurePanel) Class.forName(panelInfo.getEditorClassname()).newInstance();
                
                    editor.initFromXML(doc);
                    
                    PanelModel pModel = new PanelModel();
                    pModel.configData = panelInfo;
                    pModel.valid = false;
                    
                    ( (PanelSelectionModel) this.panels ).addElement(pModel);
                    this.editors.add(editor);
                }                
            }
            catch (InstantiationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                this.editors.add(new NoEditorCreated());
            }
            catch (XPathExpressionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void setEditors(ArrayList<ConfigurePanel> editors)
    {
        this.editors = editors;
    }

    private ListModel panels;
    private ArrayList<ConfigurePanel> editors;
    int currentlyActivePanel;
    
    public int getCurrentlyActivePanel()
    {
        return currentlyActivePanel;
    }

    public ListModel getPanels()
    {
        return panels;
    }
 
    public void setCurrentlyActivePanel(int currentlyActivePanel)
    {
        this.currentlyActivePanel = currentlyActivePanel;
    }

    public void setPanels(ListModel panels)
    {
        this.panels = panels;
    }
    
    public void setPanels(ArrayList<PanelModel> panels)
    {
        //this.panels = new ArrayListModel(panels);
    }

    public ArrayList<ConfigurePanel> getEditors()
    {
        return editors;
    }

    public void intervalAdded(ListDataEvent e)
    {
        System.out.println("Panels added");        
    }

    public void intervalRemoved(ListDataEvent e)
    {
        System.out.println("Panels removed");
        
    }

    public void contentsChanged(ListDataEvent e)
    {
        System.out.println("Panels moved");        
    }    
}
