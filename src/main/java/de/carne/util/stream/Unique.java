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
package de.carne.util.stream;

import java.util.Collections;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * {@linkplain Collector} implementation ensuring a one element collection at the end of a stream operation.
 *
 * @param <T> the actual stream element type.
 * @param <R> the actual result type (either T or Optional&lt;T&gt;).
 */
public final class Unique<T, R> implements Collector<T, UniqueLatch<T>, R> {

	private static final Set<Characteristics> CHARACTERISTICS = Collections
			.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED));

	private final Function<UniqueLatch<T>, R> finisher;

	private Unique(Function<UniqueLatch<T>, R> finisher) {
		this.finisher = finisher;
	}

	/**
	 * Collects and gets the unique result element.
	 *
	 * @return the unique result element.
	 * @throws NoSuchElementException if the stream operation result contains zero or more than one element.
	 * @param <T> the actual result type.
	 */
	public static <T> Unique<T, T> get() {
		return new Unique<>(UniqueLatch::get);
	}

	/**
	 * Collects and gets the unique result element as an {@linkplain Optional}.
	 *
	 * @return the unique result element.
	 * @param <T> the actual result type.
	 */
	public static <T> Unique<T, Optional<T>> getOptional() {
		return new Unique<>(UniqueLatch::getOptional);
	}

	@Override
	public Supplier<UniqueLatch<T>> supplier() {
		return UniqueLatch::new;
	}

	@Override
	public BiConsumer<UniqueLatch<T>, T> accumulator() {
		return UniqueLatch::accumulate;
	}

	@Override
	public BinaryOperator<UniqueLatch<T>> combiner() {
		return UniqueLatch::combine;
	}

	@Override
	public Function<UniqueLatch<T>, R> finisher() {
		return this.finisher;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return CHARACTERISTICS;
	}

}
