/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.util;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.Exceptions;
import de.carne.util.Late;

/**
 * Test {@link Late} class.
 */
public class LateTest {

	/**
	 * Test {@link Late} class functionality.
	 */
	@Test
	public void testLazy() {
		Late<Object> lateObject = new Late<>();

		System.out.println(lateObject);

		Assert.assertFalse(lateObject.isInitialized());
		Assert.assertNull(lateObject.getIfInitialized());

		try {
			lateObject.get();
			Assert.fail();
		} catch (IllegalStateException e) {
			Exceptions.ignore(e);
		}

		Object object = lateObject.initialize(new Object());

		System.out.println(lateObject);

		Assert.assertTrue(lateObject.isInitialized());

		try {
			lateObject.get();
		} catch (IllegalStateException e) {
			Exceptions.ignore(e);
			Assert.fail();
		}

		Assert.assertTrue(object == lateObject.get());
		Assert.assertTrue(object == lateObject.getIfInitialized());
	}

}
