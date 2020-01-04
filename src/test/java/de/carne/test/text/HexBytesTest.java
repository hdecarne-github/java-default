/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.text.HexBytes;

/**
 * Test {@linkplain HexBytes} class.
 */
class HexBytesTest {

	private static final byte[] TEST_BYTES = new byte[] { 0x00, 0x01, (byte) 0x80, (byte) 0xfe, (byte) 0xff };
	private static final String TEST_HEX_SEQUENCE = "000180fEfF";
	private static final String TEST_HEX_SEQUENCE_L = TEST_HEX_SEQUENCE.toLowerCase();
	private static final String TEST_HEX_SEQUENCE_U = TEST_HEX_SEQUENCE.toUpperCase();

	@Test
	void testToString() {
		Assertions.assertEquals(TEST_HEX_SEQUENCE_L, HexBytes.toStringL(TEST_BYTES));
		Assertions.assertEquals(TEST_HEX_SEQUENCE_U, HexBytes.toStringU(TEST_BYTES));
	}

	@Test
	void testValueOf() {
		Assertions.assertArrayEquals(TEST_BYTES, HexBytes.valueOf(TEST_HEX_SEQUENCE));
		Assertions.assertArrayEquals(TEST_BYTES, HexBytes.valueOf(TEST_HEX_SEQUENCE_L));
		Assertions.assertArrayEquals(TEST_BYTES, HexBytes.valueOf(TEST_HEX_SEQUENCE_U));
	}

}
