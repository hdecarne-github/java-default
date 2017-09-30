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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * Utility class providing I/O related functions.
 */
public final class IOUtil {

	private IOUtil() {
		// prevent instantiation
	}

	private static final int STREAM_IO_BUFFER_SIZE = 8192;

	private static final int CHANNEL_IO_BUFFER_SIZE = 8192;

	/**
	 * Copy all bytes from one stream to another.
	 *
	 * @param dst The {@linkplain OutputStream} to copy to.
	 * @param src The {@linkplain InputStream} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyStream(OutputStream dst, InputStream src) throws IOException {
		long copied;

		if (dst instanceof FileOutputStream && src instanceof FileInputStream) {
			try (FileChannel dstChannel = ((FileOutputStream) dst).getChannel();
					FileChannel srcChannel = ((FileInputStream) src).getChannel()) {
				copied = copyStreamChannel(dstChannel, srcChannel);
			}
		} else {
			copied = copyStreamStandard(dst, src);
		}
		return copied;
	}

	private static long copyStreamChannel(FileChannel dst, FileChannel src) throws IOException {
		long position = 0;
		long remaining = src.size();

		while (remaining > 0) {
			long transferred = dst.transferFrom(src, position, CHANNEL_IO_BUFFER_SIZE);

			position += transferred;
			remaining -= transferred;
		}
		dst.position(position);
		return position;
	}

	private static long copyStreamStandard(OutputStream dst, InputStream src) throws IOException {
		byte[] buffer = new byte[STREAM_IO_BUFFER_SIZE];
		long copied = 0;
		int read;

		while ((read = src.read(buffer)) > 0) {
			dst.write(buffer, 0, read);
			copied += read;
		}
		return copied;
	}

	/**
	 * Copy all bytes from an {@linkplain InputStream} to a {@linkplain File}.
	 *
	 * @param dst The {@linkplain File} to copy to.
	 * @param src The {@linkplain InputStream} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyStream(File dst, InputStream src) throws IOException {
		long copied;

		try (FileOutputStream dstStream = new FileOutputStream(dst)) {
			copied = copyStream(dstStream, src);
		}
		return copied;
	}

	/**
	 * Copy all bytes from an {@linkplain File} to an {@linkplain OutputStream}.
	 *
	 * @param dst The {@linkplain OutputStream} to copy to.
	 * @param src The {@linkplain File} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyFile(OutputStream dst, File src) throws IOException {
		long copied;

		try (FileInputStream srcStream = new FileInputStream(src)) {
			copied = copyStream(dst, srcStream);
		}
		return copied;
	}

	/**
	 * Copy all bytes from an {@linkplain File} to a {@linkplain File}.
	 *
	 * @param dst The {@linkplain File} to copy to.
	 * @param src The {@linkplain File} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyFile(File dst, File src) throws IOException {
		long copied;

		try (FileInputStream srcStream = new FileInputStream(src)) {
			copied = copyStream(dst, srcStream);
		}
		return copied;
	}

	/**
	 * Copy all bytes from an {@linkplain URL} to an {@linkplain OutputStream}.
	 *
	 * @param dst The {@linkplain OutputStream} to copy to.
	 * @param src The {@linkplain URL} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyUrl(OutputStream dst, URL src) throws IOException {
		long copied;

		try (InputStream srcStream = src.openStream()) {
			copied = copyStream(dst, srcStream);
		}
		return copied;
	}

	/**
	 * Copy all bytes from an {@linkplain URL} to a {@linkplain File}.
	 *
	 * @param dst The {@linkplain File} to copy to.
	 * @param src The {@linkplain URL} to copy from.
	 * @return The number of copied bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static long copyUrl(File dst, URL src) throws IOException {
		long copied;

		try (InputStream srcStream = src.openStream()) {
			copied = copyStream(dst, srcStream);
		}
		return copied;
	}

}
