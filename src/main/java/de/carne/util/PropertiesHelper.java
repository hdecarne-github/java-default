/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.util;

import java.util.Properties;

import de.carne.util.logging.Log;

/**
 * Utility class providing {@link Properties} related functions.
 */
public final class PropertiesHelper {

	private static final Log LOG = new Log();

	private PropertiesHelper() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Get a {@link String} system property.
	 * <p>
	 * The system property key to retrieve is created by concatenating the
	 * package name of the submitted class with the submitted key.
	 * </p>
	 *
	 * @param clazz The class to use for system property key creation.
	 * @param key The key to use for system property key creation.
	 * @param def The default value to use in case the system property is
	 *        undefined.
	 * @return The system property value or the submitted default value if the
	 *         system property is undefined.
	 */
	public static String get(Class<?> clazz, String key, String def) {
		assert clazz != null;
		assert key != null;

		return System.getProperty(systemPropertyKey(clazz, key), def);
	}

	/**
	 * Get a {@code int} system property.
	 * <p>
	 * The system property key to retrieve is created by concatenating the
	 * package name of the submitted class with the submitted key.
	 * </p>
	 *
	 * @param clazz The class to use for system property key creation.
	 * @param key The key to use for system property key creation.
	 * @param def The default value to use in case the system property is
	 *        undefined.
	 * @return The system property value or the submitted default value if the
	 *         system property is undefined.
	 */
	public static int getInt(Class<?> clazz, String key, int def) {
		assert clazz != null;
		assert key != null;

		return getInt(System.getProperties(), systemPropertyKey(clazz, key), def);
	}

	/**
	 * Get a {@link String} property.
	 *
	 * @param properties The properties object to evaluate.
	 * @param key The property key to retrieve.
	 * @param def The default value to use in case the property is undefined.
	 * @return The property value or the submitted default value if the property
	 *         is undefined.
	 */
	public static String get(Properties properties, String key, String def) {
		assert properties != null;
		assert key != null;

		return properties.getProperty(key, def);
	}

	/**
	 * Get a {@code int} property.
	 *
	 * @param properties The properties object to evaluate.
	 * @param key The property key to retrieve.
	 * @param def The default value to use in case the property is undefined.
	 * @return The property value or the submitted default value if the property
	 *         is undefined.
	 */
	public static int getInt(Properties properties, String key, int def) {
		assert properties != null;
		assert key != null;

		return toInt(properties.getProperty(key), key, def);
	}

	private static String systemPropertyKey(Class<?> clazz, String key) {
		return clazz.getPackage().getName() + key;
	}

	private static int toInt(String property, String key, int def) {
		int propertyValue = def;

		try {
			propertyValue = Integer.valueOf(property).intValue();
		} catch (NumberFormatException e) {
			LOG.warning(e, "Invalid integer property value ''{0}'' = ''{1}''; using default value", key, property);
		}
		return propertyValue;
	}

}
