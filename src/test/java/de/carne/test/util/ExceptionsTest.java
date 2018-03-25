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

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.Exceptions;

/**
 * Test {@linkplain Exceptions} class.
 */
class ExceptionsTest {

	@Test
	void testToRuntimeFromChecked() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			try {
				throw new IOException();
			} catch (Exception e) {
				throw Exceptions.toRuntime(e);
			}
		});
	}

	@Test
	void testToRuntimeFromUnchecked() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			try {
				throw new IllegalStateException();
			} catch (Exception e) {
				throw Exceptions.toRuntime(e);
			}
		});
	}

	@Test
	void testIgnore() {
		Exceptions.ignore(null);
		Exceptions.ignore(new NullPointerException());
	}

	@Test
	void testWarn() {
		Exceptions.warn(null);
		Exceptions.warn(new NullPointerException());
	}

	@Test
	void testGetStackTrace() {
		String stackTrace = Exceptions.getStackTrace(new IOException());

		System.err.println(stackTrace);
		Assertions.assertNotNull(stackTrace);
	}

	@Test
	void testToString() {
		Throwable noMessageException = new IOException();

		Assertions.assertEquals(noMessageException.getClass().getName(), Exceptions.toString(noMessageException));

		String message = "An error occurred";
		Throwable messageException = new IOException(message);

		Assertions.assertEquals(messageException.getClass().getName() + ": " + message,
				Exceptions.toString(messageException));
	}

}
