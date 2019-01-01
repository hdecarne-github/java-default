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
package de.carne.nio.file.attribute;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class providing {@linkplain FileAttribute} related functions.
 */
public final class FileAttributes {

	private FileAttributes() {
		// Prevent instantiation
	}

	private static final String POSIX = "posix";

	/**
	 * Gets the {@linkplain FileSystem} specific best choice for the access rights for a user's private directory.
	 *
	 * @param fileSystem the {@linkplain FileSystem} to determine the access rights for.
	 * @return the access rights to use for a user's private directory.
	 */
	@SuppressWarnings("squid:S1452")
	public static FileAttribute<?>[] userDirectoryDefault(FileSystem fileSystem) {
		List<FileAttribute<?>> userDefault = new ArrayList<>();
		Set<String> fileAttributeViews = fileSystem.supportedFileAttributeViews();

		if (fileAttributeViews.contains(POSIX)) {
			userDefault.add(PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ,
					PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE)));
		}
		return userDefault.toArray(new @Nullable FileAttribute<?>[userDefault.size()]);
	}

	/**
	 * Gets the {@linkplain FileSystem} specific best choice for the access rights for a user's private directory.
	 *
	 * @param path the {@linkplain Path} to determine the access rights for.
	 * @return the access rights to use for a user's private directory.
	 */
	@SuppressWarnings("squid:S1452")
	public static FileAttribute<?>[] userDirectoryDefault(Path path) {
		return userDirectoryDefault(path.getFileSystem());
	}

	/**
	 * Gets the {@linkplain FileSystem} specific best choice for the access rights for a user's private file.
	 *
	 * @param fileSystem the {@linkplain FileSystem} to determine the access rights for.
	 * @return the access rights to use for a user's private file.
	 */
	@SuppressWarnings("squid:S1452")
	public static FileAttribute<?>[] userFileDefault(FileSystem fileSystem) {
		List<FileAttribute<?>> userDefault = new ArrayList<>();
		Set<String> fileAttributeViews = fileSystem.supportedFileAttributeViews();

		if (fileAttributeViews.contains(POSIX)) {
			userDefault.add(PosixFilePermissions
					.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE)));
		}
		return userDefault.toArray(new @Nullable FileAttribute<?>[userDefault.size()]);
	}

	/**
	 * Gets the {@linkplain FileSystem} specific best choice for the access rights for a user's private file.
	 *
	 * @param path the {@linkplain Path} to determine the access rights for.
	 * @return the access rights to use for a user's private file.
	 */
	@SuppressWarnings("squid:S1452")
	public static FileAttribute<?>[] userFileDefault(Path path) {
		return userFileDefault(path.getFileSystem());
	}

}
