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

import java.util.Optional;
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
	 * Sets/initializes the object.
	 *
	 * @param object the object to set.
	 * @return the set object.
	 */
	public synchronized T set(T object) {
		if (this.object != null) {
			throw new IllegalStateException("Already initialized");
		}
		this.object = object;
		return object;
	}

	/**
	 * Gets the object.
	 * <p>
	 * If the object has not yet been set/initialized an {@linkplain IllegalStateException} will be thrown.
	 *
	 * @return the object.
	 */
	@Override
	public synchronized T get() {
		T checkedObject = this.object;

		if (checkedObject == null) {
			throw new IllegalStateException("Not initialized");
		}
		return checkedObject;
	}

	/**
	 * Wraps the object in an {@linkplain Optional}.
	 *
	 * @return the {@linkplain Optional} wrapped object.
	 */
	public Optional<T> toOptional() {
		return Optional.ofNullable(this.object);
	}

	@Override
	public String toString() {
		T checkedObject = this.object;

		return (checkedObject != null ? checkedObject.toString() : "<not initialized>");
	}

}
