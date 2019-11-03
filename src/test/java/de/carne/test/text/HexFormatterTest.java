/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.text.HexFormatter;

/**
 * Test {@linkplain HexFormatter} class.
 */
class HexFormatterTest {

	@Test
	void testLowerCaseFormat() {
		HexFormatter formatter = new HexFormatter();

		Assertions.assertEquals("00", formatter.format((byte) 0x00));
		Assertions.assertEquals("ff", formatter.format((byte) 0xff));
		Assertions.assertEquals("0000", formatter.format((short) 0x0000));
		Assertions.assertEquals("ffff", formatter.format((short) 0xffff));
		Assertions.assertEquals("00000000", formatter.format(0x00000000));
		Assertions.assertEquals("ffffffff", formatter.format(0xffffffff));
		Assertions.assertEquals("0000000000000000", formatter.format(0x0000000000000000l));
		Assertions.assertEquals("ffffffffffffffff", formatter.format(0xffffffffffffffffl));
	}

	@Test
	void testUpperCaseFormat() {
		HexFormatter formatter = new HexFormatter(true);

		Assertions.assertEquals("00", formatter.format((byte) 0x00));
		Assertions.assertEquals("FF", formatter.format((byte) 0xff));
		Assertions.assertEquals("0000", formatter.format((short) 0x0000));
		Assertions.assertEquals("FFFF", formatter.format((short) 0xffff));
		Assertions.assertEquals("00000000", formatter.format(0x00000000));
		Assertions.assertEquals("FFFFFFFF", formatter.format(0xffffffff));
		Assertions.assertEquals("0000000000000000", formatter.format(0x0000000000000000l));
		Assertions.assertEquals("FFFFFFFFFFFFFFFF", formatter.format(0xffffffffffffffffl));
	}

}
