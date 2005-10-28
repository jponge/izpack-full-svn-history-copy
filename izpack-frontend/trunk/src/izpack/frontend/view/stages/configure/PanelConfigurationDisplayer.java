/*
 * Created on Aug 26, 2005
 * 
 * $Id: PanelConfigurationDisplayer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PanelConfigurationDisplayer.java 
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

package izpack.frontend.view.stages.configure;

import izpack.frontend.model.stages.PanelModel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.UI;
import exceptions.UnhandleableException;

public class PanelConfigurationDisplayer extends JPanel
{    
    public PanelConfigurationDisplayer()
    {
        label = new JLabel("Text goes here");
        add(label);   
        
        setLayout(new BorderLayout());
        add(label, BorderLayout.SOUTH);
        
        setSize(400, 400);
    }
    
    public void initializeForDisplay()
    {
        
    }
    
    public Object getPanelOnDisplay()
    {
        return panelOnDisplay;
    }

    public void setPanelOnDisplay(Object panelOnDisplay)
    {   
        if (panelOnDisplay == null)
            return;
        
        if (panelOnDisplay instanceof PanelModel)
            this.panelOnDisplay = (PanelModel) panelOnDisplay;        
        
        try
        {
            //TODO very retarded implementation
            Class c = Class.forName(this.panelOnDisplay.configData.getEditorClassname());
            JPanel editor = (JPanel) c.newInstance();
            
            removeAll();
            add(editor, BorderLayout.CENTER);            
            
            label.setText(c.getSimpleName());
            add(label, BorderLayout.SOUTH);
            
            repaint();
        }
        catch (ClassNotFoundException e)
        {
            label.setText("No editing necessary");
            add(label);
        }       
        catch (InstantiationException e)
        {
            UI.showError("Unable to initialize editor " + this.panelOnDisplay.configData.getEditorClassname() + 
                            "\n" + e.getLocalizedMessage(), "Failure creating editor");            
        }
        catch (IllegalAccessException e)
        {
            //Shouldn't ever happen
            throw new UnhandleableException(e);            
        }
    } 
    
    private PanelModel panelOnDisplay;
    private JLabel label;
}
