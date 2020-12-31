/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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
 * Functional interface used to define input validation steps.
 *
 * @param <I> the actual input type.
 */
@FunctionalInterface
public interface Validation<I> {

	/**
	 * Checks whether the given input is valid.
	 *
	 * @param input the input value to check.
	 * @return {@code true} if the input valid, {@code false} otherwise.
	 * @throws Exception if the validation fails.
	 */
	@SuppressWarnings("squid:S00112")
	boolean check(I input) throws Exception;

}
