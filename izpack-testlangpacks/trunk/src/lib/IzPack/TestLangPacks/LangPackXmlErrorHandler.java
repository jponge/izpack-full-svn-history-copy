/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2008 Ari Voutilainen
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

package IzPack.TestLangPacks;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implements SAX2 error event handler.
 *
 * @author Ari Voutilainen
 */
public class LangPackXmlErrorHandler extends DefaultHandler implements ErrorHandler
{
   /**
    * Deafult constructor of the class.
    */
   public LangPackXmlErrorHandler()
   {
      super();
   }

   /**
    * Receive notification of a recoverable error.
    *
    * @param exception
    *
    * @throws SAXException
    */
   @Override
   public void error(SAXParseException exception) throws SAXException
   {
      throw new SAXException(makeDetailedMessage(LangPackException.Severity.ERROR, exception));
   }

   /**
    * Receive notification of a non-recoverable error.
    *
    * @param exception
    *
    * @throws SAXException
    */
   @Override
   public void fatalError(SAXParseException exception) throws SAXException
   {
      throw new SAXException(makeDetailedMessage(LangPackException.Severity.FATAL, exception));
   }

   /**
    * Receive notification of a warning.
    *
    * @param exception
    *
    * @throws SAXException
    */
   @Override
   public void warning(SAXParseException exception) throws SAXException
   {
      throw new SAXException(makeDetailedMessage(LangPackException.Severity.WARNING, exception));
   }

   /**
    * Makes readable message for printing or saving.
    *
    * @param flag      The flag which indicates whether the exception is warning,
    *                  error or fatal error.
    * @param exception SAXParseException which contain information about the exception.
    *
    * @return          Return the message as String.
    */
   private String makeDetailedMessage(LangPackException.Severity flag, SAXParseException exception)
   {
      String flagStr = "";

      if (flag == LangPackException.Severity.WARNING)
      {
         flagStr = "Warning";
      }
      if (flag == LangPackException.Severity.ERROR)
      {
         flagStr = "Error";
      }
      if (flag == LangPackException.Severity.FATAL)
      {
         flagStr = "Fatal error";
      }

      int               lineNum   = exception.getLineNumber(),
                        columnNum = exception.getColumnNumber();
      String            publId    = exception.getPublicId();
      ArrayList<String> aList     = new ArrayList<String>();

      if (lineNum != -1)
      {
         aList.add(" line: " + lineNum);
      }
      if (columnNum != -1)
      {
         aList.add(" column: " + columnNum);
      }
      if (publId != null)
      {
         aList.add(" id: \"" + publId + "\"");
      }

      String msg = "[" + flagStr;
      int    max = aList.size();

      if (max > 0)
      {
         msg = msg + ";";

         int x = 0;
         for (x = 0; x < max - 1; x++)
         {
            msg = msg + " " + aList.get(x) + ",";
         }
         msg = msg + " " + aList.get(x);
      }
      msg = msg + "]: " + exception.getMessage();
      return msg;
   }
}
