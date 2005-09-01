/*
 * Created on Apr 13, 2005
 * 
 * $Id: StageChangeEvent.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2001-2003 IzPack Development Group
 * 
 * File : StageChangeEvent.java 
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
package izpack.frontend.controller;

/**
 * @author Andy Gombos
 */
public class StageChangeEvent
{
    public StageChangeEvent(Class stage)
    {
        this.stage = stage;
    }
    
    public Class getStageClass()
    {
        return stage;
    }
    
    private Class stage;

    /**
     * @return If the event has already been processed
     */
    public boolean isConsumed()
    {
        return consumed;        
    }    
    
    /**
     *  Consume the event so it is not triggered multiple times
     */
    public void consume()
    {
        consumed = true;        
    }
    
    private boolean consumed = false;    
}
