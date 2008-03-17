
package IzPack.TestLangPacks;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implements SAX2 error event handler.
 *
 * @author Ari Voutilainen, 2008
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
