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
package de.carne.test.util.stream;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.stream.Unique;

/**
 * Test {@linkplain Unique} class.
 */
class UniqueTest {

	@Test
	void testUnique() {
		String[] elements = new String[] { "1" };

		String unique = Arrays.asList(elements).stream().collect(Unique.get());

		Assertions.assertEquals(elements[0], unique);

		Optional<String> optionalUnique = Arrays.asList(elements).stream().collect(Unique.getOptional());

		Assertions.assertTrue(optionalUnique.isPresent());
		Assertions.assertEquals(elements[0], optionalUnique.get());
	}

	@Test
	void testEmpty() {
		@NonNull String[] elements = new @NonNull String[] {};
		Stream<String> elementsStream = Arrays.asList(elements).stream();
		Unique<String, String> uniqueCollector = Unique.get();

		Assertions.assertThrows(NoSuchElementException.class, () -> elementsStream.collect(uniqueCollector));
		Assertions.assertFalse(Arrays.asList(elements).stream().collect(Unique.getOptional()).isPresent());
	}

	@Test
	void testNonUnique() {
		@NonNull String[] elements = new @NonNull String[] { "1", "2" };
		Stream<String> elementsStream = Arrays.asList(elements).stream();
		Unique<String, String> uniqueCollector = Unique.get();

		Assertions.assertThrows(NoSuchElementException.class, () -> elementsStream.collect(uniqueCollector));
		Assertions.assertFalse(Arrays.asList(elements).stream().collect(Unique.getOptional()).isPresent());
	}

}
