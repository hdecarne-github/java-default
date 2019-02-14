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
package de.carne.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class providing file related functions.
 */
public final class FileUtil {

	private FileUtil() {
		// prevent instantiation
	}

	/**
	 * Gets the temporary directory path.
	 *
	 * @return the temporary directory path.
	 */
	public static Path tmpDir() {
		return Paths.get(Objects.requireNonNull(System.getProperty("java.io.tmpdir"))).toAbsolutePath();
	}

	/**
	 * Gets the current working directory path.
	 *
	 * @return the current working directory path.
	 */
	public static Path workingDir() {
		return Paths.get(".").toAbsolutePath();
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

	/**
	 * Splits a path into it's three parts directory, name and extension.
	 * <p>
	 * For missing parts the empty string is returned.
	 * </p>
	 *
	 * @param path the path to split.
	 * @return the three path parts.
	 */
	public static String[] splitPath(String path) {
		String[] split = new String[] { "", "", "" };
		int baseIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
		String fileName;

		if (baseIndex > 0) {
			split[0] = path.substring(0, baseIndex);
			fileName = path.substring(baseIndex + 1);
		} else if (baseIndex == 0) {
			split[0] = path.substring(0, baseIndex + 1);
			fileName = path.substring(baseIndex + 1);
		} else {
			fileName = path;
		}

		int extIndex = fileName.lastIndexOf('.');

		if (extIndex > 0) {
			split[1] = fileName.substring(0, extIndex);
			split[2] = fileName.substring(extIndex + 1);
		} else {
			split[1] = fileName;
		}
		return split;
	}

}
