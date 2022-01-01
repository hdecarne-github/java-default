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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain FilterOutputStream} that calculates a checksum of the written data.
 */
public class ChecksumOutputStream extends FilterOutputStream {

	private final Checksum checksum;

	/**
	 * Constructs a new {@linkplain ChecksumOutputStream} instance.
	 *
	 * @param out the underlying {@linkplain OutputStream}.
	 * @param checksum the {@linkplain Checksum} instance to use for checksum calculation.
	 */
	public ChecksumOutputStream(OutputStream out, Checksum checksum) {
		super(out);
		this.checksum = checksum;
	}

	/**
	 * Gets the checksum value corresponding to the written data and resets the checksum processor.
	 *
	 * @return the checksum value corresponding to the written data.
	 * @see Checksum#getValue()
	 */
	public byte[] getChecksumValue() {
		return this.checksum.getValue();
	}

	@Override
	public void write(int b) throws IOException {
		this.out.write(b);
		this.checksum.update((byte) b);
	}

	@SuppressWarnings("null")
	@Override
	public void write(byte @Nullable [] b) throws IOException {
		write(b, 0, b.length);
	}

	@SuppressWarnings("null")
	@Override
	public void write(byte @Nullable [] b, int off, int len) throws IOException {
		this.out.write(b, off, len);
		this.checksum.update(b, off, len);
	}

}
