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

}
