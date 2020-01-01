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

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Base class for all kinds of input validation classes.
 *
 * @param <I> the actual input type.
 */
public class InputValidator<I> implements Supplier<I> {

	private final I input;

	/**
	 * Constructs a new {@linkplain InputValidator} instance.
	 *
	 * @param input the input value to validate.
	 */
	protected InputValidator(I input) {
		this.input = input;
	}

	/**
	 * Constructs a new {@linkplain InputValidator} instance for input validation.
	 *
	 * @param <I> the actual input type.
	 * @param input the input value to check.
	 * @return the new {@linkplain InputValidator} instance for further validation steps.
	 */
	public static <I> InputValidator<I> input(I input) {
		return new InputValidator<>(input);
	}

	/**
	 * Checks that an input value is not {@code null}.
	 *
	 * @param <I> the actual input type.
	 * @param input the input value to check.
	 * @param message the validation message to create in case the validation fails.
	 * @return a new {@linkplain InputValidator} instance for further validation steps.
	 * @throws ValidationException if the validation fails.
	 */
	public static <I> InputValidator<I> checkNotNull(@Nullable I input, ValidationMessage message)
			throws ValidationException {
		if (input == null) {
			throw new ValidationException(message.format(input));
		}
		return new InputValidator<>(input);
	}

	/**
	 * Checks the given validation.
	 *
	 * @param validation the {@linkplain Validation} to perform.
	 * @param message the validation message to create in case the validation fails.
	 * @return the validated {@linkplain InputValidator} instance for further validation steps.
	 * @throws ValidationException if the validation fails.
	 */
	public InputValidator<I> check(Validation<I> validation, ValidationMessage message) throws ValidationException {
		try {
			if (!validation.check(this.input)) {
				throw new ValidationException(message.format(this.input));
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidationException(message.format(this.input), e);
		}
		return this;
	}

	/**
	 * Checks and converts the input value against the given conversion.
	 *
	 * @param <O> the actual output type.
	 * @param conversion the {@linkplain Conversion} to apply.
	 * @param message the validation message to create in case the conversion fails.
	 * @return the converted {@linkplain InputValidator} instance for further validation steps.
	 * @throws ValidationException if the conversion fails.
	 */
	public <O> O convert(Conversion<I, O> conversion, ValidationMessage message) throws ValidationException {
		@Nullable O convertedInput = null;

		try {
			convertedInput = conversion.checkAndApply(this.input);
			if (convertedInput == null) {
				throw new ValidationException(message.format(this.input));
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidationException(message.format(this.input), e);
		}
		return convertedInput;
	}

	@Override
	public I get() {
		return this.input;
	}

}
