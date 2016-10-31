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
package de.carne.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility class providing {@link String} related functions.
 */
public final class Strings {

	private Strings() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Check whether a {@link String} is empty.
	 * <p>
	 * A {@link String} is considered empty if it is either {@code null} of of
	 * length 0.
	 *
	 * @param s The string to check.
	 * @return {@code true} if the string is empty.
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Check whether a {@link String} is not empty.
	 * <p>
	 * A {@link String} is considered empty if it is either {@code null} of of
	 * length 0.
	 *
	 * @param s The string to check.
	 * @return {@code true} if the string is not empty.
	 */
	public static boolean notEmpty(String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * Make sure a {@link String} is not {@code null}.
	 * <p>
	 * The empty string is returned if a {@code null} value is submitted.
	 *
	 * @param s The string to check.
	 * @return The submitted string or the empty string if {@code null} was
	 *         submitted.
	 */
	public static String safe(String s) {
		return (s != null ? s : "");
	}

	/**
	 * Make sure a {@link String} is trimmed.
	 * <p>
	 * If the string is {@code null} this function simply returns the submitted
	 * value.
	 *
	 * @param s The string to trim.
	 * @return The trimmed string or {@code null} if {@code null} was submitted.
	 */
	public static String safeTrim(String s) {
		return (s != null ? s.trim() : s);
	}

	/**
	 * Split a {@link String} into multiple sub strings.
	 *
	 * @param s The string to split.
	 * @param delimiter The single character string to use as a delimiter.
	 * @return The split string.
	 */
	public static String[] split(String s, String delimiter) {
		assert s != null;
		assert delimiter != null;
		assert delimiter.length() == 1;

		List<String> split = new ArrayList<>();
		StringTokenizer tokens = new StringTokenizer(s, delimiter);
		StringBuilder tokenBuffer = new StringBuilder();

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			if (token.length() > 0) {
				if (tokenBuffer.length() > 0) {
					split.add(tokenBuffer.toString());
					tokenBuffer.setLength(0);
				}
				tokenBuffer.append(token);
			} else {
				tokenBuffer.append(delimiter);
			}
		}
		if (tokenBuffer.length() > 0) {
			split.add(tokenBuffer.toString());
		}
		return split.toArray(new String[split.size()]);
	}

}