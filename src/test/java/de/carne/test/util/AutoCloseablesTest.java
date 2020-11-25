/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.AutoCloseables;

/**
 * Test {@linkplain AutoCloseables} class.
 */
class AutoCloseablesTest {

	private static final AutoCloseable FAILING_CLOSEABLE = () -> {
		throw new Exception();
	};

	@Test
	void testClose() throws Exception {
		AutoCloseables.close(null);
		AutoCloseables.close(this);

		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") AutoCloseable closeable = () -> closeCounter.incrementAndGet();

		AutoCloseables.close(closeable);

		Assertions.assertEquals(1, closeCounter.get());
		Assertions.assertThrows(Exception.class, () -> {
			AutoCloseables.close(FAILING_CLOSEABLE);
		});
	}

	@Test
	void testCloseAll() throws Exception {
		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") AutoCloseable closeable = () -> closeCounter.incrementAndGet();

		AutoCloseables.closeAll(closeable, null, closeable);

		Assertions.assertEquals(2, closeCounter.get());
		Assertions.assertThrows(Exception.class, () -> {
			AutoCloseables.closeAll(closeable, FAILING_CLOSEABLE, closeable, FAILING_CLOSEABLE);
		});
		Assertions.assertEquals(4, closeCounter.get());

		AutoCloseables.closeAll(Arrays.asList(closeable, closeable, closeable));

		Assertions.assertEquals(7, closeCounter.get());
		Assertions.assertThrows(Exception.class, () -> {
			AutoCloseables.closeAll(Arrays.asList(closeable, FAILING_CLOSEABLE, closeable, FAILING_CLOSEABLE));
		});
		Assertions.assertEquals(9, closeCounter.get());
	}

	@Test
	void testSafeClose() {
		AutoCloseables.safeClose(null);
		AutoCloseables.safeClose(this);
		AutoCloseables.safeClose(FAILING_CLOSEABLE);

		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") AutoCloseable closeable = () -> closeCounter.incrementAndGet();

		AutoCloseables.safeClose(closeable);

		Assertions.assertEquals(1, closeCounter.get());

		Exception outerException = new Exception();

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		AutoCloseables.safeClose(outerException, null);

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		AutoCloseables.safeClose(outerException, this);

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		AutoCloseables.safeClose(outerException, closeable);

		Assertions.assertEquals(2, closeCounter.get());
		Assertions.assertEquals(0, outerException.getSuppressed().length);

		AutoCloseables.safeClose(outerException, FAILING_CLOSEABLE);

		Assertions.assertEquals(1, outerException.getSuppressed().length);
	}

}
