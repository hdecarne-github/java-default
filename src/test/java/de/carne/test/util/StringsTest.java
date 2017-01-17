/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.Strings;

/**
 * Test {@link Strings} class functionality.
 */
public class StringsTest {

	/**
	 * Test {@link Strings} class functionality.
	 */
	@Test
	public void testStrings() {
		Assert.assertTrue(Strings.isEmpty(null));
		Assert.assertTrue(Strings.isEmpty(""));
		Assert.assertFalse(Strings.isEmpty(" "));
		Assert.assertFalse(Strings.notEmpty(null));
		Assert.assertFalse(Strings.notEmpty(""));
		Assert.assertTrue(Strings.notEmpty(" "));
		Assert.assertEquals("", Strings.safe(null));
		Assert.assertEquals("", Strings.safe(""));
		Assert.assertEquals(" ", Strings.safe(" "));
		Assert.assertEquals(null, Strings.safeTrim(null));
		Assert.assertEquals("", Strings.safeTrim(""));
		Assert.assertEquals("", Strings.safeTrim(" "));
		Assert.assertArrayEquals(new String[] { "1", "2", "3" }, Strings.split("1,2,3", ","));
		Assert.assertEquals("1,2,3", Strings.join(Arrays.asList("1", "2", "3"), ","));
		Assert.assertEquals("1,2,\u2026", Strings.join(Arrays.asList("1", "2", "3"), ",", 2));
	}

}
