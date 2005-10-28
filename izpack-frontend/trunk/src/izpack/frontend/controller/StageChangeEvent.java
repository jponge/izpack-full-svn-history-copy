/*
 * Created on Apr 13, 2005
 * 
 * $Id: StageChangeEvent.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : StageChangeEvent.java 
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
