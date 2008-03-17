
package IzPack.TestLangPacks;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Main routine for language pack handling.
 *
 * @author Ari Voutilainen, 2008
 */
public class Main
{
   /** */
   private static final String APP_VERSION = "0.1";

   /**
    * Main method to run.
    *
    * @param args  The command line arguments.
    */
   public static void main(String[] args)
   {
      System.out.println("\nTestLangPacks in IzPack for review    version " + APP_VERSION);
      System.out.println("");

      // We need at least one argument.
      if (args.length==0)
      {
         System.out.println("No any IzPack language file given to be checked!\n");
         PrintHelp();
         return;
      }

      if (args[0].equals("-h") || args[0].equals("-?"))
      {
         PrintHelp();
         return;
      }

      String xmlBaseFile = "eng.xml";
      String xmlTestFile = null;

      // Handle command line arguments.
      for (int x=0; x < args.length; x++)
      {
         if (args[x].equals("-b"))
         {
            if (x < args.length)
            {
               x++;
               xmlBaseFile = args[x];
            }
         }
         else if (args[x].contains("-"))
         {
            System.err.println("Command line contains invalid argument: \""+args[x]+"\"!\n");
            PrintHelp();
            return;
         }
         else
            xmlTestFile = args[x];
      }

      LangPack langPackBase = null;
      LangPack langPackTest = null;
      ArrayList<LanguageItem> langItemsBase = null;
      ArrayList<LanguageItem> langItemsTest = null;

      // Load and parse language XML base file.
      System.out.println("Loading and checking base file: " + xmlBaseFile);
      try
      {
         langPackBase = new LangPack(xmlBaseFile);
         langItemsBase = langPackBase.GetLangItems();

      }
      catch (Exception e)
      {
         PrintMessage(System.err, "Error in base file", e.getMessage());
         return;
      }

      // Load and parse language XML file to be tested.
      System.out.println("\nLoading and checking file to check: " + xmlTestFile);
      try
      {
         langPackTest = new LangPack(xmlTestFile);
         langItemsTest = langPackTest.GetLangItems();
      }
      catch (Exception e)
      {
         PrintMessage(System.err, "Error in file to be tested", e.getMessage());
         return;
      }

      // Finding missing string ID's.
      // ---------------------------
      System.out.println("\nFinding ID's which should be added ("+xmlBaseFile+" => "+xmlTestFile+"):");

      boolean found = false;
      String key = null;
      for (LanguageItem itemBase : langItemsBase)
      {
         key = itemBase.GetKey();
         // If there isn't any ID string,
         if (key==null)
            continue;   // just go to next.
         for (LanguageItem itemToCheck : langItemsTest)
         {
            String checkKey = itemToCheck.GetKey();
            if (checkKey!=null)
            {
               if (key.equals(checkKey))
               {
                  if (itemToCheck.GetValue() != null)
                     found = true;  // Only filled id and txt will procedure true!
               }
            }
         }
         if (!found)
         {
            System.out.println(MakeKeyValueString(key, itemBase.GetValue()));
            found = false;
         }
      }
      if (key==null)
         System.out.println("   (none)");

      // Finding ID's which are not needed anymore.
      // -----------------------------------------
      System.out.println("\nFinding ID's which are not needed anymore in "+xmlTestFile+":");

      key = null;
      found = false;
      for (LanguageItem itemToCheck : langItemsTest)
      {
         key = itemToCheck.GetKey();
         // If there isn't any ID string,
         if (key==null)
            continue;   // just go to next.
         for (LanguageItem itemBase : langItemsBase)
         {
            String checkKey = itemBase.GetKey();
            String checkValue = itemBase.GetValue();
            if (checkKey!=null && checkValue!=null)
            {
               if (checkKey.equals(key))
               {
                  if (checkValue != null)
                     found = true;  // Only filled id and txt will procedure true!
               }
            }
         }
         if (!found)
         {
            System.out.println(MakeKeyValueString(key, itemToCheck.GetValue()));
            found = false;
         }
      }
      if (key==null)
         System.out.println("   (none)");
      
      // Check unknown attributes.
      // ------------------------
      System.out.println("\nFinding unknown attributes in "+xmlTestFile+":");

      found = false;
      for (LanguageItem itemToCheck : langItemsTest)
      {
         String[] unknownAttrs = itemToCheck.GetUnknownAttributes();
         if (unknownAttrs != null)
         {
            String msg = "   Found unknown attributes in";
            msg = msg + MakeKeyValueString(itemToCheck.GetKey(), itemToCheck.GetValue());
            System.out.println(msg + ":");
            int max = unknownAttrs.length;
            for (int x=0; x < max; x++)
               System.out.println("      "+unknownAttrs[x]);
            unknownAttrs = null;
            found = true;
         }
      }
      if (!found)
         System.out.println("   (none)");

      // Finding unknown elements.
      // ------------------------
      System.out.println("\nFinding unknown elements in "+xmlTestFile+":");
      System.out.println("(means elements which are not supported.)");

      String[] unknownElements = langPackTest.GetUnknownElements();
      if (unknownElements.length==0)
         System.out.println("   (none)");
      else
      {
         for (String elem : unknownElements)
            System.out.println("   <"+elem+" ... />");
      }
   }

   /**
    * Prints the message on the console screen.
    *
    * @param outStream  Output stream. System.out or System.err.
    * @param caption    The caption of the message.
    * @param msg        The message.
    */
   public static void PrintMessage(PrintStream outStream, String caption, String msg)
   {
      String totMsg = "";
      if (caption!=null && caption.contains(""))
         totMsg = "\n" + caption + ":\n   ";
      totMsg = totMsg +  msg;
      outStream.println(totMsg);
   }

   /**
    * Returns the string made with id and txt.
    *
    * @param key     ID found from 'str' element.
    * @param value   Text value found from 'str' element.
    */
   private static String MakeKeyValueString(String key, String value)
   {
      String msg = "   str ";
      //"   str id=\"" + key + "\" txt=\"" + itemBase.GetValue() + "\"");
      if (key==null || key.equals(""))
         msg = msg + "(id missing!) ";
      else
         msg = msg + "id=\"" + key + "\" ";

      if (value==null || value.equals(""))
         msg = msg + "(txt missing!)";
      else
         msg = msg + "txt=\"" + value + "\"";

      return msg;
   }

   /**
    * Prints the help on the System.out.
    */
   private static void PrintHelp()
   {
      String helpMsg =
      "Usage: java -jar TestLangPacks.jar {langfile} [-b {basefile}]\n"+
      "where:\n\n"+
      "{langfile} is the IzPack language file to be tested. File name can contain path\n"+
      "           to the file.\n\n"+
      "{basefile} is the IzPack language file which is used as a base file to the\n"+
      "           comparisons. If -b exists there must be space between -b and\n"+
      "           {basefile}. The name can contain also a path. If you want to give\n"+
      "           the path to default base file you must give both path and file.\n"+
      "           This file is optional. Default base file name is eng.xml.\n\n"+
      "If you give -? or -h you will get this help.\n\n"
      ;
      System.out.print(helpMsg);
   }
}
