/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.util.validation.StringValidator;
import de.carne.util.validation.ValidationException;
import de.carne.util.validation.ValidationMessage;

/**
 * Test {@linkplain StringValidator} class.
 */
class StringValidatorTest {

	private static final ValidationMessage VALIDATION_MESSAGE = arguments -> String.format("Invalid string '%1$s'",
			arguments);

	@Test
	void testStringValidation() throws ValidationException {
		String input1 = null;
		String message1 = Assertions.assertThrows(ValidationException.class, () -> {
			StringValidator.checkNotEmpty(input1, VALIDATION_MESSAGE);
		}).getMessage();

		Assertions.assertEquals(VALIDATION_MESSAGE.format(input1), message1);

		String input2 = "";
		String message2 = Assertions.assertThrows(ValidationException.class, () -> {
			StringValidator.checkNotEmpty(input2, VALIDATION_MESSAGE);
		}).getMessage();

		Assertions.assertEquals(VALIDATION_MESSAGE.format(input2), message2);

		String input3 = getClass().getName();
		StringValidator stringValidator = StringValidator.checkNotEmpty(input3, VALIDATION_MESSAGE);

		Assertions.assertEquals(input3, stringValidator.get());
	}

}
