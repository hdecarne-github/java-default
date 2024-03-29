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
package de.carne.util.validation;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Functional interface used to define an conversion step during an input validation.
 *
 * @param <I> the actual input type.
 * @param <O> the actual output type.
 */
@FunctionalInterface
public interface Conversion<I, O> {

	/**
	 * Checks the given input value and applies the conversion.
	 *
	 * @param input the input value to convert.
	 * @return the converted value or {@code null} if the input value is not valid.
	 * @throws Exception if the conversion fails.
	 */
	@SuppressWarnings("squid:S00112")
	@Nullable
	O checkAndApply(I input) throws Exception;

}
