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

/**
 * Exception for language pack handling.
 *
 * @author Ari Voutilainen
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
   public int getId()
   {
      return id;
   }
   
   public static final long serialVersionUID = 0x29324576;
}
