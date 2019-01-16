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
package de.carne.test.nio.file.attribute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.nio.file.FileUtil;
import de.carne.nio.file.attribute.FileAttributes;

/**
 * Test {@linkplain FileAttributes} class.
 */
class FileAttributesTest {

	private static final Map<String, Object[]> USER_DIRECTORY_PERMISSIONS = new HashMap<>();

	static {
		USER_DIRECTORY_PERMISSIONS.put("posix", new Object[] { PosixFilePermission.OWNER_READ,
				PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE });
	}

	private static final Map<String, Object[]> USER_FILE_PERMISSIONS = new HashMap<>();

	static {
		USER_FILE_PERMISSIONS.put("posix",
				new Object[] { PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE });
	}

	@Test
	void testUserDirectoryDefault() throws IOException {
		Path userDirectory = Files.createTempDirectory(FileUtil.tmpDir(), getClass().getName(),
				FileAttributes.userDirectoryDefault(FileUtil.tmpDir()));

		try {
			checkFilePermissions(userDirectory, USER_DIRECTORY_PERMISSIONS);
		} finally {
			Files.delete(userDirectory);
		}
	}

	@Test
	void testUserFileDefault() throws IOException {
		Path userFile = Files.createTempFile(FileUtil.tmpDir(), getClass().getName(), null,
				FileAttributes.userFileDefault(FileUtil.tmpDir()));

		try {
			checkFilePermissions(userFile, USER_FILE_PERMISSIONS);
		} finally {
			Files.delete(userFile);
		}
	}

	private void checkFilePermissions(Path path, Map<String, Object[]> expectedPermissionsMap) throws IOException {
		Set<String> fileAttributeViews = path.getFileSystem().supportedFileAttributeViews();

		for (String fileAttributeView : fileAttributeViews) {
			if ("posix".equals(fileAttributeView)) {
				PosixFileAttributeView attributeView = Objects
						.requireNonNull(Files.getFileAttributeView(path, PosixFileAttributeView.class));
				Set<PosixFilePermission> actualPermissions = attributeView.readAttributes().permissions();
				Object[] expectedPermissions = Objects.requireNonNull(expectedPermissionsMap.get(fileAttributeView));

				Assertions.assertEquals(expectedPermissions.length, actualPermissions.size());
				for (Object expectedPermission : expectedPermissions) {
					Assertions.assertTrue(actualPermissions.contains(expectedPermission),
							"Missing: " + expectedPermission);
				}
			}
		}
	}

}
