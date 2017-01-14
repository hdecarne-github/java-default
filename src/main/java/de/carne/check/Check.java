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
package de.carne.check;

/**
 * Utility class providing code check related functions.
 */
@NonNullByDefault
public final class Check {

	private Check() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Check and ensure that an argument is not {@code null}.
	 *
	 * @param <T> The actual argument type.
	 * @param a The argument to check.
	 * @return The checked argument (never {@code null}).
	 * @throws IllegalArgumentException if the submitted argument is {@code null}.
	 */
	public static <T> T nonNullA(@Nullable T a) throws IllegalArgumentException {
		if (a == null) {
			throw new IllegalArgumentException();
		}
		return a;
	}

	/**
	 * Check and ensure that an argument is not {@code null}.
	 *
	 * @param <T> The actual argument type.
	 * @param a The argument to check.
	 * @param message The message to issue if the check fails.
	 * @return The checked argument (never {@code null}).
	 * @throws IllegalArgumentException if the submitted argument is {@code null}.
	 */
	public static <T> T nonNullA(@Nullable T a, String message) throws IllegalArgumentException {
		if (a == null) {
			throw new IllegalArgumentException(message);
		}
		return a;
	}

	/**
	 * Check and ensure that a specific state object is not {@code null}.
	 *
	 * @param <T> The actual state type.
	 * @param s The state object to check.
	 * @return The checked state object (never {@code null}).
	 * @throws IllegalStateException if the state object is {@code null}.
	 */
	public static <T> T nonNullS(@Nullable T s) throws IllegalStateException {
		if (s == null) {
			throw new IllegalStateException();
		}
		return s;
	}

	/**
	 * Check and ensure that a specific state object is not {@code null}.
	 *
	 * @param <T> The actual state type.
	 * @param s The state object to check.
	 * @param message The message to issue if the check fails.
	 * @return The checked state object (never {@code null}).
	 * @throws IllegalStateException if the state object is {@code null}.
	 */
	public static <T> T nonNullS(@Nullable T s, String message) throws IllegalStateException {
		if (s == null) {
			throw new IllegalStateException(message);
		}
		return s;
	}

	/**
	 * Check and ensure that a specific condition is met.
	 *
	 * @param c The condition to check.
	 * @throws IllegalStateException if the condition is not met.
	 */
	public static void condition(boolean c) throws IllegalStateException {
		if (!c) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Check and ensure that a specific condition is met.
	 *
	 * @param c The condition to check.
	 * @param message The message to issue if the check fails.
	 * @throws IllegalStateException if the condition is not met.
	 */
	public static void condition(boolean c, String message) throws IllegalStateException {
		if (!c) {
			throw new IllegalStateException(message);
		}
	}

}
