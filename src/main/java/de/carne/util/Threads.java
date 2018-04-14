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
package de.carne.util;

import de.carne.boot.Exceptions;

/**
 * Utility class providing {@linkplain Thread} related functions.
 */
public final class Threads {

	private Threads() {
		// Prevent instantiation
	}

	/**
	 * Checks whether the current {@linkplain Thread} has been interrupted.
	 *
	 * @throws InterruptedException if the current {@linkplain Thread} has been interrupted.
	 */
	public static void checkInterrupted() throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
	}

	/**
	 * Causes the current {@linkplain Thread} to sleep.
	 * <p>
	 * In addition to {@linkplain Thread#sleep(long)} this function automatically handles possible
	 * {@linkplain InterruptedException}s by handling and discarding the exception.
	 *
	 * @param millis the sleep timeout.
	 * @return {@code true} if the sleep operation was not interrupted.
	 * @see Thread#sleep(long)
	 */
	public static boolean sleep(long millis) {
		boolean interrupted = false;

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Exceptions.ignore(e);
			Thread.currentThread().interrupt();
			interrupted = true;
		}
		return !interrupted;
	}

}
