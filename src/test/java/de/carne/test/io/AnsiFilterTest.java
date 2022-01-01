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
package de.carne.test.io;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.io.AnsiFilter;

/**
 * Test {@linkplain AnsiFilter} class.
 */
class AnsiFilterTest {

	private static final char[] IN_NO_ANSI = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char[] OUT_NO_ANSI = IN_NO_ANSI;
	private static final char[] IN_VALID_ANSI_1 = "\u001b\u009b\u0040x".toCharArray();
	private static final char[] OUT_VALID_ANSI_1 = "x".toCharArray();
	private static final char[] IN_VALID_ANSI_2 = "\u001b\u009b\u0030\u0040x".toCharArray();
	private static final char[] OUT_VALID_ANSI_2 = "x".toCharArray();
	private static final char[] IN_VALID_ANSI_3 = "\u001b\u009b\u0030\u0020\u0040x".toCharArray();
	private static final char[] OUT_VALID_ANSI_3 = "x".toCharArray();
	private static final char[] IN_UNKNOWN_ANSI_1 = "\u001b".toCharArray();
	private static final char[] OUT_UNKNOWN_ANSI_1 = "\u001b".toCharArray();
	private static final char[] IN_UNKNOWN_ANSI_2 = "\u001bx".toCharArray();
	private static final char[] OUT_UNKNOWN_ANSI_2 = "\u001bx".toCharArray();

	@Test
	void testNoAnsi() throws IOException {
		Assertions.assertArrayEquals(OUT_NO_ANSI, filterChar(IN_NO_ANSI));
		Assertions.assertArrayEquals(OUT_NO_ANSI, filterArray(IN_NO_ANSI));
		Assertions.assertEquals(new String(OUT_NO_ANSI), filterString(new String(IN_NO_ANSI)));
	}

	@Test
	void testValidAnsi1() throws IOException {
		Assertions.assertArrayEquals(OUT_VALID_ANSI_1, filterChar(IN_VALID_ANSI_1));
		Assertions.assertArrayEquals(OUT_VALID_ANSI_1, filterArray(IN_VALID_ANSI_1));
		Assertions.assertEquals(new String(OUT_VALID_ANSI_1), filterString(new String(IN_VALID_ANSI_1)));
	}

	@Test
	void testValidAnsi2() throws IOException {
		Assertions.assertArrayEquals(OUT_VALID_ANSI_2, filterChar(IN_VALID_ANSI_2));
		Assertions.assertArrayEquals(OUT_VALID_ANSI_2, filterArray(IN_VALID_ANSI_2));
		Assertions.assertEquals(new String(OUT_VALID_ANSI_2), filterString(new String(IN_VALID_ANSI_2)));
	}

	@Test
	void testValidAnsi3() throws IOException {
		Assertions.assertArrayEquals(OUT_VALID_ANSI_3, filterChar(IN_VALID_ANSI_3));
		Assertions.assertArrayEquals(OUT_VALID_ANSI_3, filterArray(IN_VALID_ANSI_3));
		Assertions.assertEquals(new String(OUT_VALID_ANSI_3), filterString(new String(IN_VALID_ANSI_3)));
	}

	@Test
	void testUnknownAnsi1() throws IOException {
		Assertions.assertArrayEquals(OUT_UNKNOWN_ANSI_1, filterChar(IN_UNKNOWN_ANSI_1));
		Assertions.assertArrayEquals(OUT_UNKNOWN_ANSI_1, filterArray(IN_UNKNOWN_ANSI_1));
		Assertions.assertEquals(new String(OUT_UNKNOWN_ANSI_1), filterString(new String(IN_UNKNOWN_ANSI_1)));
	}

	@Test
	void testUnknownAnsi2() throws IOException {
		Assertions.assertArrayEquals(OUT_UNKNOWN_ANSI_2, filterChar(IN_UNKNOWN_ANSI_2));
		Assertions.assertArrayEquals(OUT_UNKNOWN_ANSI_2, filterArray(IN_UNKNOWN_ANSI_2));
		Assertions.assertEquals(new String(OUT_UNKNOWN_ANSI_2), filterString(new String(IN_UNKNOWN_ANSI_2)));
	}

	private char[] filterChar(char[] in) throws IOException {
		StringWriter buffer = new StringWriter();
		@SuppressWarnings("resource") AnsiFilter filter = new AnsiFilter(buffer);

		for (char c : in) {
			filter.write(c & 0xff);
		}
		filter.flush();
		return buffer.toString().toCharArray();
	}

	private char[] filterArray(char[] in) throws IOException {
		StringWriter buffer = new StringWriter();
		@SuppressWarnings("resource") AnsiFilter filter = new AnsiFilter(buffer);

		filter.write(in);
		filter.flush();
		return buffer.toString().toCharArray();
	}

	private String filterString(String in) throws IOException {
		StringWriter buffer = new StringWriter();
		@SuppressWarnings("resource") AnsiFilter filter = new AnsiFilter(buffer);

		filter.write(in);
		filter.flush();
		return buffer.toString();
	}

}
