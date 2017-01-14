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
package de.carne.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.carne.check.Check;
import de.carne.check.NonNullByDefault;
import de.carne.check.Nullable;
import de.carne.util.PropertiesHelper;

/**
 * Utility class providing I/O related functions.
 */
@NonNullByDefault
public final class IOHelper {

	private IOHelper() {
		// Make sure this class is not instantiated from outside
	}

	private static final int DEFAULT_BUFFER_SIZE = PropertiesHelper.getInt(IOHelper.class, ".bufferSize", 4096);

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
		byte[] bytes;

		try (LimitOutputStream<ByteArrayOutputStream> out = new LimitOutputStream<>(new ByteArrayOutputStream(),
				limit)) {
			copyStream(in, out);
			bytes = Check.nonNullS(out.outputStream().toByteArray());
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
	 * Collect all files in a directory.
	 *
	 * @param start The directory path to scan.
	 * @param options The scan options.
	 * @return The collected file paths.
	 * @throws IOException if an I/O error occurs.
	 */
	public static List<Path> collectDirectoryFiles(Path start, FileVisitOption... options) throws IOException {
		return collectDirectoryFiles(start, (p) -> Files.isRegularFile(p), options);
	}

	/**
	 * Collect all files in a directory.
	 *
	 * @param start The directory path to scan.
	 * @param filter The filter to apply to the scan result.
	 * @param options The scan options.
	 * @return The collected file paths.
	 * @throws IOException if an I/O error occurs.
	 */
	public static List<Path> collectDirectoryFiles(Path start, Predicate<Path> filter, FileVisitOption... options)
			throws IOException {
		List<Path> collection;

		try (Stream<Path> stream = Files.walk(start, options)) {
			collection = Check.nonNullS(stream.filter(filter).collect(Collectors.toList()));
		}
		return collection;
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory tree.
	 */
	public static void deleteDirectoryTree(String directory) throws IOException {
		deleteDirectoryTree(Check.nonNullS(Paths.get(directory)));
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory tree.
	 */
	public static void deleteDirectoryTree(File directory) throws IOException {
		deleteDirectoryTree(Check.nonNullS(directory.toPath()));
	}

	/**
	 * Delete a directory and all it's included sub-directories/files.
	 *
	 * @param directory The directory to delete.
	 * @throws IOException if an I/O error occurs while deleting the directory tree.
	 */
	public static void deleteDirectoryTree(Path directory) throws IOException {
		assert directory != null;

		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

			@Override
			@Nullable
			public FileVisitResult visitFile(@Nullable Path file, @Nullable BasicFileAttributes attrs)
					throws IOException {
				Files.delete(file);
				return super.visitFile(file, attrs);
			}

			@Override
			@Nullable
			public FileVisitResult postVisitDirectory(@Nullable Path dir, @Nullable IOException exc)
					throws IOException {
				if (exc == null) {
					Files.delete(dir);
				}
				return super.postVisitDirectory(dir, exc);
			}

		});
	}

	/**
	 * Create a temporary file and initialize it by copying a resource's content.
	 *
	 * @param resource The resource to use for file initialization.
	 * @param prefix The prefix to use for temp file creation (may be {@code null}).
	 * @param suffix The suffix to use for temp file creation (may be {@code null}).
	 * @param attrs The file attributes to use for temp file creation.
	 * @return The created and initialized file.
	 * @throws IOException if an I/O error occurs.
	 * @see Files#createTempFile(String, String, FileAttribute...)
	 */
	public static Path createTempFileFromResource(URL resource, String prefix, String suffix, FileAttribute<?>... attrs)
			throws IOException {
		return copyResourceToFile(resource, Check.nonNullS(Files.createTempFile(prefix, suffix, attrs)));
	}

	/**
	 * Create a temporary file and initialize it by copying a resource's content.
	 *
	 * @param resource The resource to use for file initialization.
	 * @param dir The directory to use for temp file creation.
	 * @param prefix The prefix to use for temp file creation (may be {@code null}).
	 * @param suffix The suffix to use for temp file creation (may be {@code null}).
	 * @param attrs The file attributes to use for temp file creation.
	 * @return The created and initialized file.
	 * @throws IOException if an I/O error occurs.
	 * @see Files#createTempFile(Path, String, String, FileAttribute...)
	 */
	public static Path createTempFileFromResource(URL resource, Path dir, String prefix, String suffix,
			FileAttribute<?>... attrs) throws IOException {
		return copyResourceToFile(resource, Check.nonNullS(Files.createTempFile(dir, prefix, suffix, attrs)));
	}

	private static Path copyResourceToFile(URL resource, Path file) throws IOException {
		try (InputStream in = Check.nonNullS(resource.openStream());
				OutputStream out = Check.nonNullS(Files.newOutputStream(file, StandardOpenOption.CREATE_NEW))) {
			copyStream(in, out);
		}
		return file;
	}

	/**
	 * Create a temporary directory and initialize it by copying a ZIP resource's content.
	 *
	 * @param resource The resource to use for directory initialization.
	 * @param prefix The prefix to use for temp dir creation (may be {@code null}).
	 * @param attrs The file attributes to use for temp dir creation.
	 * @return The created and initialized directory.
	 * @throws IOException if an I/O error occurs.
	 * @see Files#createTempDirectory(String, FileAttribute...)
	 */
	public static Path createTempDirFromZIPResource(URL resource, String prefix, FileAttribute<?>... attrs)
			throws IOException {
		return exportZIPResourceToDirectory(resource, Check.nonNullS(Files.createTempDirectory(prefix, attrs)));
	}

	/**
	 * Create a temporary directory and initialize it by copying a ZIP resource's content.
	 *
	 * @param resource The resource to use for directory initialization.
	 * @param dir The directory to use for temp dir creation.
	 * @param prefix The prefix to use for temp dir creation (may be {@code null}).
	 * @param attrs The file attributes to use for temp dir creation.
	 * @return The created and initialized directory.
	 * @throws IOException if an I/O error occurs.
	 * @see Files#createTempDirectory(Path, String, FileAttribute...)
	 */
	public static Path createTempDirFromZIPResource(URL resource, Path dir, String prefix, FileAttribute<?>... attrs)
			throws IOException {
		return exportZIPResourceToDirectory(resource, Check.nonNullS(Files.createTempDirectory(dir, prefix, attrs)),
				attrs);
	}

	private static Path exportZIPResourceToDirectory(URL resource, Path dir, FileAttribute<?>... attrs)
			throws IOException {
		try (ZipInputStream in = new ZipInputStream(resource.openStream())) {
			ZipEntry entry;

			while ((entry = in.getNextEntry()) != null) {
				Path entryPath = dir.resolve(entry.getName());

				if (entry.isDirectory()) {
					Files.createDirectories(entryPath, attrs);
				} else {
					Files.createDirectories(entryPath.getParent(), attrs);
					try (OutputStream out = Check
							.nonNullS(Files.newOutputStream(entryPath, StandardOpenOption.CREATE_NEW))) {
						copyStream(in, out);
					}
				}
				in.closeEntry();
			}
		}
		return dir;
	}

}
