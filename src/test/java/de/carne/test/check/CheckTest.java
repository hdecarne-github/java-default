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
package de.carne.test.check;

import org.junit.Assert;
import org.junit.Test;

import de.carne.check.Check;

/**
 * Test {@link Check} class.
 */
public class CheckTest {

	/**
	 * Check {@link Check#notNull(Object)} with non {@code null} argument.
	 */
	@Test
	public void checkNotNullPassed() {
		Assert.assertNotNull(Check.notNull(this));
	}

	/**
	 * Check {@link Check#notNull(Object, String)} with non {@code null} argument.
	 */
	@Test
	public void checkNotNullMessagePassed() {
		Assert.assertNotNull(Check.notNull(this, getClass().getSimpleName()));
	}

	/**
	 * Check {@link Check#notNull(Object)} with {@code null} argument.
	 */
	@Test(expected = NullPointerException.class)
	public void checkNotNullFailed() {
		Check.notNull(null);
	}

	/**
	 * Check {@link Check#notNull(Object, String)} with {@code null} argument.
	 */
	@Test(expected = NullPointerException.class)
	public void checkNotNullMessageFailed() {
		Check.notNull(null, getClass().getSimpleName());
	}

	/**
	 * Check {@link Check#assertTrue(boolean)} with {@code true} argument.
	 */
	@Test
	public void checkAssertTruePassed() {
		Check.assertTrue(true);
	}

	/**
	 * Check {@link Check#assertTrue(boolean, String)} with {@code true} argument.
	 */
	@Test
	public void checkAssertTrueMessagePassed() {
		Check.assertTrue(true, getClass().getSimpleName());
	}

	/**
	 * Check {@link Check#assertTrue(boolean)} with {@code false} argument.
	 */
	@Test(expected = IllegalStateException.class)
	public void checkAssertTrueFailed() {
		Check.assertTrue(false);
	}

	/**
	 * Check {@link Check#assertTrue(boolean, String)} with {@code false} argument.
	 */
	@Test(expected = IllegalStateException.class)
	public void checkAssertTrueMessageFailed() {
		Check.assertTrue(false, getClass().getSimpleName());
	}

	/**
	 * Check {@link Check#fail()}.
	 */
	@Test(expected = IllegalStateException.class)
	public void checkFail() {
		Check.fail();
	}

	/**
	 * Check {@link Check#fail(String)}.
	 */
	@Test(expected = IllegalStateException.class)
	public void checkFailMessage() {
		Check.fail(getClass().getSimpleName());
	}

}
