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
package de.carne.text;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Basic hex formatting support.
 */
public final class HexFormat {

	/**
	 * Upper case {@linkplain HexFormat} instance.
	 */
	public static final HexFormat UPPER_CASE = new HexFormat(true);

	/**
	 * Lower case {@linkplain HexFormat} instance.
	 */
	public static final HexFormat LOWER_CASE = new HexFormat(false);

	private final char[] hexChars;
	private final @Nullable String prefix;
	private final @Nullable String suffix;
	private final int baseBufferSize;

	/**
	 * Constructs a {@linkplain HexFormat} instance.
	 *
	 * @param upperCase whether to use upper case ({@code true}) or lower case ({@code false}) formatting.
	 */
	public HexFormat(boolean upperCase) {
		this(upperCase, null, null);
	}

	/**
	 * Constructs a {@linkplain HexFormat} instance.
	 *
	 * @param upperCase whether to use upper case ({@code true}) or lower case ({@code false}) formatting.
	 * @param prefix the prefix to use for formatting (may by {@code null}).
	 * @param suffix the suffix to use for formatting (may by {@code null}).
	 */
	public HexFormat(boolean upperCase, @Nullable String prefix, @Nullable String suffix) {
		this.hexChars = (upperCase ? HexChars.UPPER_CASE : HexChars.LOWER_CASE);
		this.prefix = prefix;
		this.suffix = suffix;
		this.baseBufferSize = (this.prefix != null ? this.prefix.length() : 0)
				+ (this.suffix != null ? this.suffix.length() : 0);
	}

	/**
	 * Formats a {@code byte} value.
	 *
	 * @param b the {@code byte} value to format.
	 * @return the format result.
	 */
	public String format(byte b) {
		return format(new StringBuilder(this.baseBufferSize + 2), b).toString();
	}

	/**
	 * Formats a {@code byte} value.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param b the {@code byte} value to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, byte b) {
		if (this.prefix != null) {
			buffer.append(this.prefix);
		}
		buffer.append(this.hexChars[(b >> 4) & 0xf]);
		buffer.append(this.hexChars[b & 0xf]);
		if (this.suffix != null) {
			buffer.append(this.suffix);
		}
		return buffer;
	}

	/**
	 * Formats a {@code byte} array.
	 *
	 * @param bs the {@code byte} array to format.
	 * @return the format result.
	 */
	public String format(byte[] bs) {
		return format(new StringBuilder(((this.baseBufferSize + 3) * bs.length) - 1), bs).toString();
	}

	/**
	 * Formats a {@code byte} array.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param bs the {@code byte} array to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, byte[] bs) {
		return format(buffer, bs, 0, bs.length);
	}

	/**
	 * Formats a {@code byte} array range.
	 *
	 * @param bs the {@code byte} array to format.
	 * @param off the offset of the first byte to format.
	 * @param len the number of bytes to format.
	 * @return the format result.
	 */
	public String format(byte[] bs, int off, int len) {
		return format(new StringBuilder(((this.baseBufferSize + 3) * len) - 1), bs, off, len).toString();
	}

	/**
	 * Formats a {@code byte} array range.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param bs the {@code byte} array to format.
	 * @param off the offset of the first byte to format.
	 * @param len the number of bytes to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, byte[] bs, int off, int len) {
		for (int bIndex = 0; bIndex < len; bIndex++) {
			if (bIndex > 0) {
				buffer.append(' ');
			}
			format(buffer, bs[off + bIndex]);
		}
		return buffer;
	}

	/**
	 * Formats a {@code short} value.
	 *
	 * @param s the {@code short} value to format.
	 * @return the format result.
	 */
	public String format(short s) {
		return format(new StringBuilder(this.baseBufferSize + 4), s).toString();
	}

	/**
	 * Formats a {@code short} value.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param s the {@code short} value to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, short s) {
		if (this.prefix != null) {
			buffer.append(this.prefix);
		}
		buffer.append(this.hexChars[(s >> 12) & 0xf]);
		buffer.append(this.hexChars[(s >> 8) & 0xf]);
		buffer.append(this.hexChars[(s >> 4) & 0xf]);
		buffer.append(this.hexChars[s & 0xf]);
		if (this.suffix != null) {
			buffer.append(this.suffix);
		}
		return buffer;
	}

	/**
	 * Formats a {@code int} value.
	 *
	 * @param i the {@code int} value to format.
	 * @return the format result.
	 */
	public String format(int i) {
		return format(new StringBuilder(this.baseBufferSize + 8), i).toString();
	}

	/**
	 * Formats a {@code int} value.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param i the {@code int} value to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, int i) {
		if (this.prefix != null) {
			buffer.append(this.prefix);
		}
		buffer.append(this.hexChars[(i >> 28) & 0xf]);
		buffer.append(this.hexChars[(i >> 24) & 0xf]);
		buffer.append(this.hexChars[(i >> 20) & 0xf]);
		buffer.append(this.hexChars[(i >> 16) & 0xf]);
		buffer.append(this.hexChars[(i >> 12) & 0xf]);
		buffer.append(this.hexChars[(i >> 8) & 0xf]);
		buffer.append(this.hexChars[(i >> 4) & 0xf]);
		buffer.append(this.hexChars[i & 0xf]);
		if (this.suffix != null) {
			buffer.append(this.suffix);
		}
		return buffer;
	}

	/**
	 * Formats a {@code long} value.
	 *
	 * @param l the {@code long} value to format.
	 * @return the format result.
	 */
	public String format(long l) {
		return format(new StringBuilder(this.baseBufferSize + 16), l).toString();
	}

	/**
	 * Formats a {@code long} value.
	 *
	 * @param buffer the {@linkplain StringBuilder} to format into.
	 * @param l the {@code long} value to format.
	 * @return the format result.
	 */
	public StringBuilder format(StringBuilder buffer, long l) {
		if (this.prefix != null) {
			buffer.append(this.prefix);
		}
		buffer.append(this.hexChars[(int) ((l >> 60) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 56) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 52) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 48) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 44) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 40) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 36) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 32) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 28) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 24) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 20) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 16) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 12) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 8) & 0xfl)]);
		buffer.append(this.hexChars[(int) ((l >> 4) & 0xfl)]);
		buffer.append(this.hexChars[(int) (l & 0xfl)]);
		if (this.suffix != null) {
			buffer.append(this.suffix);
		}
		return buffer;
	}

}
