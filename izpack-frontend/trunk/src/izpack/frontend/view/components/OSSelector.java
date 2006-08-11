/*
 * Created on Apr 7, 2005
 * 
 * $Id: OSComboBox.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : OSComboBox.java 
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

package izpack.frontend.view.components;

import izpack.frontend.model.OS;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyAdapter;

/**
 * @author Andy Gombos
 */
public class OSSelector extends JPanel
{
    public OSSelector()
    {       
        windows = new JCheckBox("Windows");
        unix = new JCheckBox("Unix");
        mac = new JCheckBox("Mac");
        
        model = new PresentationModel(osModel);
        
        Bindings.bind(windows, model.getModel("windows"));
        Bindings.bind(unix, model.getModel("unix"));
        Bindings.bind(mac, model.getModel("mac"));
        
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);        
        add(windows);
        add(unix);
        add(mac);
    }
    
    public OS getOsModel()
    {
        return osModel;
    }
    
    public void setOsModel(OS osModel)
    {        
        this.osModel = osModel;
        
        model = new PresentationModel(osModel);
        
        Bindings.bind(windows, model.getModel("windows"));
        Bindings.bind(unix, model.getModel("unix"));
        Bindings.bind(mac, model.getModel("mac"));
    }
    
    public void setNoneSelected()
    {
        osModel.setMac(false);
        osModel.setWindows(false);
        osModel.setUnix(false);
    }
    
    private JCheckBox windows, unix, mac;
    
    //TODO the pack stuff should bind to the model, not create a new instance
    private OS osModel = new OS();
    private PresentationModel model;            
}
