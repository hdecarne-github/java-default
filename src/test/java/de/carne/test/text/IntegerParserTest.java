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
package de.carne.test.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.text.IntegerParser;

/**
 * Test {@linkplain IntegerParser} class.
 */
class IntegerParserTest {

	@Test
	void testParseSuccess() {
		IntegerParser parser = IntegerParser.ANY;

		Assertions.assertEquals(Integer.valueOf(0), parser.parse("0"));
		Assertions.assertEquals(Integer.valueOf(42), parser.parse("42"));
		Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE), parser.parse(Integer.toString(Integer.MIN_VALUE)));
		Assertions.assertEquals(Integer.valueOf(Integer.MAX_VALUE), parser.parse(Integer.toString(Integer.MAX_VALUE)));
	}

	@Test
	void testParseFailure() {
		IntegerParser parser = IntegerParser.POSITIVE;

		Assertions.assertEquals(Integer.valueOf(0), parser.parse("0"));
		Assertions.assertEquals(Integer.valueOf(42), parser.parse("42"));
		Assertions.assertThrows(RuntimeException.class, () -> parser.parse("?"));
		Assertions.assertThrows(RuntimeException.class, () -> parser.parse("-1"));
	}

}
