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
package de.carne.test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import de.carne.text.MemUnitFormat;

/**
 * Test {@link MemUnitFormat} class functionality.
 */
public class MemUnitFormatTest {

	/**
	 * Test memory unit formatting and parsing.
	 */
	@Test
	public void testFormatAndParse() {
		MemUnitFormat format = new MemUnitFormat(NumberFormat.getNumberInstance(Locale.GERMANY));
		Number numberZero = 0.0;
		Number numberOne = 1.0;
		Number number1k = numberOne.doubleValue() * 1024.0;
		Number number1m = number1k.doubleValue() * 1024.0;
		Number number1g = number1m.doubleValue() * 1024.0;
		Number number1t = number1g.doubleValue() * 1024.0;
		Number number1p = number1t.doubleValue() * 1024.0;

		String numberZeroString = format.format(numberZero);
		String numberOneString = format.format(numberOne);
		String number1kString = format.format(number1k);
		String number1mString = format.format(number1m);
		String number1gString = format.format(number1g);
		String number1tString = format.format(number1t);
		String number1pString = format.format(number1p);

		try {
			Assert.assertEquals(numberZero, format.parse(numberZeroString).doubleValue());
			Assert.assertEquals(numberOne, format.parse(numberOneString).doubleValue());
			Assert.assertEquals(number1k, format.parse(number1kString).doubleValue());
			Assert.assertEquals(number1m, format.parse(number1mString).doubleValue());
			Assert.assertEquals(number1g, format.parse(number1gString).doubleValue());
			Assert.assertEquals(number1t, format.parse(number1tString).doubleValue());
			Assert.assertEquals(number1p, format.parse(number1pString).doubleValue());
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
	}

}
