
package IzPack.TestLangPacks;

/**
 * Contains language file items.
 * 
 * @author Ari Voutilainen, 2008
 */
public class LanguageItem
{
   private String key = null;
   private String value = null;
   private String[] unknownAttrs = null;
   
   /**
    * The default contructor of the class.
    * 
    * @param key     ID in the 'str' element.
    * @param value   Text in the 'str' element.
    */
   public LanguageItem(String key, String value)
   {
      this.key = key;
      this.value = value;
   }
   
   /**
    * Returns the ID in 'str' element.
    */
   public String GetKey()
   {
      return key;
   }
   
   /**
    * Returns the text in 'str' element.
    */
   public String GetValue()
   {
      return value;
   }
   
   /**
    * Returns the unknown attributes in 'str' element.
    */
   public String[] GetUnknownAttributes()
   {
      return unknownAttrs;
   }
   
   /**
    * Sets the ID in 'str' element.
    * 
    * @param key  ID to set.
    */
   public void SetKey(String key)
   {
      this.key = key;
   }
   
   /**
    * Sets the text in 'str' element.
    * 
    * @param value   Value to set.
    */
   public void SetValue(String value)
   {
      this.value = value;
   }
   
   /**
    * Sets the unknown attributes in 'str' element.
    * 
    * @param unknownAttrs  String array containing unknown attributes. Each
    *                      index contain element and attributes (not 'id' and
    *                      'txt').
    */
   public void SetUnknownAttributes(String[] unknownAttrs)
   {
      if (unknownAttrs.length > 0)
         this.unknownAttrs = unknownAttrs;
   }
   
   /**
    * Checks whether given ID is the same as in this object.
    * 
    * @param id   ID to check.
    * @return     true if IDs are the same. false if not.
    */
   public boolean EqualsId(String id)
   {
      return key.equals(id);
   }
}
