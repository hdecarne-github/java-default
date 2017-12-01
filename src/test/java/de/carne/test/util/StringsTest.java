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

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.Strings;

/**
 * Test {@linkplain Strings} class.
 */
public class StringsTest {

	/**
	 * Test empty-checks.
	 */
	@Test
	public void testEmptyChecks() {
		Assert.assertTrue(Strings.isEmpty(null));
		Assert.assertTrue(Strings.isEmpty(""));
		Assert.assertFalse(Strings.isEmpty(" "));
		Assert.assertFalse(Strings.notEmpty(null));
		Assert.assertFalse(Strings.notEmpty(""));
		Assert.assertTrue(Strings.notEmpty(" "));
	}

	/**
	 * Test safe-functions.
	 */
	@Test
	public void testSafeFunctions() {
		Assert.assertEquals("", Strings.safe(null));
		Assert.assertEquals(" ", Strings.safe(" "));
		Assert.assertEquals("", Strings.safeTrim(null));
		Assert.assertEquals("", Strings.safeTrim(""));
		Assert.assertEquals("", Strings.safeTrim(" "));
		Assert.assertEquals("test", Strings.safeTrim("test"));
	}

	/**
	 * Test encode/decode functions.
	 */
	@Test
	public void testEncodeDecodeFunctions() {
		String decoded = "\0\u0001\b\t\n\f\ra";
		String encoded = "\\0\\u0001\\b\\t\\n\\f\\ra";

		Assert.assertEquals(encoded, Strings.encode(decoded));
		Assert.assertEquals(decoded, Strings.decode(encoded));
	}

}
