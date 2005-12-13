package izpack.frontend.view.win32;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/*
 * Created on Dec 9, 2005
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

public class NativeIconAccessor
{   
    public NativeIconAccessor(String filename)
    {
        boolean successful = initializeIconSet(filename);
        
        NativeIcon ni = getNativeBuffers();
        iconBuf = ni.icon;
        maskBuf = ni.mask;
        
        instances.add(this);
    }
    
    public BufferedImage getIcon(int index)
    {
        getNativeIcon(index);
        
        iconBuf.clear();
        maskBuf.clear();
        
        // Copy buffer to BufferedImage BufferedImage bi = new
        BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        
        for (int y = 31; y >= 0; y--)
        {
            for (int x = 0; x < 32; x++)
            {
                int colorVal = createARGB(iconBuf.getInt(), maskBuf.getInt());
                
                bi.setRGB(x, y, colorVal);
            }
        }
        
        return bi;
    }       
    
    private int createARGB(int bgr, int mask)
    {   
        int r = (bgr >> 8) & 0xFF;
        int b = (bgr >> 24) & 0xFF;
        int g = (bgr >> 16) & 0xFF;        
        
        int a = (bgr & 0xFF);
        
        //Black = transparent
        if (mask == 0xFFFFFF00)
        {
            a = 0x0;
        }
        else
        {
            if (a == 0x0)
                a = 0xFF;
        }
                
        int rgbVal = (a << 24) | (r << 16) | (g << 8) | b;
        
        return rgbVal;
    }
    
    protected static void cleanupCreatedAccessors()
    {
        for (NativeIconAccessor accessor : instances)
        {
            accessor.destroyIconSet();
        }
    }
    
    private native boolean initializeIconSet(String filename);
    private native NativeIcon getNativeBuffers();    
    public native int getNumIcons();
    private native void destroyIconSet();
    
    private native void getNativeIcon(int index);
    
    private ByteBuffer iconBuf;
    private ByteBuffer maskBuf;
    
    static
    {        
        System.loadLibrary("IconLoader");
        
        Runtime.getRuntime().addShutdownHook(new NativeCleanupShutdownHook());
    }
    
    private static ArrayList<NativeIconAccessor> instances = new ArrayList<NativeIconAccessor>();
}
