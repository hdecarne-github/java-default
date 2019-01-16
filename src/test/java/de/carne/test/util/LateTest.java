/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.util;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.Late;

/**
 * Test {@linkplain Late} class.
 */
class LateTest {

	@Test
	void testInitializedAccess() {
		Late<@NonNull LateTest> late = new Late<>();

		Assertions.assertEquals("<not initialized>", late.toString());

		late.set(() -> this);

		Assertions.assertTrue(late.getOptional().isPresent());
		Assertions.assertEquals(toString(), late.toString());
		Assertions.assertEquals(this, late.get());
	}

	@Test
	void testInvalidAccess() {
		Late<@NonNull LateTest> late = new Late<>();

		Assertions.assertFalse(late.getOptional().isPresent());
		Assertions.assertEquals("<not initialized>", late.toString());
		Assertions.assertThrows(IllegalStateException.class, () -> {
			late.get();
		});
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

}
