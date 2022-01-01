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

/**
 * Utility class providing hex sequence conversion functions.
 */
public final class HexBytes {

	private HexBytes() {
		// Prevent instantiation
	}

	/**
	 * Converts a byte array to a lower case hex sequence.
	 *
	 * @param bs the byte array to convert.
	 * @return the hex sequence representing the byte array.
	 */
	public static String toStringL(byte[] bs) {
		return toStringL(bs, 0, bs.length);
	}

	/**
	 * Converts a byte array to a lower case hex sequence.
	 *
	 * @param bs the byte array to convert.
	 * @param off the first byte to convert.
	 * @param len the number of bytes to convert.
	 * @return the hex sequence representing the byte array.
	 */
	public static String toStringL(byte[] bs, int off, int len) {
		StringBuilder buffer = new StringBuilder(len);

		for (int byteIndex = 0; byteIndex < len; byteIndex++) {
			buffer.append(HexChars.LOWER_CASE[((bs[off + byteIndex] >> 4) & 0xf)]);
			buffer.append(HexChars.LOWER_CASE[(bs[off + byteIndex] & 0xf)]);
		}
		return buffer.toString();
	}

	/**
	 * Converts a byte array to an upper case hex sequence.
	 *
	 * @param bs the byte array to convert.
	 * @return the hex sequence representing the byte array.
	 */
	public static String toStringU(byte[] bs) {
		return toStringU(bs, 0, bs.length);
	}

	/**
	 * Converts a byte array to an upper case hex sequence.
	 *
	 * @param bs the byte array to convert.
	 * @param off the first byte to convert.
	 * @param len the number of bytes to convert.
	 * @return the hex sequence representing the byte array.
	 */
	public static String toStringU(byte[] bs, int off, int len) {
		StringBuilder buffer = new StringBuilder(len);

		for (int byteIndex = 0; byteIndex < len; byteIndex++) {
			buffer.append(HexChars.UPPER_CASE[((bs[off + byteIndex] >> 4) & 0xf)]);
			buffer.append(HexChars.UPPER_CASE[(bs[off + byteIndex] & 0xf)]);
		}
		return buffer.toString();
	}

	/**
	 * Converts a hex sequence to a byte array.
	 *
	 * @param s the hex sequence to convert.
	 * @return the byte array represented by the submitted hex sequence.
	 * @throws NumberFormatException if the hex sequence is invalid.
	 */
	public static byte[] valueOf(String s) {
		int sLength = s.length();

		if ((sLength % 2) != 0) {
			throw new NumberFormatException("Invalid hex sequence: " + s);
		}

		byte[] value = new byte[sLength / 2];
		int cIndex = 0;

		while (cIndex < sLength) {
			int bIndex = cIndex / 2;
			int valueH = valueOf(s.charAt(cIndex++));
			int valueL = valueOf(s.charAt(cIndex++));

			value[bIndex] = (byte) ((valueH << 4) | valueL);
		}
		return value;
	}

	/**
	 * Converts a hex character to a byte value.
	 *
	 * @param c the hex character to convert.
	 * @return the byte value represented by the submitted hex character.
	 * @throws NumberFormatException if the hex character is invalid.
	 */
	public static int valueOf(char c) {
		int value;

		if ('0' <= c && c <= '9') {
			value = c - '0';
		} else if ('a' <= c && c <= 'f') {
			value = 10 + (c - 'a');
		} else if ('A' <= c && c <= 'F') {
			value = 10 + (c - 'A');
		} else {
			throw new NumberFormatException("Invalid hex char: " + c);
		}
		return value;
	}

}
