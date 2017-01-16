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
package de.carne.test.util.validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import de.carne.util.validation.PathValidator;
import de.carne.util.validation.ValidationException;

/**
 * Test {@link PathValidator} class.
 */
public class PathValidatorTest {

	private static final String VALIDATION_MESSAGE = PathValidatorTest.class.getSimpleName();

	/**
	 * Test all validations in success scenario.
	 *
	 * @throws ValidationException if a validation fails.
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testSuccess() throws ValidationException, IOException {
		Path testDirectory = Files.createTempDirectory(getClass().getSimpleName());

		try {
			PathValidator.isPath(testDirectory.toString(), (a) -> VALIDATION_MESSAGE);
			PathValidator.isPath(testDirectory, "test", (a) -> VALIDATION_MESSAGE);
			PathValidator.isReadableDirectory(testDirectory.toString(), (a) -> VALIDATION_MESSAGE);
			PathValidator.isWritableDirectory(testDirectory.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testDirectory);
		}

		Path testFile = Files.createTempFile(getClass().getSimpleName(), null);

		try {
			PathValidator.isReadableFile(testFile.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testFile);
		}
	}

}
