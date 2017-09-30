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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.Exceptions;

/**
 * Test {@linkplain Exceptions} class.
 */
public class ExceptionsTest {

	/**
	 * Test {@linkplain Exceptions#toRuntime(Throwable)} with checked {@linkplain Exception}.
	 */
	@Test(expected = RuntimeException.class)
	public void testToRuntimeFromChecked() {
		try {
			throw new IOException();
		} catch (Exception e) {
			throw Exceptions.toRuntime(e);
		}
	}

	/**
	 * Test {@linkplain Exceptions#toRuntime(Throwable)} with unchecked {@linkplain Exception}.
	 */
	@Test(expected = IllegalStateException.class)
	public void testToRuntimeFromUnchecked() {
		try {
			throw new IllegalStateException();
		} catch (Exception e) {
			throw Exceptions.toRuntime(e);
		}
	}

	/**
	 * Test {@linkplain Exceptions#ignore(Throwable)}.
	 */
	@Test
	public void testIgnore() {
		Exceptions.ignore(null);
		Exceptions.ignore(new NullPointerException());
	}

	/**
	 * Test {@linkplain Exceptions#warn(Throwable)}.
	 */
	@Test
	public void testWarn() {
		Exceptions.warn(null);
		Exceptions.warn(new NullPointerException());
	}

	/**
	 * Test {@linkplain Exceptions#getStackTrace(Throwable)}.
	 */
	@Test
	public void testGetStackTrace() {
		String stackTrace = Exceptions.getStackTrace(new IOException());

		System.err.println(stackTrace);
		Assert.assertNotNull(stackTrace);
	}

}
