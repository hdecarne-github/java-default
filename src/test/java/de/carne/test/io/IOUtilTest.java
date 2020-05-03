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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.carne.io.IOUtil;
import de.carne.test.annotation.io.TempFile;
import de.carne.test.extension.io.TempPathExtension;

/**
 * Test {@linkplain IOUtil} class.
 */
@ExtendWith(TempPathExtension.class)
class IOUtilTest {

	@Test
	void testCopy(@TempFile File file1, @TempFile File file2) throws IOException {
		// Test stream/file copy operations
		ByteArrayOutputStream resourceDataOutputStream = new ByteArrayOutputStream();

		IOUtil.copyUrl(resourceDataOutputStream, Objects.requireNonNull(getClass().getResource("data.bin")));

		byte[] resourceData = resourceDataOutputStream.toByteArray();
		ByteArrayInputStream resourceDataInputStream = new ByteArrayInputStream(resourceData);

		IOUtil.copyStream(file1, resourceDataInputStream);
		IOUtil.copyFile(file2, file1);

		ByteArrayOutputStream fileDataOutputStream = new ByteArrayOutputStream();

		IOUtil.copyFile(fileDataOutputStream, file2);

		Assertions.assertArrayEquals(resourceData, fileDataOutputStream.toByteArray());

		// Test channel copy operations
		try (FileChannel file1Channel = FileChannel.open(file1.toPath(), StandardOpenOption.READ);
				FileChannel file2Channel = FileChannel.open(file2.toPath(), StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING)) {
			IOUtil.copyChannel(file2Channel, file1Channel);
		}

		fileDataOutputStream.reset();

		IOUtil.copyFile(fileDataOutputStream, file2);

		Assertions.assertArrayEquals(resourceData, fileDataOutputStream.toByteArray());

		// Test buffer copy operations
		byte[] bufferBytes = new byte[Long.BYTES];
		ByteBuffer heapBuffer = ByteBuffer.wrap(bufferBytes);
		ByteBuffer directBuffer = ByteBuffer.allocateDirect(Long.BYTES);

		heapBuffer.putLong(0x123456789abcdef0l);
		heapBuffer.flip();
		directBuffer.putLong(0x123456789abcdef0l);
		directBuffer.flip();

		fileDataOutputStream.reset();

		IOUtil.copyBuffer(fileDataOutputStream, directBuffer);

		Assertions.assertFalse(directBuffer.hasRemaining());
		Assertions.assertArrayEquals(bufferBytes, fileDataOutputStream.toByteArray());

		fileDataOutputStream.reset();

		IOUtil.copyBuffer(fileDataOutputStream, heapBuffer);

		Assertions.assertFalse(heapBuffer.hasRemaining());
		Assertions.assertArrayEquals(bufferBytes, fileDataOutputStream.toByteArray());
	}

	@Test
	void testReadAllBytes(@TempFile File file) throws IOException {
		// Prepare file
		URL url = Objects.requireNonNull(getClass().getResource("data.bin"));

		IOUtil.copyUrl(file, url);

		ByteArrayOutputStream fileDataOutputStream = new ByteArrayOutputStream();

		IOUtil.copyUrl(fileDataOutputStream, url);

		byte[] bytes = fileDataOutputStream.toByteArray();

		// Test read operations
		Assertions.assertArrayEquals(bytes, IOUtil.readAllBytes(file));
		Assertions.assertArrayEquals(bytes, IOUtil.readAllBytes(file, bytes.length));
		Assertions.assertArrayEquals(bytes, IOUtil.readAllBytes(url, bytes.length));
		Assertions.assertThrows(IOException.class, () -> {
			IOUtil.readAllBytes(file, bytes.length - 1);
		});
		Assertions.assertThrows(IOException.class, () -> {
			IOUtil.readAllBytes(url, bytes.length - 1);
		});
	}

	@Test
	void testReadBlocking(@TempFile File file) throws IOException {
		// Prepare file
		URL url = Objects.requireNonNull(getClass().getResource("data.bin"));

		IOUtil.copyUrl(file, url);

		ByteArrayOutputStream fileDataOutputStream = new ByteArrayOutputStream();

		IOUtil.copyUrl(fileDataOutputStream, url);

		byte[] buffer = new byte[fileDataOutputStream.size() + 1];
		int read;

		try (InputStream in = new FileInputStream(file)) {
			read = IOUtil.readBlocking(in, buffer);
		}

		Assertions.assertEquals(buffer.length - 1, read);

		byte[] bufferSlice = new byte[fileDataOutputStream.size()];

		System.arraycopy(buffer, 0, bufferSlice, 0, read);
		Assertions.assertArrayEquals(fileDataOutputStream.toByteArray(), bufferSlice);
	}

	@Test
	void testReadEager(@TempFile File file) throws IOException {
		// Prepare file
		URL url = Objects.requireNonNull(getClass().getResource("data.bin"));

		IOUtil.copyUrl(file, url);

		ByteArrayOutputStream fileDataOutputStream = new ByteArrayOutputStream();

		IOUtil.copyUrl(fileDataOutputStream, url);

		byte[] buffer = new byte[fileDataOutputStream.size()];

		try (InputStream in = new FileInputStream(file)) {
			IOUtil.readEager(in, buffer);
		}
		Assertions.assertArrayEquals(fileDataOutputStream.toByteArray(), buffer);
	}

}
