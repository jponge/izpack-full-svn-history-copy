
package IzPack.TestLangPacks;

/**
 * Exception for language pack handling.
 *
 * @author Ari Voutilainen, 2008
 */
public class LangPackException extends Exception
{
   /**
    * Severity of the exception.
    */
   public enum Severity
   {
      /** Exception is information. */
      INFO,
      /** Exception is warning. */
      WARNING,
      /** Exception is error. */
      ERROR,
      /** Exception is fatal error. */
      FATAL
   };
   
   private int id = 0;
   
   /**
    * The default contructor of the class.
    */
   public LangPackException() {}
   
   /**
    * The contructor of the class.
    * @param msg  Message string for the exception.
    */
   public LangPackException(String msg)
   {
      super(msg);
   }
   
   /**
    * The contructor of the class.
    * @param id   ID for the exception.
    */
   public LangPackException(int id)
   {
      this.id = id;
   }
   
   /**
    * Returns the ID of the exception.
    */
   public int GetId()
   {
      return id;
   }
   
   public static final long serialVersionUID = 0x29324576;
}
