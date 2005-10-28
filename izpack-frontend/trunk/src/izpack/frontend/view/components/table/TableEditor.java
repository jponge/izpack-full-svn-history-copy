/*
 * Created on Apr 11, 2005
 * 
 * $Id: TableEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : TableEditor.java 
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

package izpack.frontend.view.components.table;

import izpack.frontend.model.LangResources;
import izpack.frontend.model.files.ElementModel;
import izpack.frontend.view.IzPackFrame;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * @author Andy Gombos
 */
public abstract class TableEditor extends JDialog implements ActionListener
{     
    public TableEditor(Frame parent)
    {
        super(parent);
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
    }
    
    public abstract boolean handles(Class type);
    
    public abstract void configure(ElementModel model);
    
    public abstract void configureClean();

    public abstract ElementModel getModel();

    public boolean wasOKPressed()
    {
        return !cancelled;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        JButton src = (JButton) e.getSource();
        if (src.equals(ok))
        {
            setVisible(false);
            cancelled = false;
        }
        else if (src.equals(cancel))
        {
            setVisible(false);
            cancelled = true;
        }
    }
    
    protected boolean cancelled = false;
    protected JButton ok, cancel;
    protected static LangResources lr = IzPackFrame.getInstance().langResources();    
}