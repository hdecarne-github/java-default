/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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

/**
 * {@link NumberFormat} implementation supporting formatting and parsing of
 * memory unit numbers.
 */
public class MemUnitFormat extends NumberFormat {

	/**
	 * Serialization support.
	 */
	private static final long serialVersionUID = -2340676521583361828L;

	private static final String[] MEM_UNITS = new String[] {

			" Byte", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB"

	};

	private NumberFormat numberFormat;

	/**
	 * Construct {@code MemUnitFormat}.
	 *
	 * @param numberFormat The {@link NumberFormat} to use for the actual number
	 *        formatting and parsing.
	 */
	public MemUnitFormat(NumberFormat numberFormat) {
		assert numberFormat != null;

		this.numberFormat = numberFormat;
	}

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		double normalizedNumber = number;
		int memUnitIndex = 0;

		if (Double.isFinite(number)) {
			while (Math.abs(normalizedNumber) >= 1024.0 && memUnitIndex < MEM_UNITS.length) {
				normalizedNumber /= 1024.0;
				memUnitIndex++;
			}
		}
		return this.numberFormat.format(normalizedNumber, toAppendTo, pos).append(MEM_UNITS[memUnitIndex]);
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return format(number * 1.0, toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		int startIndex = parsePosition.getIndex();
		Number number = this.numberFormat.parse(source, parsePosition);

		if (parsePosition.getIndex() > startIndex && parsePosition.getErrorIndex() < 0) {
			double doubleNumber = number.doubleValue();
			int memUnitIndex = 0;

			if (Double.isFinite(doubleNumber)) {
				while (memUnitIndex < MEM_UNITS.length
						&& !source.startsWith(MEM_UNITS[memUnitIndex], parsePosition.getIndex())) {
					memUnitIndex++;
					doubleNumber *= 1024.0;
				}
			} else if (source.startsWith(MEM_UNITS[memUnitIndex], parsePosition.getIndex())) {
				memUnitIndex = MEM_UNITS.length;
			}
			if (memUnitIndex < MEM_UNITS.length) {
				String memUnit = MEM_UNITS[memUnitIndex];

				parsePosition.setIndex(parsePosition.getIndex() + memUnit.length());
				if (Math.floor(doubleNumber) == doubleNumber && Long.MIN_VALUE <= doubleNumber
						&& doubleNumber <= Long.MAX_VALUE) {
					number = Long.valueOf((long) doubleNumber);
				} else {
					number = Double.valueOf(doubleNumber).longValue();
				}
			} else {
				parsePosition.setErrorIndex(parsePosition.getIndex());
				parsePosition.setIndex(startIndex);
			}
		}
		return number;
	}

}
