/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.text.IntegerParser;
import de.carne.text.LongParser;
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
	 * Gets a system property value.
	 *
	 * @param key the property key to get.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	@SuppressWarnings("null")
	public static String value(String key, String defaultValue) {
		return System.getProperty(key, defaultValue);
	}

	/**
	 * Gets a system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	@SuppressWarnings("null")
	public static String value(Class<?> clazz, String key, String defaultValue) {
		return System.getProperty(clazz.getName() + key, defaultValue);
	}

	/**
	 * Gets a {@code boolean} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @return the property value or {@code false} if the property is not defined.
	 */
	public static boolean booleanValue(String key) {
		return booleanValue(key, false);
	}

	/**
	 * Gets a {@code boolean} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @return the property value or {@code false} if the property is not defined.
	 */
	public static boolean booleanValue(Class<?> clazz, String key) {
		return booleanValue(clazz, key, false);
	}

	/**
	 * Gets a {@code boolean} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
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
	 * Gets a {@code boolean} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the system property key (relative to the submitted {@linkplain Class}) to get.
	 * @param defaultValue The default value to return in case the property is not defined.
	 * @return The property value or the submitted default value if the property is not defined.
	 */
	public static boolean booleanValue(Class<?> clazz, String key, boolean defaultValue) {
		return booleanValue(clazz.getName() + key, defaultValue);
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @return the property value or {@code 0} if the property is not defined.
	 */
	public static int intValue(String key) {
		return intValue(key, IntegerParser.ANY, 0);
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @return the property value or {@code 0} if the property is not defined.
	 */
	public static int intValue(Class<?> clazz, String key) {
		return intValue(clazz, key, IntegerParser.ANY, 0);
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static int intValue(String key, int defaultValue) {
		return intValue(key, IntegerParser.ANY, defaultValue);
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static int intValue(Class<?> clazz, String key, int defaultValue) {
		return intValue(clazz.getName() + key, IntegerParser.ANY, defaultValue);
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @param parser the {@linkplain IntegerParser} to use for property value parsing.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static int intValue(String key, IntegerParser parser, int defaultValue) {
		String value = System.getProperty(key);
		int intValue = defaultValue;

		if (value != null) {
			try {
				intValue = parser.parseInt(value);
			} catch (RuntimeException e) {
				LOG.warning(e, "Ignoring invalid int system propert: ''{0}'' = ''{1}''", key, value);
			}
		}
		return intValue;
	}

	/**
	 * Gets a {@code int} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @param parser the {@linkplain IntegerParser} to use for property value parsing.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static int intValue(Class<?> clazz, String key, IntegerParser parser, int defaultValue) {
		return intValue(clazz.getName() + key, parser, defaultValue);
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @return the property value or {@code 0} if the property is not defined.
	 */
	public static long longValue(String key) {
		return longValue(key, LongParser.ANY, 0);
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @return the property value or {@code 0} if the property is not defined.
	 */
	public static long longValue(Class<?> clazz, String key) {
		return longValue(clazz, key, LongParser.ANY, 0);
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static long longValue(String key, long defaultValue) {
		return longValue(key, LongParser.ANY, defaultValue);
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static long longValue(Class<?> clazz, String key, long defaultValue) {
		return longValue(clazz.getName() + key, LongParser.ANY, defaultValue);
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param key the property key to retrieve.
	 * @param parser the {@linkplain LongParser} to use for property value parsing.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static long longValue(String key, LongParser parser, long defaultValue) {
		String value = System.getProperty(key);
		long longValue = defaultValue;

		if (value != null) {
			try {
				longValue = parser.parseLong(value);
			} catch (RuntimeException e) {
				LOG.warning(e, "Ignoring invalid long system propert: ''{0}'' = ''{1}''", key, value);
			}
		}
		return longValue;
	}

	/**
	 * Gets a {@code long} system property value.
	 *
	 * @param clazz the {@linkplain Class} to derive the property key from.
	 * @param key the property key (relative to the submitted {@linkplain Class}) to get.
	 * @param parser the {@linkplain LongParser} to use for property value parsing.
	 * @param defaultValue the default value to return in case the property is not defined.
	 * @return the property value or the submitted default value if the property is not defined.
	 */
	public static long longValue(Class<?> clazz, String key, LongParser parser, long defaultValue) {
		return longValue(clazz.getName() + key, parser, defaultValue);
	}

}
