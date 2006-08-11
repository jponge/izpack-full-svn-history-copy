/*
 * Created on May 4, 2006
 * 
 * $Id: DocumentationGenerator.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : DocumentationGenerator.java 
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

package documentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.Segment;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;

public class DocumentationGenerator
{

    /**
     * @param args
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException
    {        
        Source src = new Source(new FileInputStream(new File("H:\\izpack-src\\src\\doc-ng\\XHTML\\node4.html")));
        src.setLogWriter(new PrintWriter(System.err));       
        
        List<Element> tags = src.findAllElements("h2");
        
        for (Element tag : tags)
        {       
            String text = tag.extractText();
           
           if (text.endsWith("Panel"))
           {
               System.out.println(text);
               
               StartTag t = src.findNextStartTag(tag.getEnd(), "h2");
               
               if (t == null)
                   t = src.findNextStartTag(tag.getEnd(), "hr");
                   
               System.out.println("\t" + new Segment(src, tag.getEnd(), t.getBegin()).extractText());
           }
        }
    }

}
