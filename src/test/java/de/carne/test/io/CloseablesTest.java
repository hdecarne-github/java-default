/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.io.Closeables;

/**
 * Test {@linkplain Closeables} class.
 */
class CloseablesTest {

	private static final Closeable FAILING_CLOSEABLE = () -> {
		throw new IOException();
	};

	@Test
	void testClose() throws IOException {
		Closeables.close(null);
		Closeables.close(this);

		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") Closeable closeable = () -> closeCounter.incrementAndGet();

		Closeables.close(closeable);

		Assertions.assertEquals(1, closeCounter.get());
		Assertions.assertThrows(IOException.class, () -> {
			Closeables.close(FAILING_CLOSEABLE);
		});
	}

	@Test
	void testCloseAll() throws IOException {
		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") Closeable closeable = () -> closeCounter.incrementAndGet();

		Closeables.closeAll(closeable, null, closeable);

		Assertions.assertEquals(2, closeCounter.get());
		Assertions.assertThrows(IOException.class, () -> {
			Closeables.closeAll(closeable, FAILING_CLOSEABLE, closeable, FAILING_CLOSEABLE);
		});
		Assertions.assertEquals(4, closeCounter.get());

		Closeables.closeAll(Arrays.asList(closeable, closeable, closeable));

		Assertions.assertEquals(7, closeCounter.get());
		Assertions.assertThrows(IOException.class, () -> {
			Closeables.closeAll(Arrays.asList(closeable, FAILING_CLOSEABLE, closeable, FAILING_CLOSEABLE));
		});
		Assertions.assertEquals(9, closeCounter.get());
	}

	@Test
	void testSafeClose() {
		Closeables.safeClose(null);
		Closeables.safeClose(this);

		final AtomicInteger closeCounter = new AtomicInteger();
		@SuppressWarnings("resource") Closeable closeable = () -> closeCounter.incrementAndGet();

		Closeables.safeClose(closeable);

		Assertions.assertEquals(1, closeCounter.get());
		Closeables.safeClose(FAILING_CLOSEABLE);

		IOException outerException = new IOException();

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		Closeables.safeClose(outerException, null);

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		Closeables.safeClose(outerException, this);

		Assertions.assertEquals(0, outerException.getSuppressed().length);

		Closeables.safeClose(outerException, closeable);

		Assertions.assertEquals(2, closeCounter.get());
		Assertions.assertEquals(0, outerException.getSuppressed().length);

		Closeables.safeClose(outerException, FAILING_CLOSEABLE);

		Assertions.assertEquals(1, outerException.getSuppressed().length);
	}

}
