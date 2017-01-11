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

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;

/**
 * {@link OutputStream} implementation used for limiting the number of bytes to
 * be written to an underlying {@link OutputStream}.
 * <p>
 * An {@link InterruptedIOException} is thrown as soon as the given limit is
 * reached.
 *
 * @param <T> The type of the underlying {@link OutputStream}.
 */
public class LimitOutputStream<T extends OutputStream> extends OutputStream {

	private final T out;

	private final long limit;

	private long totalWritten = 0;

	/**
	 * Construct {@code LimitOutputStream}.
	 *
	 * @param out The output stream to limit.
	 * @param limit The limit to apply.
	 */
	public LimitOutputStream(T out, long limit) {
		assert out != null;

		this.out = out;
		this.limit = limit;
	}

	/**
	 * The underlying {@link OutputStream}.
	 * 
	 * @return The underlying output stream.
	 */
	public T outputStream() {
		return this.out;
	}

	@Override
	public void write(int b) throws IOException {
		this.out.write(b);
		recordWrite(1);
	}

	@Override
	public void write(byte[] b) throws IOException {
		this.out.write(b);
		recordWrite(b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.out.write(b, off, len);
		recordWrite(len);
	}

	@Override
	public void flush() throws IOException {
		this.out.flush();
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}

	private synchronized void recordWrite(int written) throws IOException {
		this.totalWritten += written;
		if (this.totalWritten >= this.limit) {
			InterruptedIOException exception = new InterruptedIOException(
					"Output limit (" + this.limit + ") reached (" + this.totalWritten + ")");

			exception.bytesTransferred = (this.totalWritten <= Integer.MAX_VALUE ? (int) this.totalWritten : -1);
			throw exception;
		}
	}

}
