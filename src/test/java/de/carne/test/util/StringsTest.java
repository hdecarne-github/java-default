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
import org.junit.jupiter.api.Test;

import de.carne.util.Strings;

/**
 * Test {@linkplain Strings} class.
 */
class StringsTest {

	@Test
	void testIsEmpty() {
		Assertions.assertTrue(Strings.isEmpty(null));
		Assertions.assertTrue(Strings.isEmpty(""));
		Assertions.assertFalse(Strings.isEmpty(" "));
		Assertions.assertFalse(Strings.notEmpty(null));
		Assertions.assertFalse(Strings.notEmpty(""));
		Assertions.assertTrue(Strings.notEmpty(" "));
	}

	@Test
	void testSafe() {
		Assertions.assertEquals("", Strings.safe(null));
		Assertions.assertEquals(" ", Strings.safe(" "));
	}

	@Test
	void testSafeTrim() {
		Assertions.assertEquals("", Strings.safeTrim(null));
		Assertions.assertEquals("", Strings.safeTrim(""));
		Assertions.assertEquals("", Strings.safeTrim(" "));
		Assertions.assertEquals("test", Strings.safeTrim("test"));
	}

	@Test
	void testTrim() {
		Assertions.assertEquals(null, Strings.trim(null));
		Assertions.assertEquals("", Strings.trim(""));
		Assertions.assertEquals("", Strings.trim(" "));
		Assertions.assertEquals("test", Strings.trim("test"));
	}

	@Test
	void testSplit() {
		Assertions.assertArrayEquals(new String[] { "l", "m=r" }, Strings.split("l=m=r", '=', false));
		Assertions.assertArrayEquals(new String[] { "l", "m", "r" }, Strings.split("l=m=r", '=', true));
		Assertions.assertArrayEquals(new String[] { "l" }, Strings.split("l", '=', true));
		Assertions.assertArrayEquals(new String[] { "", "l" }, Strings.split("=l", '=', true));
		Assertions.assertArrayEquals(new String[] { "l", "" }, Strings.split("l=", '=', true));
	}

	@Test
	void testEncodeDecodeFunctions() {
		String decoded = "\\\0\u08af\b\t\n\f\ra";
		String encoded = "\\\\\\0\\u08AF\\b\\t\\n\\f\\ra";

		Assertions.assertEquals(encoded, Strings.encode(decoded));
		Assertions.assertEquals(decoded, Strings.decode(encoded));
	}

	@Test
	void testDecodeFailure() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Strings.decode("\\?");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Strings.decode("\\uXXXXXx");
		});
	}

}
