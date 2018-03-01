/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.util.logging.Log;

/**
 * Utility class providing system properties related functions.
 */
public final class SystemProperties {

	private static final Log LOG = new Log();

	private SystemProperties() {
		// Prevent instantiation
	}

	/**
	 * Get {@code boolean} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @return The property value or {@code false} if the property is not defined.
	 */
	public static boolean booleanValue(String key) {
		return booleanValue(key, false);
	}

	/**
	 * Get {@code boolean} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @param defaultValue The default value to return in case the property is not defined.
	 * @return The property value or the submitted default value if the property is not defined.
	 */
	public static boolean booleanValue(String key, boolean defaultValue) {
		String value = System.getProperty(key);
		boolean booleanValue = defaultValue;

		if (value != null) {
			booleanValue = Boolean.parseBoolean(value);
		}
		return booleanValue;
	}

	/**
	 * Get {@code int} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @return The property value or {@code 0} if the property is not defined.
	 */
	public static int intValue(String key) {
		return intValue(key, 0);
	}

	/**
	 * Get {@code int} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @param defaultValue The default value to return in case the property is not defined.
	 * @return The property value or the submitted default value if the property is not defined.
	 */
	public static int intValue(String key, int defaultValue) {
		String value = System.getProperty(key);
		int intValue = defaultValue;

		if (value != null) {
			try {
				intValue = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				LOG.warning(e, "Ignoring invalid int system propert: ''{0}'' = ''{1}''", key, value);
			}
		}
		return intValue;
	}

	/**
	 * Get {@code long} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @return The property value or {@code 0} if the property is not defined.
	 */
	public static long longValue(String key) {
		return longValue(key, 0);
	}

	/**
	 * Get {@code long} system property value.
	 *
	 * @param key The property key to retrieve.
	 * @param defaultValue The default value to return in case the property is not defined.
	 * @return The property value or the submitted default value if the property is not defined.
	 */
	public static long longValue(String key, long defaultValue) {
		String value = System.getProperty(key);
		long longValue = defaultValue;

		if (value != null) {
			try {
				longValue = Long.parseLong(value);
			} catch (NumberFormatException e) {
				LOG.warning(e, "Ignoring invalid long system propert: ''{0}'' = ''{1}''", key, value);
			}
		}
		return longValue;
	}

}
