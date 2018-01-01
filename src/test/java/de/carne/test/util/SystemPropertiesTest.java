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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.util.SystemProperties;

/**
 * Test {@linkplain SystemProperties} class.
 */
public class SystemPropertiesTest {

	private static final String KEY_UNDEFINED = "sysprop.undefined";
	private static final String KEY_EMPTY = "sysprop.empty";
	private static final String KEY_BOOLEAN_TRUE = "sysprop.boolean.true";
	private static final String KEY_BOOLEAN_FALSE = "sysprop.boolean.false";
	private static final String KEY_INT_0 = "sysprop.int.zero";
	private static final String KEY_INT_42 = "sysprop.int.42";

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty(KEY_EMPTY, "");
		System.setProperty(KEY_BOOLEAN_TRUE, Boolean.TRUE.toString());
		System.setProperty(KEY_BOOLEAN_FALSE, Boolean.FALSE.toString());
		System.setProperty(KEY_INT_0, "0");
		System.setProperty(KEY_INT_42, "42");
	}

	/**
	 * Test boolean access functions.
	 */
	@Test
	public void testBooleans() {
		Assert.assertFalse(SystemProperties.booleanValue(KEY_UNDEFINED));
		Assert.assertTrue(SystemProperties.booleanValue(KEY_BOOLEAN_TRUE, false));
		Assert.assertFalse(SystemProperties.booleanValue(KEY_BOOLEAN_FALSE, true));
	}

	/**
	 * Test int access functions.
	 */
	@Test
	public void testInts() {
		Assert.assertEquals(0, SystemProperties.intValue(KEY_UNDEFINED));
		Assert.assertEquals(0, SystemProperties.intValue(KEY_BOOLEAN_TRUE));
		Assert.assertEquals(0, SystemProperties.intValue(KEY_UNDEFINED, 0));
		Assert.assertEquals(1, SystemProperties.intValue(KEY_UNDEFINED, 1));
		Assert.assertEquals(0, SystemProperties.intValue(KEY_INT_0, -1));
		Assert.assertEquals(42, SystemProperties.intValue(KEY_INT_42, -1));
	}

}
