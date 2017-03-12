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

import java.util.function.Supplier;

import de.carne.check.Nullable;

/**
 * Utility class used to manage lazy initialized objects.
 *
 * @param <T> The managed object type.
 */
public final class Lazy<T> {

	private final Supplier<T> initializer;

	@Nullable
	private T initializedObject = null;

	/**
	 * Construct {@code Lazy}.
	 *
	 * @param initializer The {@link Supplier} to use for object initialization.
	 */
	public Lazy(Supplier<T> initializer) {
		this.initializer = initializer;
	}

	/**
	 * Check whether the hold object has already been initialized (by a call to {@link #get()}).
	 *
	 * @return {@code true} if the object has already been initialized.
	 */
	public synchronized boolean isInitialized() {
		return this.initializedObject != null;
	}

	/**
	 * Get the hold object.
	 * <p>
	 * The object will be initialized the first time this function is invoked.
	 *
	 * @return The hold object.
	 */
	public synchronized T get() {
		T object = this.initializedObject;

		if (object == null) {
			object = this.initializedObject = this.initializer.get();
		}
		return object;
	}

	@Override
	public String toString() {
		T object = this.initializedObject;

		return (object != null ? object.toString() : "<not initialized>");
	}

}
