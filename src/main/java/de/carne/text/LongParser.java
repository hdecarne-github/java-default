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

/**
 * {@linkplain Long} parser.
 */
public class LongParser implements Parser<Long> {

	/**
	 * Standard parser for any long value.
	 */
	public static final LongParser ANY = new LongParser();

	/**
	 * Standard parser for any positive long value.
	 */
	public static final LongParser POSITIVE = new LongParser(0, Long.MAX_VALUE);

	private final long min;
	private final long max;

	/**
	 * Constructs a new {@linkplain LongParser} accepting any long value.
	 */
	public LongParser() {
		this(Long.MIN_VALUE, Long.MAX_VALUE);
	}

	/**
	 * Constructs a new {@linkplain LongParser} accepting long values of the given range.
	 *
	 * @param min the minimum long value to accept.
	 * @param max the maximum long value to accept.
	 */
	public LongParser(long min, long max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Long parse(String s) {
		return Long.valueOf(parseLong(s));
	}

	/**
	 * Parses the submitted string.
	 *
	 * @param s the {@linkplain String} to parse.
	 * @return the parse result.
	 * @throws RuntimeException if the parse operation fails.
	 */
	public long parseLong(String s) {
		long l = Long.parseLong(s);

		if (l < this.min || this.max < l) {
			throw new IllegalArgumentException(
					"Integer value " + l + " out of range [" + this.min + ", " + this.max + "]");
		}
		return l;
	}

}
