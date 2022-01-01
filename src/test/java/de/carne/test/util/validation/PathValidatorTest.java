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
package de.carne.test.util.validation;

import java.nio.file.Path;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.nio.file.FileUtil;
import de.carne.util.validation.InputValidator;
import de.carne.util.validation.PathValidator;
import de.carne.util.validation.StringValidator;
import de.carne.util.validation.ValidationException;
import de.carne.util.validation.ValidationMessage;

/**
 * Test {@linkplain PathValidator} class.
 */
class PathValidatorTest {

	private static final ValidationMessage VALIDATION_MESSAGE = arguments -> String.format("Invalid path '%1$s'",
			arguments);

	@Test
	void testPathValidation() throws ValidationException {
		@NonNull String input1 = "\\//:|<>*?\0\n";
		String message1 = Assertions.assertThrows(ValidationException.class, () -> {
			InputValidator.input(input1).convert(PathValidator::fromString, VALIDATION_MESSAGE);
		}).getMessage();

		Assertions.assertEquals(VALIDATION_MESSAGE.format(input1), message1);

		Path tmpDir = FileUtil.tmpDir();

		PathValidator pathValidator = StringValidator.checkNotEmpty(tmpDir.toString(), VALIDATION_MESSAGE)
				.convert(PathValidator::fromString, VALIDATION_MESSAGE);

		Assertions.assertEquals(tmpDir, pathValidator.get());
	}

}
