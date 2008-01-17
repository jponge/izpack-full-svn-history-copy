/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 * 
 * http://izpack.org/
 * http://developer.berlios.de/projects/izpack/
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

import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.installer.ResourceManager;

/**
 * The HTML info panel.
 * 
 * @author Julien Ponge
 */
public class HTMLInfoPanel extends IzPanel implements HyperlinkListener
{

    private static final long serialVersionUID = 3257008769514025270L;

    /** The text area. */
    private JEditorPane textArea;

    /**
     * The constructor.
     * 
     * @param parent The parent.
     * @param idata The installation data.
     */
    public HTMLInfoPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata,new IzPanelLayout());
        // We add the components

        add(LabelFactory.create(parent.langpack.getString("InfoPanel.info"), parent.icons
                .getImageIcon("edit"),  LEADING), NEXT_LINE);
        try
        {
            textArea = new JEditorPane();
            textArea.setContentType("text/html; charset=utf-8");
            textArea.setEditable(false);
            textArea.addHyperlinkListener(this);
            JScrollPane scroller = new JScrollPane(textArea);
            textArea.setPage(loadInfo());
            add(scroller, NEXT_LINE);
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
        getLayoutHelper().completeLayout();
    }

    /**
     * Loads the info.
     * 
     * @return The info URL.
     */
    private URL loadInfo()
    {
        String resNamePrifix = "HTMLInfoPanel.info";
        try
        {
            return ResourceManager.getInstance().getURL(resNamePrifix);
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
                if(urls.contains("HTMLInfoPanel.info#"))
                   textArea.setPage(e.getURL());
                else
                {
                   if(com.izforge.izpack.util.OsVersion.IS_OSX)
                   {
                       Runtime.getRuntime().exec("open " + urls);
                   } else if(com.izforge.izpack.util.OsVersion.IS_UNIX)
                   {
                      String[] launchers = {"htmlview QqzURL", "xdg-open QqzURL", "gnome-open QqzURL", "kfmclient openURL QqzURL", "call-browser QqzURL", "firefox QqzURL", "opera QqzURL", "konqueror QqzURL", "epiphany QqzURL", "mozilla QqzURL", "netscape QqzURL"};
                      //String launchers = "/bin/sh -c \"htmlview QqzURL || xdg-open QqzURL || gnome-open QqzURL || kfmclient openURL QqzURL || call-browser QqzURL || firefox QqzURL || opera QqzURL || konqueror QqzURL || epiphany QqzURL || mozilla QqzURL || netscape QqzURL\"";
                      for(int q=0; q<launchers.length; q++)
                      {
                         
                         try
                         {
                            Runtime.getRuntime().exec(launchers[q].replaceAll("QqzURL", urls));
                            System.out.println("OK");
                            break;
                         }
                         catch(Exception ignore)
                         {
                            System.out.println(launchers[q]+" NOT OK");
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
}
