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
package de.carne.test.nio.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.nio.file.FileUtil;
import de.carne.util.Threads;

/**
 * Test {@linkplain FileUtil} class.
 */
class FileUtilTest {

	@Test
	void testTmpDir() {
		Path tmpDir = FileUtil.tmpDir();

		Assertions.assertTrue(Files.isDirectory(tmpDir));
		Assertions.assertEquals(tmpDir.toAbsolutePath(), tmpDir);
	}

	@Test
	void testWorkingDir() {
		Path workingDir = FileUtil.workingDir();

		Assertions.assertTrue(Files.isDirectory(workingDir));
		Assertions.assertEquals(workingDir.toAbsolutePath(), workingDir);
	}

	@Test
	void testTouch() throws IOException {
		Path tempFile = Files.createTempFile(getClass().getName(), null);

		FileUtil.touch(tempFile);

		Assertions.assertTrue(Files.exists(tempFile));

		FileTime mtimeOld = Files.getLastModifiedTime(tempFile);

		Assertions.assertTrue(mtimeOld.compareTo(FileTime.from(Instant.now())) <= 0);

		Threads.sleep(1000);

		Files.delete(tempFile);
		FileUtil.touch(tempFile);

		Assertions.assertTrue(Files.exists(tempFile));

		FileTime mtimeNew = Files.getLastModifiedTime(tempFile);

		Assertions.assertTrue(mtimeOld.compareTo(mtimeNew) < 0);

		Files.delete(tempFile);
	}

	@Test
	void testDelete() throws IOException {
		Path tempDir = Files.createTempDirectory(getClass().getName());

		for (int tempFileIndex = 0; tempFileIndex < 10; tempFileIndex++) {
			FileUtil.touch(tempDir.resolve("test" + tempFileIndex + ".tmp"));
		}

		Assertions.assertTrue(FileUtil.delete(tempDir));
		Assertions.assertFalse(Files.exists(tempDir));
		Assertions.assertFalse(FileUtil.delete(tempDir));

		Path tempFile = Files.createTempFile(getClass().getName(), null);

		Assertions.assertTrue(FileUtil.delete(tempFile));
		Assertions.assertFalse(Files.exists(tempFile));
		Assertions.assertFalse(FileUtil.delete(tempFile));
	}

}
