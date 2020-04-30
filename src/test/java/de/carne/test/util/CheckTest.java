/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.Check;

/**
 * Test {@linkplain Check} class.
 */
class CheckTest {

	private static final String MESSAGE_PATTERN = "Check failed: {0}";

	@Test
	void testCheckIsInstanceOf() {
		Assertions.assertEquals(this, Check.isInstanceOf(this, CheckTest.class));
		Assertions.assertEquals(this, Check.isInstanceOf(this, CheckTest.class, getClass().getSimpleName()));
		Assertions.assertThrows(NullPointerException.class, () -> {
			Check.isInstanceOf(null, String.class);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Check.isInstanceOf(this, String.class);
		});
		Assertions.assertThrows(NullPointerException.class, () -> {
			Check.isInstanceOf(null, String.class, MESSAGE_PATTERN, getClass().getSimpleName());
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Check.isInstanceOf(this, String.class, MESSAGE_PATTERN, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckIsTrue() {
		Check.isTrue(true);
		Check.isTrue(true, getClass().getSimpleName());
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Check.isTrue(false);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Check.isTrue(false, getClass().getSimpleName());
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Check.isTrue(false, MESSAGE_PATTERN, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckAssertTrue() {
		Check.assertTrue(true);
		Check.assertTrue(true, getClass().getSimpleName());
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.assertTrue(false);
		});
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.assertTrue(false, getClass().getSimpleName());
		});
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.assertTrue(false, MESSAGE_PATTERN, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckFail() {
		Assertions.assertThrows(IllegalStateException.class, () -> Check.fail());
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.fail(getClass().getSimpleName());
		});
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.fail(MESSAGE_PATTERN, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckUnexpected() {
		Assertions.assertThrows(IllegalStateException.class, () -> Check.unexpected(this));
	}

}
