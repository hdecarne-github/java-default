/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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

	private static final String KEY_UNDEFINED = ".undefined";
	private static final String KEY_EMPTY = ".empty";
	private static final String KEY_BOOLEAN_TRUE = ".booleanTrue";
	private static final String KEY_BOOLEAN_FALSE = ".booleanFalse";
	private static final String KEY_INT_0 = ".intZero";
	private static final String KEY_INT_42 = ".int42";
	private static final String KEY_LONG_0 = ".longZero";
	private static final String KEY_LONG_42 = ".long42";

	@BeforeAll
	static void setUpSystemProperties() {
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_EMPTY, "");
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_BOOLEAN_TRUE, Boolean.TRUE.toString());
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_BOOLEAN_FALSE, Boolean.FALSE.toString());
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_INT_0, "0");
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_INT_42, "42");
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_LONG_0, "0");
		System.setProperty(SystemPropertiesTest.class.getName() + KEY_LONG_42, "42");
	}

	@Test
	void testStringProperties() {
		Assertions.assertEquals("?", SystemProperties.value(getClass(), KEY_UNDEFINED, "?"));
		Assertions.assertEquals("", SystemProperties.value(getClass(), KEY_EMPTY, "?"));
	}

	@Test
	void testBooleanProperties() {
		Assertions.assertFalse(SystemProperties.booleanValue(getClass(), KEY_UNDEFINED));
		Assertions.assertTrue(SystemProperties.booleanValue(getClass(), KEY_BOOLEAN_TRUE, false));
		Assertions.assertFalse(SystemProperties.booleanValue(getClass(), KEY_BOOLEAN_FALSE, true));
	}

	@Test
	void testInts() {
		Assertions.assertEquals(0, SystemProperties.intValue(getClass(), KEY_UNDEFINED));
		Assertions.assertEquals(0, SystemProperties.intValue(getClass(), KEY_BOOLEAN_TRUE));
		Assertions.assertEquals(0, SystemProperties.intValue(getClass(), KEY_UNDEFINED, 0));
		Assertions.assertEquals(1, SystemProperties.intValue(getClass(), KEY_UNDEFINED, 1));
		Assertions.assertEquals(0, SystemProperties.intValue(getClass(), KEY_INT_0, -1));
		Assertions.assertEquals(42, SystemProperties.intValue(getClass(), KEY_INT_42, -1));
	}

	@Test
	void testLongs() {
		Assertions.assertEquals(0l, SystemProperties.longValue(getClass(), KEY_UNDEFINED));
		Assertions.assertEquals(0l, SystemProperties.longValue(getClass(), KEY_BOOLEAN_TRUE));
		Assertions.assertEquals(0l, SystemProperties.longValue(getClass(), KEY_UNDEFINED, 0));
		Assertions.assertEquals(1l, SystemProperties.longValue(getClass(), KEY_UNDEFINED, 1));
		Assertions.assertEquals(0l, SystemProperties.longValue(getClass(), KEY_LONG_0, -1));
		Assertions.assertEquals(42l, SystemProperties.longValue(getClass(), KEY_LONG_42, -1));
	}

}
