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
package de.carne.test.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import org.junit.Assert;
import org.junit.Test;

import de.carne.check.NonNullByDefault;
import de.carne.nio.FileAttributes;

/**
 * Test {@link FileAttributes} class.
 */
@NonNullByDefault
public class FileAttributesTest {

	/**
	 * Test {@link FileAttributes#defaultUserDirectoryAttributes(Path)} function.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testDefaultUserDirectoryAttributes() throws IOException {
		Path userDirectory = Files.createTempDirectory(getClass().getSimpleName());

		try {
			FileAttribute<?>[] attributes = FileAttributes.defaultUserDirectoryAttributes(userDirectory);

			Assert.assertNotNull(attributes);
		} finally {
			Files.delete(userDirectory);
		}
	}

	/**
	 * Test {@link FileAttributes#defaultUserFileAttributes(Path)} function.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testDefaultUserFileAttributes() throws IOException {
		Path userFile = Files.createTempDirectory(getClass().getSimpleName());

		try {
			FileAttribute<?>[] attributes = FileAttributes.defaultUserFileAttributes(userFile);

			Assert.assertNotNull(attributes);
		} finally {
			Files.delete(userFile);
		}
	}

}
