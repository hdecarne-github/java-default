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
package de.carne.util.validation;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain InputValidator} implementation for {@linkplain String} input.
 */
public class StringValidator extends InputValidator<@NonNull String> {

	private StringValidator(String input) {
		super(input);
	}

	/**
	 * Checks that an input value is not empty.
	 *
	 * @param input the input value to check.
	 * @param message the validation message to create in case the validation fails.
	 * @return a new {@linkplain StringValidator} instance for further validation steps.
	 * @throws ValidationException if the validation fails.
	 */
	public static StringValidator checkNotEmpty(@Nullable String input, ValidationMessage message)
			throws ValidationException {
		if (input == null || input.length() == 0) {
			throw new ValidationException(message.format(input));
		}
		return new StringValidator(input);
	}

}
