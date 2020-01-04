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
package de.carne.test.io;

import java.security.GeneralSecurityException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.io.Checksum;
import de.carne.io.SHA256Checksum;
import de.carne.text.HexBytes;

/**
 * Test {@linkplain Checksum} implementations.
 */
class ChecksumTest {

	private static final byte[] TEST_DATA = new byte[256];

	static {
		for (int testDataIndex = 0; testDataIndex < TEST_DATA.length; testDataIndex++) {
			TEST_DATA[testDataIndex] = (byte) (testDataIndex & 0xff);
		}
	}

	private static final String TEST_DATA_SHA256 = "40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880";

	@Test
	void testSHA256Checksum() throws GeneralSecurityException {
		Checksum sha256 = SHA256Checksum.getInstance();

		testChecksumBulked(sha256, TEST_DATA_SHA256);
		testChecksumChunked(sha256, TEST_DATA_SHA256);
	}

	private void testChecksumBulked(Checksum checksum, String expected) {
		checksum.reset();
		checksum.update(TEST_DATA);

		String actual = HexBytes.toStringL(checksum.getValue());

		Assertions.assertEquals(expected, actual);
	}

	private void testChecksumChunked(Checksum checksum, String expected) {
		checksum.reset();

		for (byte testDataByte : TEST_DATA) {
			checksum.update(testDataByte);
		}

		String actual = HexBytes.toStringL(checksum.getValue());

		Assertions.assertEquals(expected, actual);
	}

}
