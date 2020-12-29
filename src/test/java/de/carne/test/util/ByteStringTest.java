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
package de.carne.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.ByteString;

/**
 * Test {@linkplain ByteString} class.
 */
class ByteStringTest {

	private static final byte[] TEST_BYTES_0 = new byte[] {};
	private static final byte[] TEST_BYTES_1 = new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x80,
			(byte) 0xfe, (byte) 0xff };
	private static final byte[] TEST_BYTES_2 = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x7f };
	private static final byte[] TEST_BYTES_3 = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x7f, (byte) 0x80,
			(byte) 0x81, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85, (byte) 0x86, (byte) 0x87, (byte) 0x88,
			(byte) 0x89, (byte) 0x8a, (byte) 0x8b, (byte) 0xfe, (byte) 0xff };

	@Test
	void testWrapping() {
		ByteString bs0a = ByteString.wrap(TEST_BYTES_0);
		ByteString bs0b = ByteString.wrap(TEST_BYTES_0, 0, TEST_BYTES_0.length);

		Assertions.assertSame(ByteString.EMPTY, bs0a);
		Assertions.assertSame(ByteString.EMPTY, bs0b);

		byte[] mutableBytes1 = Arrays.copyOf(TEST_BYTES_1, TEST_BYTES_1.length);

		ByteString bs1 = ByteString.wrap(mutableBytes1);

		Assertions.assertEquals(TEST_BYTES_1.length, bs1.length());

		for (int byteIndex = 0; byteIndex < TEST_BYTES_1.length; byteIndex++) {
			Assertions.assertEquals(TEST_BYTES_1[byteIndex], bs1.byteAt(byteIndex));
		}

		mutableBytes1[1] = (byte) 0x02;

		Assertions.assertNotEquals(TEST_BYTES_1[1], bs1.byteAt(1));
	}

	@Test
	void testCopying() {
		ByteString bs0 = ByteString.copy(TEST_BYTES_0);

		Assertions.assertSame(ByteString.EMPTY, bs0);

		byte[] mutableBytes1 = Arrays.copyOf(TEST_BYTES_1, TEST_BYTES_1.length);

		ByteString bs1 = ByteString.copy(mutableBytes1);

		Assertions.assertEquals(TEST_BYTES_1.length, bs1.length());

		for (int byteIndex = 0; byteIndex < TEST_BYTES_1.length; byteIndex++) {
			Assertions.assertEquals(TEST_BYTES_1[byteIndex], bs1.byteAt(byteIndex));
		}

		mutableBytes1[1] = (byte) 0x02;

		Assertions.assertEquals(TEST_BYTES_1[1], bs1.byteAt(1));
	}

	@Test
	void testCopyTo() {
		ByteString bs1 = ByteString.wrap(TEST_BYTES_1);
		byte[] bs1Copy = new byte[TEST_BYTES_1.length];

		bs1.copyTo(bs1Copy, 0);

		Assertions.assertArrayEquals(TEST_BYTES_1, bs1Copy);
	}

	@Test
	void testWriteTo() throws IOException {
		ByteString bs1 = ByteString.wrap(TEST_BYTES_1);

		try (ByteArrayOutputStream bs1Copy = new ByteArrayOutputStream()) {
			bs1.write(bs1Copy);

			Assertions.assertArrayEquals(TEST_BYTES_1, bs1Copy.toByteArray());
		}
	}

	@Test
	void testSlicing() {
		ByteString bs1 = ByteString.wrap(TEST_BYTES_1);
		ByteString bs1Total = bs1.slice(0, TEST_BYTES_1.length);
		ByteString bs1Begin = bs1.slice(0, 1);
		ByteString bs1End = bs1.slice(1, TEST_BYTES_1.length - 1);

		Assertions.assertSame(bs1, bs1Total);

		Assertions.assertEquals(1, bs1Begin.length());
		Assertions.assertEquals(TEST_BYTES_1.length - 1, bs1End.length());

		Assertions.assertEquals(TEST_BYTES_1[0], bs1Begin.byteAt(0));
		Assertions.assertEquals(TEST_BYTES_1[1], bs1End.byteAt(0));
	}

	@Test
	void testCompare() {
		ByteString bs0 = ByteString.wrap(TEST_BYTES_0);
		ByteString bs1 = ByteString.wrap(TEST_BYTES_1);
		ByteString bs2 = ByteString.wrap(TEST_BYTES_2);
		ByteString bs3 = ByteString.wrap(TEST_BYTES_3);

		Assertions.assertEquals(0, bs0.compareTo(bs0));
		Assertions.assertEquals(0, bs1.compareTo(bs1));
		Assertions.assertEquals(0, bs2.compareTo(bs2));
		Assertions.assertEquals(0, bs3.compareTo(bs3));

		Assertions.assertTrue(bs0.compareTo(bs1) < 0);
		Assertions.assertTrue(bs1.compareTo(bs2) < 0);
		Assertions.assertTrue(bs2.compareTo(bs3) < 0);

		Assertions.assertTrue(bs1.compareTo(bs0) > 0);
		Assertions.assertTrue(bs2.compareTo(bs1) > 0);
		Assertions.assertTrue(bs3.compareTo(bs2) > 0);
	}

	@Test
	void testEquals() {
		ByteString bs0 = ByteString.wrap(TEST_BYTES_0);
		ByteString bs1 = ByteString.wrap(TEST_BYTES_1);
		ByteString bs2 = ByteString.wrap(TEST_BYTES_2);
		ByteString bs3 = ByteString.wrap(TEST_BYTES_3);
		ByteString bs3a = ByteString.copy(TEST_BYTES_3);
		ByteString bs3b = ByteString.copy(TEST_BYTES_3);

		Assertions.assertEquals(bs0, bs0);
		Assertions.assertEquals(bs1, bs1);
		Assertions.assertEquals(bs2, bs2);
		Assertions.assertEquals(bs3, bs3);
		Assertions.assertEquals(bs3a, bs3b);
		Assertions.assertEquals(bs3a.hashCode(), bs3b.hashCode());

		Assertions.assertNotEquals(bs0, bs1);
		Assertions.assertNotEquals(bs1, bs2);
		Assertions.assertNotEquals(bs2, bs3);
	}

	@Test
	void testToString() {
		ByteString bs2 = ByteString.wrap(TEST_BYTES_2);
		ByteString bs3 = ByteString.wrap(TEST_BYTES_3);

		Assertions.assertEquals("01 01 7f", bs2.toString());
		Assertions.assertEquals("01 01 7f 80 81 82 83 84 85 86 87 88 89 8a 8b fe\u2026", bs3.toString());
	}

}
