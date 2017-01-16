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
package de.carne.test.util.validation;

import java.util.regex.Pattern;

import org.junit.Test;

import de.carne.util.validation.InputValidator;
import de.carne.util.validation.ValidationException;

/**
 * Test {@link InputValidator} class.
 */
public class InputValidatorTest {

	private static final String VALIDATION_MESSAGE = InputValidator.class.getSimpleName();

	/**
	 * Test all validations in success scenario.
	 *
	 * @throws ValidationException if a validation fails.
	 */
	@Test
	public void testSuccess() throws ValidationException {
		InputValidator.isTrue(true, (a) -> VALIDATION_MESSAGE);
		InputValidator.notNull("1", (a) -> VALIDATION_MESSAGE);
		InputValidator.notEmpty("2", (a) -> VALIDATION_MESSAGE);
		InputValidator.matches("3", Pattern.compile("\\d*"), (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link InputValidator#isTrue(boolean, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 */
	@Test(expected = ValidationException.class)
	public void testIsTrueFailure() throws ValidationException {
		InputValidator.isTrue(false, (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link InputValidator#notNull(Object, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 */
	@Test(expected = ValidationException.class)
	public void testNotNullFailure() throws ValidationException {
		InputValidator.notNull(null, (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link InputValidator#notEmpty(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 */
	@Test(expected = ValidationException.class)
	public void testNotEmptyFailure1() throws ValidationException {
		InputValidator.notEmpty(null, (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link InputValidator#notEmpty(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 */
	@Test(expected = ValidationException.class)
	public void testNotEmptyFailure2() throws ValidationException {
		InputValidator.notEmpty("", (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link InputValidator#matches(String, Pattern, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 */
	@Test(expected = ValidationException.class)
	public void testMatchesFailure() throws ValidationException {
		InputValidator.matches("a", Pattern.compile("\\d*"), (a) -> VALIDATION_MESSAGE);
	}

}
