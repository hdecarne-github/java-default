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
package de.carne.test.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.io.Checksum;
import de.carne.io.ChecksumInputStream;
import de.carne.io.ChecksumOutputStream;
import de.carne.io.IOUtil;
import de.carne.io.MD5Checksum;
import de.carne.io.NullOutputStream;
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
	private static final String TEST_DATA_MD5 = "e2c865db4162bed963bfaa9ef6ac18f0";

	@Test
	void testSHA256Checksum() throws Exception {
		Checksum sha256 = SHA256Checksum.getInstance();

		testChecksumBulked(sha256, TEST_DATA_SHA256);
		sha256.reset();
		testChecksumChunked(sha256, TEST_DATA_SHA256);
		sha256.reset();
		testChecksumInputStream(sha256, TEST_DATA_SHA256);
		sha256.reset();
		testChecksumOutputStream(sha256, TEST_DATA_SHA256);
	}

	@Test
	void testMD5Checksum() throws Exception {
		Checksum md5 = MD5Checksum.getInstance();

		testChecksumBulked(md5, TEST_DATA_MD5);
		md5.reset();
		testChecksumChunked(md5, TEST_DATA_MD5);
		md5.reset();
		testChecksumInputStream(md5, TEST_DATA_MD5);
		md5.reset();
		testChecksumOutputStream(md5, TEST_DATA_MD5);
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

	private void testChecksumInputStream(Checksum checksum, String expected) throws IOException {
		String actual;

		try (ChecksumInputStream in = new ChecksumInputStream(new ByteArrayInputStream(TEST_DATA), checksum);
				OutputStream out = new NullOutputStream()) {
			IOUtil.copyStream(out, in);
			actual = HexBytes.toStringL(in.getChecksumValue());
		}
		Assertions.assertEquals(expected, actual);
	}

	private void testChecksumOutputStream(Checksum checksum, String expected) throws IOException {
		String actual;

		try (InputStream in = new ByteArrayInputStream(TEST_DATA);
				ChecksumOutputStream out = new ChecksumOutputStream(new NullOutputStream(), checksum)) {
			IOUtil.copyStream(out, in);
			actual = HexBytes.toStringL(out.getChecksumValue());
		}
		Assertions.assertEquals(expected, actual);
	}

}
