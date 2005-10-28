/*
 * $Id: LangResources.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : LangResources.java 
 * Description : Model to support the application with i18n.
 * Author's email : dani@gueggs.net 
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

package izpack.frontend.model;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Model to support the application with i18n.
 * 
 * @author Daniel Guggi
 */
public class LangResources extends Observable {
	/** The resource bundle object. */
	private ResourceBundle resourceBundle = null;
	
	/** The current locale. */
	private Locale locale = null;
	
	/** The location of the bundle files. */
	private String bundleLocation = null;

	/**
	 * Set up the model. Use the default locale.
	 * @param bundleLocation where to find the bundle files
	 */
	public LangResources(String bundleLocation) {
		this.locale = Locale.getDefault();
		this.bundleLocation = bundleLocation;
		resourceBundle = ResourceBundle.getBundle(bundleLocation, locale);
	}

	/**
	 * Set up the model. Use the appropriate locale for the specified langCode.
	 * @param bundleLocation where to find the bundle files
	 * @param langCode the language to use
	 */
	public LangResources(String bundleLocation, String langCode) {
		this.locale = new Locale(langCode);
		this.bundleLocation = bundleLocation;
		resourceBundle = ResourceBundle.getBundle(bundleLocation, locale);
	}
	
	/**
	 * Set the locale to the default one
	 */
	public void setDefaultLocale() {
		setLocale(Locale.getDefault());
	}

	
	/**
	 * Get the locale.
	 * @return the current locale
	 */
	public Locale getLocale() {
		return locale;
	}

	
	/**
	 * Set a new current locale. If the locale changes this method reloads the bundle and
	 * notifies all observers afterwards.
	 * @param locale the new locale to use
	 */
	public void setLocale(Locale locale) {
		if (!this.locale.equals(locale)) {
			this.locale = locale;
			resourceBundle = ResourceBundle.getBundle(bundleLocation, this.locale);
			// notify observers
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Get the language code for the current locale.
	 * @return the language code of the current locale
	 */
	public String getLangCode() {
		return locale.getLanguage();
	}

	
	/**
	 * Set the current locale by specifing the language code.
	 * @param langCode the language code
	 */
	public void setLangCode(String langCode) {
		if (langCode != null) {
			setLocale(new Locale(langCode));
		}
	}

	/**
	 * Retrieve a property.
	 * @param propertyName the name of the property
	 * @return the property value
	 */
	public String getText(String propertyName) {
		String text = resourceBundle.getString(propertyName);		
		return text;
	}
}