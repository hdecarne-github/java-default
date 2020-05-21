/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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

import java.text.MessageFormat;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class providing code check related functions.
 */
public final class Check {

	private Check() {
		// Prevent instantiation
	}

	/**
	 * Checks and ensures that an {@linkplain Object} is an instance of a specific type.
	 *
	 * @param <T> the type to ensure.
	 * @param object the {@linkplain Object} to check.
	 * @param type the type to ensure.
	 * @return the checked {@linkplain Object} (casted to the checked type}).
	 * @throws IllegalArgumentException if the submitted argument is not an instance of the given type.
	 */
	public static <T> T isInstanceOf(@Nullable Object object, Class<T> type) {
		if (object == null) {
			throw new NullPointerException();
		}
		if (!type.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException();
		}
		return type.cast(object);
	}

	/**
	 * Checks and ensures that an {@linkplain Object} is an instance of a specific type.
	 * <p>
	 * The {@linkplain MessageFormat} class is used to format an exception message in case the check fails.
	 *
	 * @param <T> the type to ensure.
	 * @param object the {@linkplain Object} to check.
	 * @param type the type to ensure.
	 * @param pattern the exception message pattern to use in case the check fails.
	 * @param arguments the exception message arguments to use in case the check fails.
	 * @return the checked {@linkplain Object} (casted to the checked type}).
	 * @throws IllegalArgumentException if the submitted argument is not an instance of the given type.
	 */
	public static <T> T isInstanceOf(@Nullable Object object, Class<T> type, String pattern, Object... arguments) {
		if (object == null) {
			throw new NullPointerException(MessageFormat.format(pattern, arguments));
		}
		if (!type.isAssignableFrom(object.getClass())) {
			throw new IllegalArgumentException(MessageFormat.format(pattern, arguments));
		}
		return type.cast(object);
	}

	/**
	 * Checks and ensures that a specific condition is met.
	 *
	 * @param condition the condition to check.
	 * @throws IllegalArgumentException if the condition is not met.
	 */
	public static void isTrue(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Checks and ensures that a specific condition is met.
	 * <p>
	 * The {@linkplain MessageFormat} class is used to format an exception message in case the check fails.
	 *
	 * @param condition the condition to check.
	 * @param pattern the exception message pattern to use in case the check fails.
	 * @param arguments the exception message arguments to use in case the check fails.
	 * @throws IllegalArgumentException if the condition is not met.
	 */
	public static void isTrue(boolean condition, String pattern, Object... arguments) {
		if (!condition) {
			throw new IllegalArgumentException(MessageFormat.format(pattern, arguments));
		}
	}

	/**
	 * Checks and ensures that a specific condition is met.
	 *
	 * @param condition the condition to check.
	 * @throws IllegalStateException if the condition is not met.
	 */
	public static void assertTrue(boolean condition) {
		if (!condition) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Checks and ensures that a specific condition is met.
	 * <p>
	 * The {@linkplain MessageFormat} class is used to format an exception message in case the check fails.
	 *
	 * @param condition the condition to check.
	 * @param pattern the exception message pattern to use in case the check fails.
	 * @param arguments the exception message arguments to use in case the check fails.
	 * @throws IllegalStateException if the condition is not met.
	 */
	public static void assertTrue(boolean condition, String pattern, Object... arguments) {
		if (!condition) {
			throw new IllegalStateException(MessageFormat.format(pattern, arguments));
		}
	}

	/**
	 * Throws an {@linkplain IllegalStateException} to indicate that an unexpected execution state occurred.
	 *
	 * @return Nothing (function never returns).
	 * @throws IllegalStateException any time this function is called.
	 */
	public static IllegalStateException fail() {
		throw new IllegalStateException();
	}

	/**
	 * Throws an {@linkplain IllegalStateException} to indicate that an unexpected execution state occurred.
	 * <p>
	 * The {@linkplain MessageFormat} class is used to format an exception message in case the check fails.
	 *
	 * @param pattern the exception message pattern to use in case the check fails.
	 * @param arguments the exception message arguments to use in case the check fails.
	 * @return Nothing (function never returns).
	 * @throws IllegalStateException any time this function is called.
	 */
	public static IllegalStateException fail(String pattern, Object... arguments) {
		throw new IllegalStateException(MessageFormat.format(pattern, arguments));
	}

	/**
	 * Throws an {@linkplain IllegalStateException} to indicate that an unexpected data object has been encountered.
	 *
	 * @param object the unexpected data object:
	 * @return Nothing (function never returns).
	 * @throws IllegalStateException any time this function is called.
	 */
	public static IllegalStateException unexpected(Object object) {
		throw new IllegalStateException("Unexpected " + object.getClass().getName() + ": " + object);
	}

}
