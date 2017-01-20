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
 * Utility class used to manage lazy initialized resources.
 *
 * @param <T> The resource object type.
 */
public final class ObjectHolder<T> {

	private final Supplier<T> objectSupplier;

	@Nullable
	private T object = null;

	/**
	 * Construct {@code ObjectHolder}.
	 *
	 * @param objectSupplier The {@link Supplier} to use for resource object creation.
	 */
	public ObjectHolder(Supplier<T> objectSupplier) {
		this.objectSupplier = objectSupplier;
	}

	/**
	 * Get the resource object managed by this class.
	 * <p>
	 * The resource object will be created the first time this function is invoked.
	 *
	 * @return The resource object.
	 */
	@Nullable
	public synchronized T get() {
		if (this.object == null) {
			this.object = this.objectSupplier.get();
		}
		return this.object;
	}

}
