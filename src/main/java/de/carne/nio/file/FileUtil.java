/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import de.carne.check.Nullable;

/**
 * Utility class providing file related functions.
 */
public final class FileUtil {

	private FileUtil() {
		// prevent instantiation
	}

	/**
	 * Sets the submitted file last modified time to the current time.
	 * <p>
	 * If the file does not yet exist, it will be created.
	 *
	 * @param file the file to touch.
	 * @throws IOException if an I/O error occurs.
	 */
	@SuppressWarnings("squid:S3725")
	public static void touch(Path file) throws IOException {
		if (Files.exists(file)) {
			Files.setLastModifiedTime(file, FileTime.from(Instant.now()));
		} else {
			Files.createFile(file);
		}
	}

	/**
	 * Deletes a file or directory (including any included file).
	 *
	 * @param fileOrDirectory the file or director to delete.
	 * @return {@code true} if the file or directory was deleted. {@code false} if the file or directory does not exist.
	 * @throws IOException if an I/O error occurs during deletion.
	 */
	@SuppressWarnings("squid:S3725")
	public static boolean delete(Path fileOrDirectory) throws IOException {
		boolean deleted = false;

		if (Files.exists(fileOrDirectory, LinkOption.NOFOLLOW_LINKS)) {
			deleted = true;
			if (Files.isDirectory(fileOrDirectory, LinkOption.NOFOLLOW_LINKS)) {
				Files.walkFileTree(fileOrDirectory, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(@Nullable Path file, @Nullable BasicFileAttributes attrs)
							throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(@Nullable Path dir, @Nullable IOException exc)
							throws IOException {
						if (exc != null) {
							throw exc;
						}
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} else {
				Files.delete(fileOrDirectory);
			}
		}
		return deleted;
	}

}