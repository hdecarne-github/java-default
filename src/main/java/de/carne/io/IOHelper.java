/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import de.carne.util.PropertiesHelper;

/**
 * Utility class providing I/O related functions.
 */
public final class IOHelper {

	private static final int DEFAULT_BUFFER_SIZE = PropertiesHelper.getInt(IOHelper.class, ".bufferSize", 4096);

	private IOHelper() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Read all bytes from an {@link InputStream}.
	 *
	 * @param in The input stream to read from.
	 * @return The read bytes.
	 * @throws IOException if an I/O error occurs while reading data.
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		return readBytes(in, Integer.MAX_VALUE);
	}

	/**
	 * Read up to a maximum number of bytes from an {@link InputStream}.
	 *
	 * @param in The input stream to read from.
	 * @param limit The maximum number of bytes to read.
	 * @return The read bytes.
	 * @throws InterruptedIOException if the read limit has been reached.
	 * @throws IOException if an I/O error occurs while reading data.
	 */
	public static byte[] readBytes(InputStream in, int limit) throws IOException {
		assert in != null;

		byte[] bytes;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			copyStream(in, out);
			bytes = out.toByteArray();
		}
		return bytes;
	}

	/**
	 * Copy all data from an {@link InputStream} to an {@link OutputStream}.
	 *
	 * @param in The input stream to read from.
	 * @param out The output stream to write to.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs while reading or writing data.
	 */
	public static long copyStream(InputStream in, OutputStream out) throws IOException {
		assert in != null;
		assert out != null;

		int read;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long copied = 0;

		while ((read = in.read(buffer)) > 0) {
			out.write(buffer, 0, read);
			copied += read;
		}
		return copied;
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory
	 *         tree.
	 */
	public static void deleteDirectoryTree(String directory) throws IOException {
		assert directory != null;

		deleteDirectoryTree(Paths.get(directory));
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory
	 *         tree.
	 */
	public static void deleteDirectoryTree(File directory) throws IOException {
		assert directory != null;

		deleteDirectoryTree(directory.toPath());
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory
	 *         tree.
	 */
	public static void deleteDirectoryTree(Path directory) throws IOException {
		assert directory != null;

		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (exc == null) {
					Files.delete(dir);
				}
				return super.postVisitDirectory(dir, exc);
			}

		});
	}

}
