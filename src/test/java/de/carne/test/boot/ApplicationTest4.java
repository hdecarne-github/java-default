/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.boot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.carne.boot.Application;
import de.carne.boot.ApplicationInitializationException;

/**
 * Test {@linkplain Application} class.
 */
class ApplicationTest4 {

	@BeforeAll
	static void setUpApplication() {
		System.setProperty("de.carne.boot.Application", "test4");
		System.setProperty("de.carne.boot.Application.debug", "true");
	}

	private static final String[] TEST_ARGS = new String[] {};

	@Test
	void testFailure() {
		// Fail due to invalid main class in application configuration
		ApplicationInitializationException exception = Assertions.assertThrows(ApplicationInitializationException.class,
				() -> {
					Application.run(TEST_ARGS);
				});

		Assertions.assertEquals(
				"Failed to load & instantiate application main class: de.carne.test.boot.ApplicationTest4Main",
				exception.getMessage());
	}

}
