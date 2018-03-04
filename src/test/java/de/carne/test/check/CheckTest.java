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
package de.carne.test.check;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.check.Check;

/**
 * Test {@linkplain Check} class.
 */
class CheckTest {

	@Test
	void testCheckNotNullPassed() {
		Assertions.assertNotNull(Check.notNull(this));
	}

	@Test
	void testCheckNotNullMessagePassed() {
		Assertions.assertNotNull(Check.notNull(this, getClass().getSimpleName()));
	}

	@Test
	void testCheckNotNullFailed() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			Check.notNull(null);
		});
	}

	@Test
	void testCheckNotNullMessageFailed() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			Check.notNull(null, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckAssertTruePassed() {
		Check.assertTrue(true);
	}

	@Test
	void testCheckAssertTrueMessagePassed() {
		Check.assertTrue(true, getClass().getSimpleName());
	}

	@Test
	void testCheckAssertTrueFailed() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.assertTrue(false);
		});
	}

	@Test
	void testCheckAssertTrueMessageFailed() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.assertTrue(false, getClass().getSimpleName());
		});
	}

	@Test
	void testCheckFail() {
		Assertions.assertThrows(IllegalStateException.class, () -> Check.fail());
	}

	@Test
	void testCheckFailMessage() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Check.fail(getClass().getSimpleName());
		});
	}

}
