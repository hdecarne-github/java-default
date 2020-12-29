/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.text.HexFormat;

/**
 * Immutable byte string support.
 */
public final class ByteString implements Serializable, Comparable<ByteString> {

	// Serialization support
	private static final long serialVersionUID = 2167856394985799551L;

	/**
	 * The empty byte string.
	 */
	public static final ByteString EMPTY = new ByteString(new byte[0]);

	private final byte[] bytes;
	private final int start;
	private final int length;

	private ByteString(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}

	private ByteString(byte[] bytes, int start, int length) {
		this.bytes = bytes;
		this.start = start;
		this.length = length;
	}

	/**
	 * Constructs a new {@linkplain ByteString} instance by wrapping the submitted bytes directly.
	 *
	 * @param bytes the bytes to wrap.
	 * @return the constructed {@linkplain ByteString} instance.
	 */
	public static ByteString wrap(byte... bytes) {
		return (bytes.length > 0 ? new ByteString(bytes) : EMPTY);
	}

	/**
	 * Constructs a new {@linkplain ByteString} instance by wrapping the submitted bytes directly.
	 *
	 * @param bytes the bytes to wrap.
	 * @param start the index of the first byte to wrap.
	 * @param length the number of bytes to wrap.
	 * @return the constructed {@linkplain ByteString} instance.
	 */
	public static ByteString wrap(byte[] bytes, int start, int length) {
		Check.isTrue(0 <= start);
		Check.isTrue(0 <= length);
		Check.isTrue(start + length <= bytes.length);

		return (length > 0 ? new ByteString(bytes, start, length) : EMPTY);
	}

	/**
	 * Constructs a new {@linkplain ByteString} instance by copying the submitted bytes.
	 *
	 * @param bytes the bytes to copy.
	 * @return the constructed {@linkplain ByteString} instance.
	 */
	public static ByteString copy(byte... bytes) {
		return copy(bytes, 0, bytes.length);
	}

	/**
	 * Constructs a new {@linkplain ByteString} instance by copying the submitted bytes.
	 *
	 * @param bytes the bytes to copy.
	 * @param start the index of the first byte to copy.
	 * @param length the number of bytes to copy.
	 * @return the constructed {@linkplain ByteString} instance.
	 */
	public static ByteString copy(byte[] bytes, int start, int length) {
		Check.isTrue(0 <= start);
		Check.isTrue(0 <= length);
		Check.isTrue(start + length <= bytes.length);

		return (length > 0 ? new ByteString(Arrays.copyOfRange(bytes, start, start + length)) : EMPTY);
	}

	/**
	 * Gets the length of this instance.
	 *
	 * @return the length of this instance.
	 */
	public int length() {
		return this.length;
	}

	/**
	 * Gets the this instance's byte value at the given index.
	 *
	 * @param index the index of the byte to get.
	 * @return the byte value at the given index
	 */
	public byte byteAt(int index) {
		Check.isTrue(0 <= index);
		Check.isTrue(index < this.length);

		return this.bytes[this.start + index];
	}

	/**
	 * Copies this instances bytes to a destination buffer.
	 *
	 * @param dest the buffer to copy into.
	 * @param destPos the buffer position to start copying at.
	 */
	public void copyTo(byte[] dest, int destPos) {
		Check.isTrue(0 <= destPos);
		Check.isTrue(dest.length - destPos >= this.length);

		System.arraycopy(this.bytes, this.start, dest, destPos, this.length);
	}

	/**
	 * Copies this instances bytes to the given {@linkplain OutputStream}.
	 *
	 * @param out the {@linkplain OutputStream} to write to.
	 * @throws IOException if an I/O error occurs.
	 */
	public void write(OutputStream out) throws IOException {
		out.write(this.bytes, this.start, this.length);
	}

	/**
	 * Slices a sub-section from this instance.
	 *
	 * @param sliceStart the index of the first byte to slice.
	 * @param sliceLength the number of bytes to slice.
	 * @return the sliced byte string.
	 */
	public ByteString slice(int sliceStart, int sliceLength) {
		Check.isTrue(this.start <= sliceStart);
		Check.isTrue(sliceStart + sliceLength <= this.length);

		return (this.start == sliceStart && this.length == sliceLength ? this
				: new ByteString(this.bytes, this.start + sliceStart, sliceLength));
	}

	@Override
	public int compareTo(ByteString o) {
		int byteIndex1 = this.start;
		int byteIndexLimit1 = this.start + this.length;
		int byteIndex2 = o.start;
		int byteIndexLImit2 = o.start + o.length;
		int comparison = 0;

		while (byteIndex1 < byteIndexLimit1 && byteIndex2 < byteIndexLImit2 && comparison == 0) {
			comparison = Integer.compare(Byte.toUnsignedInt(this.bytes[byteIndex1]),
					Byte.toUnsignedInt(o.bytes[byteIndex2]));
			byteIndex1++;
			byteIndex2++;
		}
		if (comparison == 0) {
			if (byteIndex1 < byteIndexLimit1) {
				comparison = 1;
			} else if (byteIndex2 < byteIndexLImit2) {
				comparison = -1;
			}
		}
		return comparison;
	}

	@Override
	public int hashCode() {
		int hashLimit = Math.min(4, this.length);
		int hash = 0;

		for (int hashIndex = 0; hashIndex < hashLimit; hashIndex++) {
			hash = (hash << 8) | (this.bytes[this.start + hashIndex] & 0xff);
		}
		return hash;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return this == obj || (obj instanceof ByteString && compareTo((ByteString) obj) == 0);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		int formatLimit = Math.min(16, this.length);

		HexFormat.LOWER_CASE.format(buffer, this.bytes, this.start, formatLimit);
		if (formatLimit < this.length) {
			buffer.append(Strings.ELLIPSIS);
		}
		return buffer.toString();
	}

}
