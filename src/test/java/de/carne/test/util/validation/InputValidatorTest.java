/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.util.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.validation.InputValidator;
import de.carne.util.validation.ValidationException;
import de.carne.util.validation.ValidationMessage;

/**
 * Test {@linkplain InputValidator} class.
 */
class InputValidatorTest {

	private final static ValidationMessage VALIDATION_MESSAGE = arguments -> String.format("Invalid input: %1$s",
			arguments);

	@Test
	void testCheckNotNull() throws ValidationException {
		String input1 = null;

		String message1 = Assertions.assertThrows(ValidationException.class, () -> {
			InputValidator.checkNotNull(input1, VALIDATION_MESSAGE);
		}).getMessage();

		Assertions.assertEquals(VALIDATION_MESSAGE.format(input1), message1);

		String input2 = getClass().getName();

		InputValidator.checkNotNull(input2, VALIDATION_MESSAGE);
	}

	@Test
	void testCheckValidation() throws ValidationException {
		String input = getClass().getName();

		String message1 = Assertions.assertThrows(ValidationException.class, () -> {
			InputValidator.input(input).check(input2 -> !input2.equals(input), VALIDATION_MESSAGE);
		}).getMessage();

		Assertions.assertEquals(VALIDATION_MESSAGE.format(input), message1);

		InputValidator.input(input).check(input2 -> input2.equals(input), VALIDATION_MESSAGE);
	}

}
