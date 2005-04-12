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

import izpack.frontend.model.Author;
import izpack.frontend.model.AuthorManager;
import izpack.frontend.view.IzPackFrame;
import izpack.frontend.view.stages.panels.ConfigurePanel;
import izpack.frontend.view.stages.panels.IzPackPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.XML;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * @author Andy Gombos
 */
public class GeneralInfoPanel extends IzPackPanel implements ActionListener, FocusListener, ConfigurePanel
{
    public void initComponents()
    {
        FormLayout layout = new FormLayout("pref, 3dlu, max(50dlu;pref), max(50dlu;pref), max(50dlu;pref), 3dlu, pref", 
                "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 40dlu");        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);

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
             
        appName = new JTextField();
        version = new JTextField();
        homepage = new JTextField();
        
        builder.add(appName, 	cc.xyw(3, 1, 2));        
        builder.add(version, 	cc.xyw(3, 3, 2));        
        builder.add(homepage,   cc.xyw(3, 5, 2));        
                
        authorModel = new DefaultListModel();
        for (Iterator iter = AuthorManager.loadAuthors().iterator(); iter.hasNext(); )
        {
           authorModel.addElement(iter.next());            	
        }
        
        authorList = new JList(authorModel);
        authorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                    authorModel.addElement(author);
            }
            //Edit
            else if (source.equals(actions[1]))
            {
                Author author = displayAuthorDialog((Author) authorList.getSelectedValue());
                if (author.getName() != null)
                    authorModel.set(authorList.getSelectedIndex(), author);
            }
            //Delete
            else if (source.equals(actions[2]))
            {
                //Make sure we have a selection
                if (authorList.getSelectedIndex() != -1)
                {
                    authorModel.remove(authorList.getSelectedIndex());
                }
            }
        }
        
        //Validate on enter pressed
        if (e.getSource() instanceof JTextComponent)
        {
            validateTextFields();
        }
    }
    
    public Author displayAuthorDialog(Author oldAuthor)
    {
        final JDialog dialog = new JDialog(IzPackFrame.getInstance(), langResources().getText("UI.GeneralInfoPage.Dialog.INFO.Text"), true);
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
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#createXML()
     */
    public Element createXML()
    {
        Element info = XML.createRootElement("info");
        Document doc = info.getOwnerDocument();
        
        Element appname = XML.createElement("appname", doc);
        Element appversion = XML.createElement("appversion", doc);
        Element url = XML.createElement("url", doc);
        Element authors = XML.createElement("authors", doc);
        
        appname.setTextContent(appName.getText());
        appversion.setTextContent(version.getText());
        url.setTextContent(homepage.getText());
        
        for (int i = 0; i < authorModel.getSize(); i++)
        {
            Author auth = (Author) authorModel.getElementAt(i);
            Element author = XML.createElement("author", doc);
            author.setAttribute("name", auth.getName());
            author.setAttribute("email", auth.getEmail());
            
            authors.appendChild(author);
        }
        
        info.appendChild(appname);
        info.appendChild(appversion);
        info.appendChild(url);
        info.appendChild(authors);
        
        return info;
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
        
        appName.addFocusListener(this);
        version.addFocusListener(this);
        homepage.addFocusListener(this);
        
        appName.addActionListener(this);
        version.addActionListener(this);
        homepage.addActionListener(this);
    }
    
    /* (non-Javadoc)
     * @see izpack.frontend.view.stages.panels.ConfigurePanel#validatePanel()
     */
    public ValidationResult validatePanel()
    {        
        return validateTextFields();
    }
    
    private ValidationResult validateTextFields()
    {
        ValidationResult vr = new ValidationResult();
        
        if (ValidationComponentUtils.isMandatoryAndBlank(appName))
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "is mandatory",
                            appName,
                            "Info",
                            "application name"                            
                            ));
        }
        if (ValidationComponentUtils.isMandatoryAndBlank(version))
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "is mandatory",
                            version,
                            "Info",
                            "application version"                            
                            ));
        }        
        
        //A simple way to check if a homepage URL is valid
        try
        {            
            if (homepage.getText().length() != 0)
                new URL(homepage.getText());
        }
        catch (MalformedURLException murle)
        {
            vr.add(new PropertyValidationMessage(
                            Severity.ERROR,
                            "is not a valid URL: " + murle.getLocalizedMessage(),
                            appName,
                            "Info",
                            "homepage"
                            ));
        }
        
        ValidationComponentUtils.updateComponentTreeValidationBackground(this, vr);
        return vr;
    }   
    
    public void focusGained(FocusEvent e) {}
    
    /* Validate components upon losing focus
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e)
    {      
        if (e.getSource() instanceof JTextField)
        {            
            validateTextFields();
        }        
    }
    
    DefaultListModel authorModel;
    JList authorList;
    JButton actions[];
    JTextField appName, version, homepage;

}
