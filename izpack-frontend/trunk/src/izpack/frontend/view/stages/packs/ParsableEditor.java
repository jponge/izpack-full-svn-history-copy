/*
 * Created on Apr 18, 2005
 * 
 * $Id: ParsableEditor.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : ParsableEditor.java 
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
package izpack.frontend.view.stages.packs;

import izpack.frontend.model.ElementModel;
import izpack.frontend.model.LangResources;
import izpack.frontend.model.files.Parsable;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.components.FormatComboBox;
import izpack.frontend.view.components.OSComboBox;
import izpack.frontend.view.components.table.TableEditor;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Andy Gombos
 */
public class ParsableEditor extends TableEditor
{
    public ParsableEditor(Frame parent)
    {
        super(parent);
        
        FormLayout layout = new FormLayout("right:max(40dlu;p), 3dlu, 80dlu, 3dlu, max(25dlu;p)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new JPanel());
        builder.setDefaultDialogBorder();
        
        targetFile = new JTextField();
        format = new FormatComboBox();
        encoding = new JTextField();
        os = new OSComboBox();
        
        ok = new JButton(lr.getText("UI.Buttons.OK"));
        ok.addActionListener(this);
        
        cancel = new JButton(lr.getText("UI.Buttons.Cancel"));
        cancel.addActionListener(this);
        
        browse = new JButton(lr.getText("UI.Buttons.Browse"));
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();                
                int returnVal = chooser.showOpenDialog(browse.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   targetFile.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }            
        });        
               
        builder.append(lr.getText("UI.ParsableEditor.TargetFile"), targetFile, browse);
        builder.nextLine();        
        builder.append(lr.getText("UI.ParsableEditor.Format"), format);
        builder.nextLine();
        builder.append(lr.getText("UI.ParsableEditor.Encoding"), encoding);
        builder.nextLine();
        builder.append(lr.getText("UI.ParsableEditor.OS"), os);
        builder.nextLine();        
        
        builder.appendUnrelatedComponentsGapRow();
        builder.nextLine();
        builder.append(ButtonBarFactory.buildOKCancelBar(ok, cancel), 3);
        
        getContentPane().add(builder.getPanel());
        pack();
        getRootPane().setDefaultButton(ok);
        setModal(true);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configure(izpack.frontend.model.ElementModel)
     */
    public void configure(ElementModel model)
    {
        Parsable pm = (Parsable) model;
        
        targetFile.setText(pm.targetfile);
        encoding.setText(pm.encoding);
        
        format.setFormat(pm.type);
        os.setOS(pm.os);
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#configureClean()
     */
    public void configureClean()
    {
        targetFile.setText("");
        encoding.setText("");
        
        format.setSelectedIndex(-1);
        os.setSelectedIndex(-1);        
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#getModel()
     */
    public ElementModel getModel()
    {
        Parsable pm = new Parsable();
        
        pm.targetfile = targetFile.getText();
        pm.encoding = encoding.getText();
        
        pm.type = format.getFormat();
        pm.os = os.getOS();
        
        return pm;
    }
    
    JButton browse;
    JTextField targetFile;
    FormatComboBox format;
    JTextField encoding;
    OSComboBox os;
    /* (non-Javadoc)
     * @see izpack.frontend.view.components.table.TableEditor#handles(java.lang.Class)
     */
    public boolean handles(Class type)
    {
        return type.equals(Parsable.class);
    }   
}
