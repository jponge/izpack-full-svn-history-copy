package izpack.frontend.view.win32;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/*
 * Created on Dec 13, 2005
 * 
 * $Id: NativeIcon.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : NativeIcon.java 
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

public final class NativeIcon
{
    public NativeIcon(ByteBuffer icon, ByteBuffer mask, int width, int height)
    {    
        this.icon = icon;
        this.mask = mask;
        
        this.width = width;
        this.height = height;
    }    
    
    protected ByteBuffer icon;
    protected ByteBuffer mask;
    protected int width, height;
}
