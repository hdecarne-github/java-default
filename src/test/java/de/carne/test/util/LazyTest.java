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
package de.carne.test.util;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.Lazy;

/**
 * Test {@linkplain Lazy} class.
 */
class LazyTest {

	@Test
	void testValidSupplierAccess() {
		Supplier<LazyTest> initializer = new Supplier<LazyTest>() {

			private boolean initialized = false;

			@Override
			public LazyTest get() {
				Assertions.assertFalse(this.initialized);
				this.initialized = true;
				return LazyTest.this;
			}

		};

		Lazy<LazyTest> lazy = new Lazy<>(initializer);

		Assertions.assertFalse(lazy.toOptional().isPresent());
		Assertions.assertEquals("<not initialized>", lazy.toString());
		Assertions.assertEquals(this, lazy.get());
		Assertions.assertTrue(lazy.toOptional().isPresent());
		Assertions.assertEquals(this, lazy.get());
		Assertions.assertEquals(toString(), lazy.toString());
	}

	@Test
	void testInvalidSupplierAccess() {
		@SuppressWarnings("null")
		Supplier<LazyTest> initializer = () -> null;
		Lazy<LazyTest> lazy = new Lazy<>(initializer);

		Assertions.assertFalse(lazy.toOptional().isPresent());
		Assertions.assertEquals("<not initialized>", lazy.toString());
		Assertions.assertThrows(NullPointerException.class, () -> {
			lazy.get();
		});
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

}
