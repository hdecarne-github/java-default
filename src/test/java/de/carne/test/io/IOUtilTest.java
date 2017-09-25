/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

import de.carne.io.IOUtil;

/**
 * Test {@link IOUtil} class.
 */
public class IOUtilTest {

	/**
	 * Test {@link IOUtil} copy functions.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testCopy() throws IOException {
		File file = Files.createTempFile(getClass().getName(), ".tmp").toFile();

		try {
			ByteArrayOutputStream resourceDataOutputStream = new ByteArrayOutputStream();

			IOUtil.copyUrl(resourceDataOutputStream, getClass().getResource("data.bin"));

			byte[] resourceData = resourceDataOutputStream.toByteArray();

			ByteArrayInputStream resourceDataInputStream = new ByteArrayInputStream(resourceData);

			IOUtil.copyStream(file, resourceDataInputStream);

			ByteArrayOutputStream fileDataOutputStream = new ByteArrayOutputStream();

			IOUtil.copyFile(fileDataOutputStream, file);

			byte[] fileData = fileDataOutputStream.toByteArray();

			Assert.assertArrayEquals(resourceData, fileData);
		} finally {
			file.delete();
		}
	}

}
