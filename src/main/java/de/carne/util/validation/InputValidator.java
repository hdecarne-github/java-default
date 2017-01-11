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
package de.carne.util.validation;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.carne.util.MessageFormatter;

/**
 * This class provides validation function for all kind of general input.
 */
public final class InputValidator {

	private InputValidator() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Make sure a condition is met.
	 *
	 * @param condition The condition to check.
	 * @param message The exception message to use if the condition is
	 *        {@code false}.
	 * @throws ValidationException if the condition is {@code false}.
	 */
	public static void isTrue(boolean condition, MessageFormatter message) throws ValidationException {
		assert message != null;

		if (!condition) {
			throw new ValidationException(message.format());
		}
	}

	/**
	 * Make sure input is not {@code null}.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 */
	public static <T> T notNull(T input, MessageFormatter message) throws ValidationException {
		assert message != null;

		if (input == null) {
			throw new ValidationException(message.format());
		}
		return input;
	}

	/**
	 * Make sure input is not an empty {@link String}.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 */
	public static String notEmpty(String input, MessageFormatter message) throws ValidationException {
		assert message != null;

		if (input == null || input.length() == 0) {
			throw new ValidationException(message.format());
		}
		return input;
	}

	/**
	 * Make sure input matches a given {@link Pattern}.
	 *
	 * @param input The input to invalidated.
	 * @param pattern The pattern to check the input against.
	 * @param message The exception message to use if the input is invalid.
	 * @return The pattern's capturing groups.
	 * @throws ValidationException if the input is invalid.
	 */
	public static String[] matches(String input, Pattern pattern, MessageFormatter message) throws ValidationException {
		assert input != null;
		assert pattern != null;
		assert message != null;

		Matcher matcher = pattern.matcher(input);

		if (!matcher.matches()) {
			throw new ValidationException(message.format(input));
		}

		int groupCount = matcher.groupCount();
		String[] groups = new String[groupCount];

		for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
			groups[groupIndex] = matcher.group(groupIndex + 1);
		}
		return groups;
	}

}
