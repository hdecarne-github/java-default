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
package de.carne.text;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import de.carne.boot.check.Check;
import de.carne.boot.check.Nullable;

/**
 * {@linkplain NumberFormat} for memory units (byte, KiB, ...).
 */
public class MemoryUnitFormat extends NumberFormat {

	// Serialization support
	private static final long serialVersionUID = -9087489563589331679L;

	private static final String[] UNITS = { " byte", " KiB", " MiB", " GiB", " TiB", " PiB", " EiB", " ZiB", " YiB" };

	private static final ThreadLocal<MemoryUnitFormat> CACHED_INSTANCE = ThreadLocal.withInitial(MemoryUnitFormat::new);

	private final NumberFormat numberFormat;

	/**
	 * Constructs a new {@linkplain MemoryUnitFormat} instance.
	 */
	public MemoryUnitFormat() {
		this(NumberFormat.getNumberInstance());
	}

	/**
	 * Constructs a new {@linkplain MemoryUnitFormat} instance.
	 *
	 * @param inLocale the {@linkplain Locale} to use for formatting and parsing.
	 */
	public MemoryUnitFormat(Locale inLocale) {
		this(NumberFormat.getNumberInstance(inLocale));
	}

	/**
	 * Constructs a new {@linkplain MemoryUnitFormat} instance.
	 *
	 * @param numberFormat the {@linkplain NumberFormat} to use for formatting and parsing.
	 */
	public MemoryUnitFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * Gets the default {@linkplain MemoryUnitFormat} instance.
	 * 
	 * @return the default {@linkplain MemoryUnitFormat} instance.
	 */
	public static MemoryUnitFormat getMemoryUnitInstance() {
		return CACHED_INSTANCE.get();
	}

	@Override
	public StringBuffer format(double number, @Nullable StringBuffer toAppendTo, @Nullable FieldPosition pos) {
		int unitIndex = 0;
		double normalizedNumber = Math.abs(number);

		while (normalizedNumber >= 1024.0 && (unitIndex + 1) < UNITS.length) {
			normalizedNumber /= 1024.0;
			unitIndex++;
		}
		normalizedNumber *= Math.signum(number);
		return this.numberFormat.format(normalizedNumber, toAppendTo, pos).append(UNITS[unitIndex]);
	}

	@Override
	public StringBuffer format(long number, @Nullable StringBuffer toAppendTo, @Nullable FieldPosition pos) {
		int unitIndex = 0;
		long normalizedNumber = Math.abs(number);

		while (normalizedNumber > (1 << 12) && (unitIndex + 1) < UNITS.length) {
			normalizedNumber >>= 10;
			unitIndex++;
		}
		normalizedNumber *= (number >= 0 ? 1 : -1);
		return this.numberFormat.format(normalizedNumber, toAppendTo, pos).append(UNITS[unitIndex]);
	}

	@Override
	@Nullable
	public Number parse(@Nullable String source, @Nullable ParsePosition parsePosition) {
		ParsePosition checkedParsePosition = Check.notNull(parsePosition);
		int initialParseIndex = checkedParsePosition.getIndex();
		Number number = this.numberFormat.parse(source, checkedParsePosition);

		if (checkedParsePosition.getErrorIndex() < 0) {
			if (number instanceof Long) {
				number = parseLong((Long) number, Check.notNull(source), checkedParsePosition, initialParseIndex);
			} else if (number instanceof Double) {
				number = parseDouble((Double) number, Check.notNull(source), checkedParsePosition, initialParseIndex);
			} else {
				checkedParsePosition.setErrorIndex(checkedParsePosition.getIndex());
				checkedParsePosition.setIndex(initialParseIndex);
				number = null;
			}
		}
		return number;
	}

	@Nullable
	private Long parseLong(Long longNumber, String source, ParsePosition parsePosition, int initialParseIndex) {
		long longValue = longNumber.longValue();
		int parseIndex = parsePosition.getIndex();
		Long parseResult;

		for (int unitIndex = 0; unitIndex < UNITS.length; unitIndex++) {
			String unit = UNITS[unitIndex];

			if (source.startsWith(unit, parseIndex)) {
				parsePosition.setIndex(parseIndex + unit.length());
				break;
			}
			longValue <<= 10;
		}
		if (parseIndex < parsePosition.getIndex()) {
			parseResult = longValue;
		} else {
			parsePosition.setErrorIndex(parseIndex);
			parsePosition.setIndex(initialParseIndex);
			parseResult = null;
		}
		return parseResult;
	}

	@Nullable
	private Double parseDouble(Double doubleNumber, String source, ParsePosition parsePosition, int initialParseIndex) {
		double doubleValue = doubleNumber.doubleValue();
		int parseIndex = parsePosition.getIndex();
		Double parseResult;

		for (int unitIndex = 0; unitIndex < UNITS.length; unitIndex++) {
			String unit = UNITS[unitIndex];

			if (source.startsWith(unit, parseIndex)) {
				parsePosition.setIndex(parseIndex + unit.length());
				break;
			}
			doubleValue *= 1024.0;
		}
		if (parseIndex < parsePosition.getIndex()) {
			parseResult = doubleValue;
		} else {
			parsePosition.setErrorIndex(parseIndex);
			parsePosition.setIndex(initialParseIndex);
			parseResult = null;
		}
		return parseResult;
	}

	@Override
	public int hashCode() {
		return this.numberFormat.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return obj instanceof MemoryUnitFormat && this.numberFormat.equals(((MemoryUnitFormat) obj).numberFormat);
	}

}
