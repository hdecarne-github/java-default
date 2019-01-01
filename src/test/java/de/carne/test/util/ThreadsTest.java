/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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

import de.carne.util.Threads;

/**
 * Test {@linkplain Threads} class.
 */
class ThreadsTest {

	@Test
	void testInterrupted() throws InterruptedException {
		Threads.checkInterrupted();

		Thread.currentThread().interrupt();
		Assertions.assertThrows(InterruptedException.class, () -> {
			Threads.checkInterrupted();
		});
	}

	@Test
	void testSleep() throws InterruptedException {
		// Sleep through
		Assertions.assertTrue(Threads.sleep(250));

		// Interrupt sleept
		Thread sleepThread = new Thread(() -> {
			Thread currentThread = Thread.currentThread();

			synchronized (currentThread) {
				currentThread.notifyAll();
			}
			Assertions.assertFalse(Threads.sleep(1000));
			synchronized (currentThread) {
				currentThread.notifyAll();
			}
		});

		sleepThread.start();
		synchronized (sleepThread) {
			sleepThread.wait();
		}
		sleepThread.interrupt();
		synchronized (sleepThread) {
			while (sleepThread.isAlive()) {
				sleepThread.wait();
			}
		}
	}

}
