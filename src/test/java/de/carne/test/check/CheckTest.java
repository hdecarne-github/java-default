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
 * Test {@linkplain Check} class.
 */
public class CheckTest {

	/**
	 * Test {@linkplain Check#notNull(Object)} with non {@code null} argument.
	 */
	@Test
	public void testCheckNotNullPassed() {
		Assert.assertNotNull(Check.notNull(this));
	}

	/**
	 * Test {@linkplain Check#notNull(Object, String)} with non {@code null} argument.
	 */
	@Test
	public void testCheckNotNullMessagePassed() {
		Assert.assertNotNull(Check.notNull(this, getClass().getSimpleName()));
	}

	/**
	 * Test {@linkplain Check#notNull(Object)} with {@code null} argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testCheckNotNullFailed() {
		Check.notNull(null);
	}

	/**
	 * Test {@linkplain Check#notNull(Object, String)} with {@code null} argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testCheckNotNullMessageFailed() {
		Check.notNull(null, getClass().getSimpleName());
	}

	/**
	 * Test {@linkplain Check#assertTrue(boolean)} with {@code true} argument.
	 */
	@Test
	public void testCheckAssertTruePassed() {
		Check.assertTrue(true);
	}

	/**
	 * Test {@linkplain Check#assertTrue(boolean, String)} with {@code true} argument.
	 */
	@Test
	public void testCheckAssertTrueMessagePassed() {
		Check.assertTrue(true, getClass().getSimpleName());
	}

	/**
	 * Test {@linkplain Check#assertTrue(boolean)} with {@code false} argument.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCheckAssertTrueFailed() {
		Check.assertTrue(false);
	}

	/**
	 * Test {@linkplain Check#assertTrue(boolean, String)} with {@code false} argument.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCheckAssertTrueMessageFailed() {
		Check.assertTrue(false, getClass().getSimpleName());
	}

	/**
	 * Test {@linkplain Check#fail()}.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCheckFail() {
		Check.fail();
	}

	/**
	 * Test {@linkplain Check#fail(String)}.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCheckFailMessage() {
		Check.fail(getClass().getSimpleName());
	}

}
