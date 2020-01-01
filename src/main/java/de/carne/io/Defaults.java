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
package de.carne.io;

import de.carne.text.IntegerParser;
import de.carne.util.SystemProperties;

/**
 * I/O default properties.
 */
public final class Defaults {

	private Defaults() {
		// prevent instantiation
	}

	/**
	 * {@linkplain #DEFAULT_BUFFER_SIZE} property.
	 */
	public static final String DEFAULT_BUFFER_SIZE_PROPERTY = Defaults.class.getPackage().getName()
			+ ".DEFAULT_BUFFER_SIZE";

	/**
	 * Default buffer size for I/O operations.
	 */
	public static final int DEFAULT_BUFFER_SIZE = SystemProperties.intValue(DEFAULT_BUFFER_SIZE_PROPERTY,
			IntegerParser.POSITIVE, 1 << 12);

	/**
	 * {@linkplain #MAX_BUFFER_SIZE} property.
	 */
	public static final String MAX_BUFFER_SIZE_PROPERTY = Defaults.class.getPackage().getName() + ".MAX_BUFFER_SIZE";

	/**
	 * Maximum buffer size for I/O operations.
	 */
	public static final int MAX_BUFFER_SIZE = SystemProperties.intValue(MAX_BUFFER_SIZE_PROPERTY,
			IntegerParser.POSITIVE, 1 << 22);

}
