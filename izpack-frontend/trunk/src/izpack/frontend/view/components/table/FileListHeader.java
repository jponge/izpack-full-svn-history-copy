/*
 * Created on Apr 11, 2005
 * 
 * $Id: PackListHeader.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : PackListHeader.java 
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

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class FileListHeader extends JPanel
{
    public FileListHeader()
    {	    
	    FormLayout layout = new FormLayout("10dlu, 40dlu, 4dlu, 80dlu, 15dlu, max(80dlu;p), 10dlu", "15dlu");
	    DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
	    
	    CellConstraints cc = new CellConstraints();
	    
	    //TODO Externalize these
	    builder.add(new JLabel("Type"), cc.xy(2, 1));
	    builder.add(new JLabel("Target"), cc.xy(4, 1));
	    builder.add(new JLabel("Source/Format/Class"), cc.xy(6, 1));
    }
}
