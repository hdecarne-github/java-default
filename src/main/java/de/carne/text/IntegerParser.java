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
package de.carne.text;

/**
 * {@linkplain Integer} parser.
 */
public class IntegerParser implements Parser<Integer> {

	/**
	 * Standard parser for any integer value.
	 */
	public static final IntegerParser ANY = new IntegerParser();

	/**
	 * Standard parser for any positive integer value.
	 */
	public static final IntegerParser POSITIVE = new IntegerParser(0, Integer.MAX_VALUE);

	private final int min;
	private final int max;

	/**
	 * Constructs a new {@linkplain IntegerParser} accepting any integer value.
	 */
	public IntegerParser() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Constructs a new {@linkplain IntegerParser} accepting integer values of the given range.
	 *
	 * @param min the minimum integer value to accept.
	 * @param max the maximum integer value to accept.
	 */
	public IntegerParser(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Integer parse(String s) {
		return Integer.valueOf(parseInt(s));
	}

	/**
	 * Parses the submitted string.
	 *
	 * @param s the {@linkplain String} to parse.
	 * @return the parse result.
	 * @throws RuntimeException if the parse operation fails.
	 */
	public int parseInt(String s) {
		int i = Integer.parseInt(s);

		if (i < this.min || this.max < i) {
			throw new IllegalArgumentException(
					"Integer value " + i + " out of range [" + this.min + ", " + this.max + "]");
		}
		return i;
	}

}
