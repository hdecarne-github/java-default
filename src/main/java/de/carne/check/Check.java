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
public final class Check {

	private Check() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Check and ensure that an object is not {@code null}.
	 *
	 * @param <T> The actual object type.
	 * @param o The object to check.
	 * @return The checked object (never {@code null}).
	 * @throws NullPointerException if the submitted argument is {@code null}.
	 */
	public static <T> T nonNull(@Nullable T o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		return o;
	}

	/**
	 * Check and ensure that an object is not {@code null}.
	 *
	 * @param <T> The actual object type.
	 * @param o The object to check.
	 * @param message The message to issue if the check fails.
	 * @return The checked object (never {@code null}).
	 * @throws NullPointerException if the submitted argument is {@code null}.
	 */
	public static <T> T nonNull(@Nullable T o, String message) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException(message);
		}
		return o;
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

	/**
	 * Throw an exception to indicate that an unexpected execution state occurred.
	 *
	 * @return Nothing.
	 * @throws IllegalStateException any time this function is called.
	 */
	public static <T> T fail() throws IllegalStateException {
		throw new IllegalStateException();
	}

	/**
	 * Throw an exception to indicate that an unexpected execution state occurred.
	 *
	 * @param message The message to issue if the check fails.
	 * @return Nothing.
	 * @throws IllegalStateException any time this function is called.
	 */
	public static <T> T fail(String message) throws IllegalStateException {
		throw new IllegalStateException(message);
	}

}
