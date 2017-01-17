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
import java.nio.file.Paths;

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
			PathValidator.isDirectoryPath(testDirectory.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testDirectory);
		}

		Path testFile = Files.createTempFile(getClass().getSimpleName(), null);

		try {
			PathValidator.isPath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
			PathValidator.isRegularFilePath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
			PathValidator.isReadablePath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
			PathValidator.isWritablePath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testFile);
		}
	}

	/**
	 * Test {@link PathValidator#isPath(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsPathFailure1() throws ValidationException, IOException {
		PathValidator.isPath("\u0000", (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link PathValidator#isPath(Path, String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsPathFailure2() throws ValidationException, IOException {
		PathValidator.isPath(Paths.get("."), "\u0000", (a) -> VALIDATION_MESSAGE);
	}

	/**
	 * Test {@link PathValidator#isRegularFilePath(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsRegularFilePathFailure() throws ValidationException, IOException {
		Path testDirectory = Files.createTempDirectory(getClass().getSimpleName());

		try {
			PathValidator.isRegularFilePath(testDirectory.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testDirectory);
		}
	}

	/**
	 * Test {@link PathValidator#isDirectoryPath(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsDirectoryPathFailure() throws ValidationException, IOException {
		Path testFile = Files.createTempFile(getClass().getSimpleName(), null);

		try {
			PathValidator.isDirectoryPath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testFile);
		}
	}

	/**
	 * Test {@link PathValidator#isReadablePath(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsReadablePathFailure() throws ValidationException, IOException {
		Path testFile = Files.createTempFile(getClass().getSimpleName(), null);

		try {
			testFile.toFile().setReadable(false, false);
			PathValidator.isReadablePath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testFile);
		}
	}

	/**
	 * Test {@link PathValidator#isWritablePath(String, de.carne.util.MessageFormatter)} in failure scenario.
	 *
	 * @throws ValidationException if test succeeds.
	 * @throws IOException if an error occurs.
	 */
	@Test(expected = ValidationException.class)
	public void testIsWritablePathFailure() throws ValidationException, IOException {
		Path testFile = Files.createTempFile(getClass().getSimpleName(), null);

		try {
			testFile.toFile().setWritable(false, false);
			PathValidator.isWritablePath(testFile.toString(), (a) -> VALIDATION_MESSAGE);
		} finally {
			Files.delete(testFile);
		}
	}

}
