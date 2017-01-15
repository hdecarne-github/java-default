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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Predicate;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.check.Check;
import de.carne.check.NonNullByDefault;
import de.carne.check.Nullable;
import de.carne.io.IOHelper;

/**
 * Test {@link IOHelper} class.
 */
@NonNullByDefault
public class IOHelperTest {

	private static final String TEST_DIR_ZIP = "test.zip";

	@Nullable
	private static Path testDir = null;

	/**
	 * Test environment.
	 *
	 * @throws IOException if setup fails.
	 */
	@BeforeClass
	public static void createTestFiles() throws IOException {
		testDir = IOHelper.createTempDirFromZIPResource(Check.nonNullS(IOHelperTest.class.getResource(TEST_DIR_ZIP)),
				Check.nonNullS(IOHelperTest.class.getSimpleName()));
	}

	/**
	 * Cleanup test environment.
	 *
	 * @throws IOException if cleanup fails.
	 */
	@AfterClass
	public static void deleteTestFiles() throws IOException {
		if (testDir != null) {
			IOHelper.deleteDirectoryTree(testDir);
			testDir = null;
		}
	}

	/**
	 * Test directory file collecting.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testCollectDirectoryFiles() throws IOException {
		Path start = Check.nonNullS(testDir);

		Assert.assertEquals(5, IOHelper.collectDirectoryFiles(start).size());

		Predicate<Path> filter = (p) -> "A.txt|B.txt|Z.txt".contains(p.getFileName().toString());

		Assert.assertEquals(2, IOHelper.collectDirectoryFiles(start, filter).size());
	}

	/**
	 * Test directory deletion.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testDeleteDirectoryTree() throws IOException {
		Path directoryPath = IOHelper.createTempDirFromZIPResource(Check.nonNullS(getClass().getResource(TEST_DIR_ZIP)),
				null);

		IOHelper.deleteDirectoryTree(directoryPath);

		File directoryFile = Check.nonNullS(IOHelper
				.createTempDirFromZIPResource(Check.nonNullS(getClass().getResource(TEST_DIR_ZIP)), null).toFile());

		IOHelper.deleteDirectoryTree(directoryFile);

		String directoryString = Check.nonNullS(IOHelper
				.createTempDirFromZIPResource(Check.nonNullS(getClass().getResource(TEST_DIR_ZIP)), null).toString());

		IOHelper.deleteDirectoryTree(directoryString);
	}

	/**
	 * Test {@link IOHelper#copyStream(InputStream, OutputStream)} function.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testCopyStream() throws IOException {
		Path inPath = IOHelper.createTempFileFromResource(getClassResource(), null, null);
		Path outPath = Files.createTempFile(null, null);
		try {
			try (InputStream in = Files.newInputStream(inPath);
					OutputStream out = Files.newOutputStream(outPath, StandardOpenOption.CREATE)) {
				assert in != null;
				assert out != null;

				IOHelper.copyStream(in, out);
			}
			try (InputStream expectedStream = Files.newInputStream(inPath);
					InputStream actualStream = Files.newInputStream(outPath)) {
				assert expectedStream != null;
				assert actualStream != null;

				byte[] expected = IOHelper.readBytes(expectedStream);
				byte[] actual = IOHelper.readBytes(actualStream);

				Assert.assertArrayEquals(expected, actual);
			}
		} finally {
			Files.delete(inPath);
			Files.delete(outPath);
		}
	}

	/**
	 * Test {@link IOHelper#readBytes(InputStream)} function.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testReadBytesAll() throws IOException {
		try (InputStream in = getClassResource().openStream()) {
			assert in != null;

			byte[] bytes = IOHelper.readBytes(in);

			Assert.assertTrue(bytes.length > 1);
		}
	}

	/**
	 * Test {@link IOHelper#readBytes(InputStream, int)} function.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = InterruptedIOException.class)
	public void testReadBytesLimit() throws IOException {
		try (InputStream in = getClassResource().openStream()) {
			assert in != null;

			IOHelper.readBytes(in, 1);

			Assert.fail();
		}
	}

	private URL getClassResource() {
		return Check.nonNullS(getClass().getResource(getClass().getSimpleName() + ".class"));
	}

}
