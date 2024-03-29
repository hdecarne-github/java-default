/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.util.function;

/**
 * {@linkplain RuntimeException} type used to handle exception during execution of a functional interface.
 */
public class FunctionException extends RuntimeException {

	private static final long serialVersionUID = 126730001007643670L;

	/**
	 * Constructs a new {@linkplain FunctionException} instance.
	 *
	 * @param cause the exception to wrap.
	 */
	public FunctionException(Exception cause) {
		super(cause);
	}

	/**
	 * Gets the wrapped exception for re-throwing.
	 *
	 * @param <T> the wrapped excption's type.
	 * @param exceptionType the wrapped exception type.
	 * @return the wrapped exception.
	 */
	public <T extends Exception> T rethrow(Class<T> exceptionType) {
		return exceptionType.cast(getCause());
	}

}
