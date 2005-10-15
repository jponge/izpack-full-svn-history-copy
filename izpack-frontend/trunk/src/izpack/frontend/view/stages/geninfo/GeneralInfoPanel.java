/*
 * Created on Jun 28, 2004
 * 
 * $Id: GeneralInfoPage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : GeneralInfoPage.java 
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
package izpack.frontend.view.stages.geninfo;

import izpack.frontend.controller.AuthorManager;
import izpack.frontend.model.Author;
import izpack.frontend.model.stages.GeneralInformationModel;
import izpack.frontend.view.stages.IzPackStage;
import izpack.frontend.view.stages.configure.panels.IzPackPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import org.w3c.dom.Document;

import utils.UI;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */
public class GeneralInfoPanel extends IzPackPanel implements ActionListener
{   
    /**
     * @param information
     */
    public GeneralInfoPanel(IzPackStage stage)
    {
        super(stage);
    }

    public void initComponents()
    {
        FormLayout layout = new FormLayout("pref, 3dlu, max(50dlu;pref), max(50dlu;pref), max(50dlu;pref), 3dlu, pref", 
                "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 40dlu");        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        
        PresentationModel adapter = stage.getValidatingModel();
                
        CellConstraints cc = new CellConstraints();
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.APPNAME.Text")));
        builder.nextRow(2);
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.VERSION.Text")));
        builder.nextRow(2);
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.HOMEPAGE.Text")));
        builder.nextRow(2);
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.AUTHORS.Text")));
        
        builder.setColumn(3);
        builder.setRow(1);
             
        appName = BasicComponentFactory.createTextField(adapter.getModel("appName"), false);
        version = BasicComponentFactory.createTextField(adapter.getModel("version"), false);
        homepage = BasicComponentFactory.createTextField(adapter.getModel("homepage"), false);
        
        builder.add(appName, 	cc.xyw(3, 1, 2));        
        builder.add(version, 	cc.xyw(3, 3, 2));        
        builder.add(homepage,   cc.xyw(3, 5, 2));        
        
        
        //Create the list from an ArrayList model
        authorListModel = ( (GeneralInformationModel) stage.getDataModel() ).getAuthors();
        
        ArrayList authorListData = AuthorManager.loadAuthors();
        if (authorListData != null)
        {
            for (Iterator iter = authorListData.iterator(); iter.hasNext(); )
            {
               authorListModel.add(iter.next());
            }        
        }       
        
        authorModel = new SelectionInList(authorListModel);
                
        authorList = BasicComponentFactory.createList(authorModel);
        //authorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        builder.add(authorList, cc.xywh(3, 7, 3, 6));
        
        builder.setColumn(7);
        builder.setRow(7);
        
        actions = new JButton[] {
                new JButton(new ImageIcon("res/imgs/author_new.png")),
                new JButton(new ImageIcon("res/imgs/author_edit.png")),
                new JButton(new ImageIcon("res/imgs/author_delete.png"))};
        
        for (int i = 0; i < actions.length; i++)
        {
            actions[i].addActionListener(this);
            builder.add(actions[i]);
            builder.nextRow(2);
        }
        
        configureValidation();
        ValidationComponentUtils.updateComponentTreeValidationBackground(this, ValidationResult.EMPTY);                
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            JButton source = (JButton) e.getSource();
            
            //New
            if (source.equals(actions[0]))
            {                
                Author author = displayAuthorDialog(null);
                if (author.getName() != null)                
                    authorListModel.add(author);
                
                AuthorManager.updateAuthors(authorListModel);
            }
            //Edit
            else if (source.equals(actions[1]))
            {
                Author author = displayAuthorDialog((Author) authorModel.getSelection());
                if (author.getName() != null)
                    authorListModel.set(authorList.getSelectedIndex(), author);
                
                AuthorManager.updateAuthors(authorListModel);
            }
            //Delete
            else if (source.equals(actions[2]))
            {
                //Make sure we have a selection
                if (authorModel.hasSelection())
                {
                    authorListModel.remove(authorModel.getSelectionIndex());
                    
                    AuthorManager.updateAuthors(authorListModel);
                }
            }
        }        
    }
    
    public Author displayAuthorDialog(Author oldAuthor)
    {
        System.out.println(UI.getApplicationFrame());
        final JDialog dialog = new JDialog(UI.getApplicationFrame(), langResources().getText("UI.GeneralInfoPage.Dialog.INFO.Text"), true);
        final Author returnValue = new Author(null, null);
        
        FormLayout layout = new FormLayout("pref, 3dlu, 100dlu",
                "pref, 3dlu, pref, 5dlu, pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.Dialog.ELEMENT.NameLabel")));
        builder.nextRow(2);
        builder.add(new JLabel(langResources().getText("UI.GeneralInfoPage.Dialog.ELEMENT.EmailLabel")));
        builder.setColumn(3);
        builder.setRow(1);
        
        final JTextField name = new JTextField();
        
        final JTextField email = new JTextField();
        builder.add(name);
        builder.nextRow(2);
        builder.add(email);        
        
        //TODO This is a rather messy way of getting the behavior I want...
        JButton OK = new JButton(langResources().getText("UI.GeneralInfoPage.Dialog.ELEMENT.OKButton"));
        OK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dialog.dispose();                
                returnValue.setName(name.getText());
                returnValue.setEmail(email.getText());
            }
        });        
        JButton cancel = new JButton(langResources().getText("UI.GeneralInfoPage.Dialog.ELEMENT.CancelButton"));
        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dialog.dispose();
            }
        });
        
        dialog.getRootPane().setDefaultButton(OK);
        builder.add(ButtonBarFactory.buildOKCancelBar(OK, cancel), new CellConstraints().xyw(1, 5, 3));
                
        if (oldAuthor != null)
        {
            name.setText(oldAuthor.getName());
            email.setText(oldAuthor.getEmail());
        }
                
        dialog.getContentPane().add(builder.getPanel());
        dialog.pack();
        dialog.setVisible(true);
        
        return returnValue;
    }

    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#initFromXML(org.w3c.dom.Document)
     */
    public void initFromXML(Document xmlFile)
    {
        // TODO Auto-generated method stub
        
    }
    
    private void configureValidation()
    {
        ValidationComponentUtils.setMandatory(appName, true);
        ValidationComponentUtils.setMandatory(version, true);
        
        ValidationComponentUtils.setMessageKey(appName, "Info.application name");
        ValidationComponentUtils.setMessageKey(version, "Info.application version");
        ValidationComponentUtils.setMessageKey(homepage, "Info.homepage");
    }
    
    ArrayList authorListModel;
    
    SelectionInList authorModel;    
    
    JList authorList;
    JButton actions[];
    JTextField appName, version, homepage;
    Model model;
}
