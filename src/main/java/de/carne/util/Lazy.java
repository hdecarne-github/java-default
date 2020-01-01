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
package de.carne.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class used to handle lazy initialized objects in a {@code null}-safe way.
 *
 * @param <T> the actual object type.
 */
public class Lazy<@NonNull T> implements Supplier<T> {

	private final Supplier<T> initializer;

	private @Nullable T object = null;

	/**
	 * Construct {@linkplain Lazy}.
	 *
	 * @param initializer The {@linkplain Supplier} to use for object initialization.
	 */
	public Lazy(Supplier<T> initializer) {
		this.initializer = initializer;
	}

	/**
	 * Get the object.
	 * <p>
	 * The actual object will be created lazily the first time this function is invoked.
	 *
	 * @return The object.
	 */
	@Override
	public synchronized T get() {
		@Nullable T checkedObject = this.object;

		if (checkedObject == null) {
			this.object = checkedObject = Objects.requireNonNull(this.initializer.get());
		}
		return checkedObject;
	}

	/**
	 * Gets the object as an {@linkplain Optional}.
	 *
	 * @return the object as an {@linkplain Optional}.
	 */
	public Optional<T> getOptional() {
		return Optional.ofNullable(this.object);
	}

	@Override
	public String toString() {
		return Strings.safe(Objects.toString(this.object, "<not initialized>"));
	}

}
