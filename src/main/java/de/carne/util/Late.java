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
package de.carne.util;

import de.carne.check.Nullable;

/**
 * Utility class used to manage late initialized objects.
 *
 * @param <T> The managed object type.
 */
public final class Late<T> {

	@Nullable
	private T initializedObject = null;

	/**
	 * Initialized the hold object.
	 *
	 * @param object The object to set.
	 * @return The initialized object.
	 * @throws IllegalStateException If the object has already been initialized.
	 */
	public synchronized T initialize(T object) throws IllegalStateException {
		if (this.initializedObject != null) {
			throw new IllegalStateException("Already initialized");
		}
		return this.initializedObject = object;
	}

	/**
	 * Check whether the hold object has already been initialized (by call to {@link #initialize(Object)}).
	 *
	 * @return {@code true} if the object has already been initialized.
	 */
	public synchronized boolean isInitialized() {
		return this.initializedObject != null;
	}

	/**
	 * Get the hold object.
	 *
	 * @return The hold object.
	 * @throws IllegalStateException if the object has not yet been initialized.
	 */
	public synchronized T get() throws IllegalStateException {
		T object = this.initializedObject;

		if (object == null) {
			throw new IllegalStateException("Not yet initialized");
		}
		return object;
	}

}
