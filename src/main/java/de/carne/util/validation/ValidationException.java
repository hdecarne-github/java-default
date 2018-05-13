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
package de.carne.util.validation;

/**
 * This {@linkplain Exception} indicates validation failures during input validation.
 *
 * @see InputValidator
 */
public final class ValidationException extends Exception {

	// Serialization support
	private static final long serialVersionUID = -4479190426070123965L;

	/**
	 * Constructs a new {@linkplain ValidationException} instance.
	 *
	 * @param message the exception message.
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new {@linkplain ValidationException} instance.
	 *
	 * @param message the exception message.
	 * @param cause the causing exception.
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
