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
package de.carne.util;

import de.carne.check.Nullable;

/**
 * Utility class providing {@linkplain String} related functions.
 */
public final class Strings {

	private Strings() {
		// Prevent instantiation
	}

	/**
	 * Check whether a {@linkplain String} is empty.
	 * <p>
	 * A {@linkplain String} is considered empty if it is either {@code null} or of length {@code 0}.
	 *
	 * @param s The {@linkplain String} to check.
	 * @return {@code true} if the string is empty.
	 */
	public static boolean isEmpty(@Nullable String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Check whether a {@linkplain String} is not empty.
	 * <p>
	 * A {@linkplain String} is considered empty if it is either {@code null} or of length {@code 0}.
	 *
	 * @param s The {@linkplain String} to check.
	 * @return {@code true} if the string is not empty.
	 */
	public static boolean notEmpty(@Nullable String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * Make sure a {@linkplain String} is not {@code null}.
	 *
	 * @param s The {@linkplain String} to secure.
	 * @return The submitted {@linkplain String} or {@code ""} if {@code null} was submitted.
	 */
	public static String safe(@Nullable String s) {
		return (s != null ? s : "");
	}

	/**
	 * Make sure a {@linkplain String} is not {@code null} and trimmed.
	 *
	 * @param s The {@linkplain String} to secure.
	 * @return The submitted {@linkplain String} in trimmred form or {@code ""} if {@code null} was submitted.
	 */
	public static String safeTrim(@Nullable String s) {
		return (s != null ? s.trim() : "");
	}

	private static char[] hexCharsUpper = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * Encode a {@linkplain CharSequence} to a pure ASCII representation by quoting non printable characters.
	 *
	 * @param chars The {@linkplain CharSequence} to encode.
	 * @return The encoded characters.
	 */
	public static String encode(CharSequence chars) {
		StringBuilder buffer = new StringBuilder();

		chars.chars().forEach(c -> {
			if (c == '\\') {
				buffer.append("\\\\");
			} else if (32 <= c && c <= 126) {
				buffer.append((char) c);
			} else {
				switch (c) {
				case 0:
					buffer.append("\\0");
					break;
				case 8:
					buffer.append("\\b");
					break;
				case 9:
					buffer.append("\\t");
					break;
				case 10:
					buffer.append("\\n");
					break;
				case 12:
					buffer.append("\\f");
					break;
				case 13:
					buffer.append("\\r");
					break;
				default:
					buffer.append("\\u").append(hexCharsUpper[(c >> 12) & 0xf]).append(hexCharsUpper[(c >> 8) & 0xf])
							.append(hexCharsUpper[(c >> 4) & 0xf]).append(hexCharsUpper[c & 0xf]);
				}
			}
		});
		return buffer.toString();
	}

	/**
	 * Decode a {@linkplain CharSequence} previously encoded via {@linkplain #encode(CharSequence)}.
	 *
	 * @param chars The {@linkplain CharSequence} to decode.
	 * @return The decoded characters.
	 */
	public static String decode(CharSequence chars) {
		CharDecoder decoder = new CharDecoder();

		chars.chars().forEach(decoder::decode);
		return decoder.getDecoded();
	}

	private static class CharDecoder {

		private final StringBuilder buffer = new StringBuilder();
		private boolean quoted = false;
		private int encodeIndex = 0;
		private int decodedC = 0;

		CharDecoder() {
			// To make it accessible to the outer class
		}

		public void decode(int c) {
			if (!this.quoted) {
				decodeUnquoted(c);
			} else if (this.encodeIndex == 0) {
				decodeQuoted(c);
			} else {
				decodeQuotedEncoded(c);
			}
		}

		private void decodeUnquoted(int c) {
			if (c != '\\') {
				this.buffer.append((char) c);
			} else {
				this.quoted = true;
			}
		}

		private void decodeQuoted(int c) {
			if (c != 'u') {
				this.quoted = false;

				char unquotedC;

				switch (c) {
				case '\\':
					unquotedC = '\\';
					break;
				case '0':
					unquotedC = '\0';
					break;
				case 'b':
					unquotedC = '\b';
					break;
				case 't':
					unquotedC = '\t';
					break;
				case 'n':
					unquotedC = '\n';
					break;
				case 'f':
					unquotedC = '\f';
					break;
				case 'r':
					unquotedC = '\r';
					break;
				default:
					throw new IllegalArgumentException("Unexpected quoted character: " + ((char) c));
				}
				this.buffer.append(unquotedC);
			} else {
				this.encodeIndex = 1;
				this.decodedC = 0;
			}
		}

		private void decodeQuotedEncoded(int c) {
			this.encodeIndex = (this.encodeIndex + 1) % 5;
			this.quoted = this.encodeIndex != 0;
			this.decodedC <<= 4;
			if ('0' <= c && c <= '9') {
				this.decodedC |= c - '0';
			} else if ('a' <= c && c <= 'f') {
				this.decodedC |= c - 'a' + 10;
			} else if ('A' <= c && c <= 'F') {
				this.decodedC |= c - 'A' + 10;
			} else {
				throw new IllegalArgumentException("Unexpected encoded character: " + ((char) c));
			}
			if (!this.quoted) {
				this.buffer.append((char) this.decodedC);
			}
		}

		public String getDecoded() {
			return this.buffer.toString();
		}

	}

}
