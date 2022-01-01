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
package de.carne.test.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.io.ConsoleWriter;

/**
 * Test {@linkplain ConsoleWriter} class.
 */
class ConsoleWriterTest {

	@Test
	void testAnsiEnabled() {
		System.setProperty(ConsoleWriter.class.getName() + ".forceAnsiOutput", Boolean.TRUE.toString());

		@SuppressWarnings("resource") ConsoleWriter cw = new ConsoleWriter();

		Assertions.assertTrue(cw.isAnsiEnabled());
	}

}
