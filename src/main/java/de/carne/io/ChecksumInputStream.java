/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain FilterInputStream} that calculates a checksum of the read data.
 */
public class ChecksumInputStream extends FilterInputStream {

	private final Checksum checksum;

	/**
	 * Constructs a new {@linkplain ChecksumInputStream} instance.
	 *
	 * @param in the underlying {@linkplain InputStream}.
	 * @param checksum the {@linkplain Checksum} instance to use for checksum calculation.
	 */
	public ChecksumInputStream(InputStream in, Checksum checksum) {
		super(in);
		this.checksum = checksum;
	}

	/**
	 * Gets the checksum value corresponding to the read data and resets the checksum processor.
	 *
	 * @return the checksum value corresponding to the read data.
	 * @see Checksum#getValue()
	 */
	public byte[] getChecksumValue() {
		return this.checksum.getValue();
	}

	@Override
	public int read() throws IOException {
		int read = this.in.read();

		if (read >= 0) {
			this.checksum.update((byte) read);
		}
		return read;
	}

	@SuppressWarnings("null")
	@Override
	public int read(byte @Nullable [] b) throws IOException {
		return read(b, 0, b.length);
	}

	@SuppressWarnings("null")
	@Override
	public int read(byte @Nullable [] b, int off, int len) throws IOException {
		int read = this.in.read(b, off, len);

		if (read > 0) {
			this.checksum.update(b, off, read);
		}
		return read;
	}

	@Override
	public long skip(long n) throws IOException {
		byte[] buffer = new byte[Defaults.DEFAULT_BUFFER_SIZE];

		long totalRead = 0;

		while (totalRead < n) {
			int read = read(buffer);

			if (read < 0) {
				break;
			}
			totalRead += read;
		}
		return totalRead;
	}

	@Override
	public synchronized void mark(int readlimit) {
		// Nothing to do here
	}

	@Override
	public synchronized void reset() throws IOException {
		throw new IOException("mark/reset not supported");
	}

	@Override
	public boolean markSupported() {
		return false;
	}

}
