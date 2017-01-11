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
package de.carne.util.validation;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.carne.util.MessageFormatter;

/**
 * This class provides validation function for {@link Path} objects and the
 * like.
 */
public final class PathValidator {

	private PathValidator() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * Make sure input is a valid path.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 */
	public static Path isPath(String input, MessageFormatter message) throws ValidationException {
		assert input != null;
		assert message != null;

		Path path;

		try {
			path = Paths.get(input);
		} catch (InvalidPathException e) {
			throw new ValidationException(message.format(input));
		}
		return path;
	}

	/**
	 * Make sure input is a valid relative path.
	 *
	 * @param basePath The base path to validate the input against.
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 */
	public static Path isPath(Path basePath, String input, MessageFormatter message) throws ValidationException {
		assert basePath != null;
		assert input != null;
		assert message != null;

		Path path;

		try {
			path = basePath.resolve(input);
		} catch (InvalidPathException e) {
			throw new ValidationException(message.format(input));
		}
		return path;
	}

	/**
	 * Make sure input is a readable file.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 * @see Files#isReadable(Path)
	 */
	public static Path isReadableFile(String input, MessageFormatter message) throws ValidationException {
		Path inputPath = isPath(input, message);

		if (!Files.isReadable(inputPath)) {
			throw new ValidationException(message.format(input));
		}
		return inputPath;
	}

	/**
	 * Make sure input is a readable directory.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 * @see Files#isReadable(Path)
	 */
	public static Path isReadableDirectory(String input, MessageFormatter message) throws ValidationException {
		Path inputPath = isPath(input, message);

		if (!Files.isDirectory(inputPath)) {
			throw new ValidationException(message.format(input));
		}
		return inputPath;
	}

	/**
	 * Make sure input is a writable directory.
	 *
	 * @param input The input to validate.
	 * @param message The exception message to use if the input is invalid.
	 * @return The validated {@link Path} object.
	 * @throws ValidationException if the input is invalid.
	 * @see Files#isWritable(Path)
	 */
	public static Path isWritableDirectory(String input, MessageFormatter message) throws ValidationException {
		Path inputPath = isPath(input, message);

		if (!Files.isWritable(inputPath)) {
			throw new ValidationException(message.format(input));
		}
		return inputPath;
	}

}
