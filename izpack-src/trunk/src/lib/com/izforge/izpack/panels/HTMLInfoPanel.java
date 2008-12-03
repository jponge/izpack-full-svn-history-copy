/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 * 
 * http://izpack.org/
 * http://izpack.codehaus.org/
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

package com.izforge.izpack.panels;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.*;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.io.IOException;
import java.net.URL;

/**
 * The HTML info panel.
 *
 * @author Julien Ponge
 */
public class HTMLInfoPanel extends IzPanel implements HyperlinkListener
{

    private static final long serialVersionUID = 3257008769514025270L;

    /**
     * The text area.
     */
    private JEditorPane textArea;

    /**
     * The constructor.
     *
     * @param parent The parent.
     * @param idata  The installation data.
     */
    public HTMLInfoPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata, new IzPanelLayout());
        // We add the components

        add(LabelFactory.create(parent.langpack.getString("InfoPanel.info"), parent.icons
                .getImageIcon("edit"), LEADING), NEXT_LINE);
        try
        {
            textArea = new JEditorPane();
            textArea.setContentType("text/html; charset=utf-8");
            textArea.setEditable(false);
            textArea.addHyperlinkListener(this);
            JScrollPane scroller = new JScrollPane(textArea);
            textArea.setPage(loadHTMLInfoContent());
            add(scroller, NEXT_LINE);
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
        getLayoutHelper().completeLayout();
    }

    /*
    * loads the content of the info resource as text so that it can be parsed afterwards
    */
    private URL loadHTMLInfoContent()
    {
        String resPrefix = "HTMLInfoPanel.info";
        if (getMetadata() != null && getMetadata().getPanelid() != null)
        {
            try
            {
                String panelSpecificResName = "HTMLInfoPanel." + this.getMetadata().getPanelid();
                String panelspecificResContent = ResourceManager.getInstance().getTextResource(panelSpecificResName);
                if (panelspecificResContent != null)
                {
                    return ResourceManager.getInstance().getURL(panelspecificResContent);
                }
            }
            catch (Exception e)
            {
                // Those ones can be skipped
            }
        }

        try
        {
            return ResourceManager.getInstance().getURL(resPrefix);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Indicates wether the panel has been validated or not.
     *
     * @return Always true.
     */
    public boolean isValidated()
    {
        return true;
    }

    /**
     * Hyperlink events handler.
     *
     * @param e The event.
     */
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        try
        {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            {
                String urls = e.getURL().toExternalForm();
                // if the link points to a chapter in the same page
                // don't open a browser
                if (urls.contains("HTMLInfoPanel.info#"))
                {
                    textArea.setPage(e.getURL());
                }
                else
                {
                    if (com.izforge.izpack.util.OsVersion.IS_OSX)
                    {
                        Runtime.getRuntime().exec("open " + urls);
                    }
                    else if (com.izforge.izpack.util.OsVersion.IS_UNIX)
                    {
                        String[] launchers = {"htmlview QqzURL", "xdg-open QqzURL", "gnome-open QqzURL", "kfmclient openURL QqzURL", "call-browser QqzURL", "firefox QqzURL", "opera QqzURL", "konqueror QqzURL", "epiphany QqzURL", "mozilla QqzURL", "netscape QqzURL"};
                        //String launchers = "/bin/sh -c \"htmlview QqzURL || xdg-open QqzURL || gnome-open QqzURL || kfmclient openURL QqzURL || call-browser QqzURL || firefox QqzURL || opera QqzURL || konqueror QqzURL || epiphany QqzURL || mozilla QqzURL || netscape QqzURL\"";
                        for (String launcher : launchers)
                        {

                            try
                            {
                                Runtime.getRuntime().exec(launcher.replaceAll("QqzURL", urls));
                                System.out.println("OK");
                                break;
                            }
                            catch (Exception ignore)
                            {
                                System.out.println(launcher + " NOT OK");
                            }
                        }
                    }
                    else // windows
                    {
                        Runtime.getRuntime().exec("cmd /C start " + urls);
                    }
                }
            }
        }
        catch (Exception err)
        {
            //TODO: Handle exception.
        }
    }

    public void panelActivate()
    {
        try
        {
            textArea.setPage(loadHTMLInfoContent());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
