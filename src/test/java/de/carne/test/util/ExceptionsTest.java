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

import de.carne.check.NonNullByDefault;
import de.carne.util.Exceptions;

/**
 * Test {@link Exceptions} class.
 */
@NonNullByDefault
public class ExceptionsTest {

	/**
	 * Test {@link Exceptions#getStackTrace(Throwable)} function.
	 */
	@Test
	public void testStacktrace() {
		String stacktrace = Exceptions.getStackTrace(new Throwable());

		System.err.println(stacktrace);
		Assert.assertTrue(stacktrace.startsWith(Throwable.class.getName()));
	}

	/**
	 * Test {@link Exceptions#ignore(Throwable)} function.
	 */
	@Test
	public void testIgnore() {
		Exceptions.ignore(new Throwable());
		Exceptions.ignore(null);
	}

	/**
	 * Test {@link Exceptions#warn(Throwable)} function.
	 */
	@Test
	public void testWarn() {
		Exceptions.warn(new Throwable());
		Exceptions.warn(null);
	}

	/**
	 * Test {@link Exceptions#toRuntime(Throwable)} function.
	 */
	@Test(expected = RuntimeException.class)
	public void testToRuntime() {
		throw Exceptions.toRuntime(new Exception());
	}

}
