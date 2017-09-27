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

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.Lazy;

/**
 * Test {@link Lazy} class.
 */
public class LazyTest {

	/**
	 * Test valid {@link Supplier} and access.
	 */
	@Test
	public void testValidSupplierAccess() {
		Supplier<LazyTest> initializer = new Supplier<LazyTest>() {

			private boolean initialized = false;

			@Override
			public LazyTest get() {
				Assert.assertFalse(this.initialized);
				this.initialized = true;
				return LazyTest.this;
			}

		};
		Lazy<LazyTest> lazy = new Lazy<>(initializer);

		Assert.assertEquals("<not initialized>", lazy.toString());
		Assert.assertEquals(this, lazy.get());
		Assert.assertEquals(this, lazy.get());
		Assert.assertEquals(toString(), lazy.toString());
	}

	/**
	 * Test invalid {@link Supplier} and access.
	 */
	@Test(expected = NullPointerException.class)
	public void testInvalidSupplierAccess() {
		Supplier<LazyTest> initializer = () -> null;
		Lazy<LazyTest> lazy = new Lazy<>(initializer);

		Assert.assertEquals("<not initialized>", lazy.toString());
		Assert.assertEquals(this, lazy.get());
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

}
