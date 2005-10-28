package izpack.frontend.view.renderers;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
/*
 * Created on Nov 18, 2004
 * 
 * $Id: LabelRenderer.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LabelRenderer.java 
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


/**
 * @author Andy Gombos
 */
public class LabelRenderer extends DefaultListCellRenderer
{

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JComponent c = (JComponent) value;
        if (isSelected)
            c.setBorder(selected);
        else
            c.setBorder(unselected);
        
        return (Component) c;
    }

    Border unselected = new SoftBevelBorder(SoftBevelBorder.RAISED);
    Border selected = new SoftBevelBorder(SoftBevelBorder.LOWERED);
}
