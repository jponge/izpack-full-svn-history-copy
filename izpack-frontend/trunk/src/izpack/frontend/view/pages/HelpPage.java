/*
 * $Id: HelpPage.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : HelpPage.java 
 * Description : Quick implementation of the help page.
 * Author's email : dani@gueggs.net 
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
package izpack.frontend.view.pages;

import izpack.frontend.controller.DisplayPageAction;
import izpack.frontend.view.GUIConstants;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Test HelpPage.
 * 
 * @author Daniel Guggi
 */
public class HelpPage extends IzPackPage {

	/**
	 * Quick implementation of the help page, to test framework.
	 */
	public void initComponents() {
		setLayout(new BorderLayout());
		JLabel label = new JLabel(this.langResources().getText("UI.HelpPage.INFO.Text"));
		JButton welcomeButton = (JButton)this.addElement(GUIConstants.BUTTON_HELP, new JButton());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		welcomeButton.setText(langResources().getText("UI.HelpPage.ELEMENT.WelcomeButton"));
		welcomeButton.addActionListener(new DisplayPageAction(GUIConstants.PAGE_WELCOME));
		panel.add(welcomeButton);
		
		// add components to the content pane
		this.add(label, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
	}

}
