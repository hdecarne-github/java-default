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
package de.carne.text;

import java.util.function.Function;

/**
 * Generic parser interface for mapping a {@linkplain String} to a specific type {@code T}.
 *
 * @param <T> the actual type to map to.
 */
public interface Parser<T> extends Function<String, T> {

	/**
	 * Parses the submitted string.
	 *
	 * @param s the {@linkplain String} to parse.
	 * @return the parse result.
	 * @throws RuntimeException if the parse operation fails.
	 */
	T parse(String s);

	@Override
	default T apply(String s) {
		return parse(s);
	}

}
