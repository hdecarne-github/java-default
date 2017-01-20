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

import org.junit.Test;

import de.carne.check.Check;
import de.carne.check.NonNullByDefault;

/**
 * Test {@link Check} class.
 */
@NonNullByDefault
public class CheckTest {

	private static final String CHECK_MESSAGE = "with message";

	/**
	 * Test all checks in success scenario.
	 */
	@Test
	public void testSuccess() {
		Check.nonNull(this);
		Check.nonNull(this, CHECK_MESSAGE);
		Check.condition(true);
		Check.condition(true, CHECK_MESSAGE);
	}

	/**
	 * Test {@link Check#nonNull(Object)} in failure scenario.
	 */
	@Test(expected = NullPointerException.class)
	public void testNonNullFailure1() {
		Check.nonNull(null);
	}

	/**
	 * Test {@link Check#nonNull(Object, String)} in failure scenario.
	 */
	@Test(expected = NullPointerException.class)
	public void testNonNullFailure2() {
		Check.nonNull(null, CHECK_MESSAGE);
	}

	/**
	 * Test {@link Check#condition(boolean)} in failure scenario.
	 */
	@Test(expected = IllegalStateException.class)
	public void testConditionFailure1() {
		Check.condition(false);
	}

	/**
	 * Test {@link Check#condition(boolean, String)} in failure scenario.
	 */
	@Test(expected = IllegalStateException.class)
	public void testConditionFailure2() {
		Check.condition(false, CHECK_MESSAGE);
	}

}
