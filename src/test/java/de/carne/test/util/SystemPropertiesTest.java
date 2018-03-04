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
package de.carne.test.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.carne.util.SystemProperties;

/**
 * Test {@linkplain SystemProperties} class.
 */
class SystemPropertiesTest {

	private static final String KEY_UNDEFINED = "sysprop.undefined";
	private static final String KEY_EMPTY = "sysprop.empty";
	private static final String KEY_BOOLEAN_TRUE = "sysprop.boolean.true";
	private static final String KEY_BOOLEAN_FALSE = "sysprop.boolean.false";
	private static final String KEY_INT_0 = "sysprop.int.zero";
	private static final String KEY_INT_42 = "sysprop.int.42";
	private static final String KEY_LONG_0 = "sysprop.long.zero";
	private static final String KEY_LONG_42 = "sysprop.long.42";

	@BeforeAll
	static void setUpSystemProperties() {
		System.setProperty(KEY_EMPTY, "");
		System.setProperty(KEY_BOOLEAN_TRUE, Boolean.TRUE.toString());
		System.setProperty(KEY_BOOLEAN_FALSE, Boolean.FALSE.toString());
		System.setProperty(KEY_INT_0, "0");
		System.setProperty(KEY_INT_42, "42");
		System.setProperty(KEY_LONG_0, "0");
		System.setProperty(KEY_LONG_42, "42");
	}

	@Test
	void testBooleans() {
		Assertions.assertFalse(SystemProperties.booleanValue(KEY_UNDEFINED));
		Assertions.assertTrue(SystemProperties.booleanValue(KEY_BOOLEAN_TRUE, false));
		Assertions.assertFalse(SystemProperties.booleanValue(KEY_BOOLEAN_FALSE, true));
	}

	@Test
	void testInts() {
		Assertions.assertEquals(0, SystemProperties.intValue(KEY_UNDEFINED));
		Assertions.assertEquals(0, SystemProperties.intValue(KEY_BOOLEAN_TRUE));
		Assertions.assertEquals(0, SystemProperties.intValue(KEY_UNDEFINED, 0));
		Assertions.assertEquals(1, SystemProperties.intValue(KEY_UNDEFINED, 1));
		Assertions.assertEquals(0, SystemProperties.intValue(KEY_INT_0, -1));
		Assertions.assertEquals(42, SystemProperties.intValue(KEY_INT_42, -1));
	}

	@Test
	void testLongs() {
		Assertions.assertEquals(0l, SystemProperties.longValue(KEY_UNDEFINED));
		Assertions.assertEquals(0l, SystemProperties.longValue(KEY_BOOLEAN_TRUE));
		Assertions.assertEquals(0l, SystemProperties.longValue(KEY_UNDEFINED, 0));
		Assertions.assertEquals(1l, SystemProperties.longValue(KEY_UNDEFINED, 1));
		Assertions.assertEquals(0l, SystemProperties.longValue(KEY_LONG_0, -1));
		Assertions.assertEquals(42l, SystemProperties.longValue(KEY_LONG_42, -1));
	}

}
