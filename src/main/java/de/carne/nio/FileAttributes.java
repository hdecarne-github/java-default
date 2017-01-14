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
package de.carne.nio;

import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import de.carne.check.Check;
import de.carne.check.NonNullByDefault;

/**
 * Utility class for {@link FileAttribute} handling.
 */
@NonNullByDefault
public final class FileAttributes {

	private FileAttributes() {
		// Make sure this class is not instantiated from outside
	}

	private static final String POSIX = "posix";

	/**
	 * Get a proper default {@link FileAttribute} set for a user directory.
	 * <p>
	 * Based upon the corresponding file system's capabilities this function determines the best suiting file attributes
	 * for only letting the current user access the directory.
	 *
	 * @param directoryPath The directory path to get the default file attributes for.
	 * @return The determined default file attributes.
	 */
	public static FileAttribute<?>[] defaultUserDirectoryAttributes(Path directoryPath) {
		assert directoryPath != null;

		List<FileAttribute<?>> attributes = new ArrayList<>();
		Set<String> supported = directoryPath.getFileSystem().supportedFileAttributeViews();

		if (supported.contains(POSIX)) {
			attributes.add(PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ,
					PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE)));
		}
		return Check.nonNullS(attributes.toArray(new FileAttribute<?>[attributes.size()]));
	}

	/**
	 * Get a proper default {@link FileAttribute} set for a user file.
	 * <p>
	 * Based upon the corresponding file system's capabilities this function determines the best suiting file attributes
	 * for only letting the current user access the file.
	 *
	 * @param filePath The file path to get the default file attributes for.
	 * @return The determined default file attributes.
	 */
	public static FileAttribute<?>[] defaultUserFileAttributes(Path filePath) {
		assert filePath != null;

		List<FileAttribute<?>> attributes = new ArrayList<>();
		Set<String> supported = filePath.getFileSystem().supportedFileAttributeViews();

		if (supported.contains(POSIX)) {
			attributes.add(PosixFilePermissions
					.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE)));
		}
		return Check.nonNullA(attributes.toArray(new FileAttribute<?>[attributes.size()]));
	}

}
