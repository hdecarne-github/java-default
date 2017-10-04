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
 * Utility class used to handle late initialized objects in a {@code null}-safe way.
 *
 * @param <T> The actual object type.
 */
public class Late<T> implements Supplier<T> {

	@Nullable
	private T object = null;

	/**
	 * Set/initialize the object.
	 *
	 * @param object The object to set.
	 * @return The set object.
	 */
	public synchronized T set(T object) {
		this.object = object;
		return object;
	}

	/**
	 * Get the object.
	 * <p>
	 * If the object has not yet been set/initialized an {@linkplain IllegalStateException} will be thrown.
	 *
	 * @return The object.
	 */
	@Override
	public synchronized T get() {
		T checkedObject = this.object;

		if (checkedObject == null) {
			throw new IllegalStateException("Not initialized");
		}
		return checkedObject;
	}

	@Override
	public String toString() {
		T checkedObject = this.object;

		return (checkedObject != null ? checkedObject.toString() : "<not initialized>");
	}

}
