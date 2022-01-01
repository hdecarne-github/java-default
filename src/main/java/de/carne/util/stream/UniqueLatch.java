/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.util.stream;

import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;

@SuppressWarnings({ "squid:S2789", "squid:S3655" })
final class UniqueLatch<T> {

	private @Nullable Optional<T> optional;

	UniqueLatch() {
		this.optional = null;
	}

	T get() {
		return getOptional().get();
	}

	Optional<T> getOptional() {
		return (this.optional != null ? this.optional : Optional.empty());
	}

	static <T> void accumulate(UniqueLatch<T> latch, T value) {
		latch.optional = (latch.optional == null ? Optional.ofNullable(value) : Optional.empty());
	}

	static <T> UniqueLatch<T> combine(UniqueLatch<T> left, UniqueLatch<T> right) {
		return (left.optional == null ? right : left);
	}

}
