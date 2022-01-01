/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.text.LongParser;

/**
 * Test {@linkplain LongParser} class.
 */
class LongParserTest {

	@Test
	void testParseSuccess() {
		LongParser parser = LongParser.ANY;

		Assertions.assertEquals(Long.valueOf(0), parser.parse("0"));
		Assertions.assertEquals(Long.valueOf(42), parser.parse("42"));
		Assertions.assertEquals(Long.valueOf(Long.MIN_VALUE), parser.parse(Long.toString(Long.MIN_VALUE)));
		Assertions.assertEquals(Long.valueOf(Long.MAX_VALUE), parser.parse(Long.toString(Long.MAX_VALUE)));
	}

	@Test
	void testParseFailure() {
		LongParser parser = LongParser.POSITIVE;

		Assertions.assertEquals(Long.valueOf(0), parser.parse("0"));
		Assertions.assertEquals(Long.valueOf(42), parser.parse("42"));
		Assertions.assertThrows(RuntimeException.class, () -> parser.parse("?"));
		Assertions.assertThrows(RuntimeException.class, () -> parser.parse("-1"));
	}

}
