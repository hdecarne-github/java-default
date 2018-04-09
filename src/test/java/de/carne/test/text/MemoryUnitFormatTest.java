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
package de.carne.test.text;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.text.MemoryUnitFormat;

/**
 * Test {@linkplain MemoryUnitFormat} class.
 */
class MemoryUnitFormatTest {

	@Test
	void testFormatDouble() {
		MemoryUnitFormat format = new MemoryUnitFormat(Locale.GERMANY);

		Assertions.assertEquals("0 byte", format.format(0.0));
		Assertions.assertEquals("1 KiB", format.format(1024.0));
		Assertions.assertEquals("1 MiB", format.format(1024.0 * 1024.0));
		Assertions.assertEquals("1,205 KiB", format.format(1234.0));
		Assertions.assertEquals("11,774 MiB", format.format(12345678.0));
	}

	@Test
	void testFormatLong() {
		MemoryUnitFormat format = new MemoryUnitFormat(Locale.GERMANY);

		Assertions.assertEquals("0 byte", format.format(0));
		Assertions.assertEquals("1.024 byte", format.format(1024));
		Assertions.assertEquals("1.024 KiB", format.format(1024 * 1024));
		Assertions.assertEquals("1.234 byte", format.format(1234));
		Assertions.assertEquals("11 MiB", format.format(12345678));
	}

	@Test
	void testParse() throws ParseException {
		MemoryUnitFormat format = new MemoryUnitFormat(Locale.GERMANY);

		Assertions.assertEquals(0, format.parse("0 byte").longValue());
		Assertions.assertEquals(1024, format.parse("1.024 byte").longValue());
		Assertions.assertEquals(1024 * 1024, format.parse("1.024 KiB").longValue());
		Assertions.assertEquals(1234, format.parse("1.234 byte").longValue());
		Assertions.assertEquals(11 * 1024 * 1024, format.parse("11 MiB").longValue());
		Assertions.assertEquals(1233, format.parse("1,205 KiB").longValue());
		Assertions.assertEquals(12345933, format.parse("11,774 MiB").longValue());
	}

}
