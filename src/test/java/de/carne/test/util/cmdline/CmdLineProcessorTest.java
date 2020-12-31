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
package de.carne.test.util.cmdline;

import java.util.Arrays;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.cmdline.CmdLineAction;
import de.carne.util.cmdline.CmdLineException;
import de.carne.util.cmdline.CmdLineProcessor;

/**
 * Test {@linkplain CmdLineProcessor} class.
 */
class CmdLineProcessorTest {

	@Test
	void testProcessingSuccess() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--switch", "-s", "--option", "option", "-o", "option", "--unknown", "unnamed"));

		cmdLine.onSwitch(arg -> {
			Assertions.assertEquals("--switch", arg);
		}).arg("--switch");
		cmdLine.onSwitch(arg -> {
			Assertions.assertEquals("-s", arg);
		}).arg("-s");
		cmdLine.onOption((arg, option) -> {
			Assertions.assertEquals("--option", arg);
			Assertions.assertEquals("option", option);
		}).arg("--option");
		cmdLine.onOption((arg, option) -> {
			Assertions.assertEquals("-o", arg);
			Assertions.assertEquals("option", option);
		}).arg("-o");
		cmdLine.onUnknownArg(option -> {
			Assertions.assertEquals("--unknown", option);
		});
		cmdLine.onUnnamedOption(option -> {
			Assertions.assertEquals("unnamed", option);
		});
		cmdLine.process();
	}

	@Test
	void testUnknownArgumentFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--switch"));

		Assertions.assertThrows(CmdLineException.class, () -> {
			cmdLine.process();
		});
	}

	@Test
	void testUnnamedOptionFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--switch"));

		Assertions.assertThrows(CmdLineException.class, () -> {
			cmdLine.process();
		});
	}

	@Test
	void testMissingOptionFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--option"));

		cmdLine.onOption((arg, option) -> {
			Assertions.fail("Unexpected invokation");
		}).arg("--option");
		Assertions.assertThrows(CmdLineException.class, () -> {
			cmdLine.process();
		});
	}

	@Test
	void testInvalidOptionFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--option", "--errornous"));

		cmdLine.onOption((arg, option) -> {
			Assertions.fail("Unexpected invokation");
		}).arg("--option");

		Assertions.assertThrows(CmdLineException.class, () -> {
			cmdLine.process();
		});

		cmdLine.onOption((arg, option) -> {
			throw new IllegalArgumentException();
		}).arg("--errornous");

		Assertions.assertThrows(CmdLineException.class, () -> {
			cmdLine.process();
		});
	}

	@Test
	void testInvalidArgFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), new @Nullable String[0]);
		CmdLineAction cmdLineAction = cmdLine.onOption((arg, option) -> {
			Assertions.fail("Unexpected invokation");
		});

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cmdLineAction.arg("o");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cmdLineAction.arg("---o");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cmdLineAction.arg("-option");
		});
	}

	@Test
	void testCmdLineException() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--option", "--unexpected"));

		cmdLine.onOption((arg, option) -> {
			Assertions.fail("Unexpected invokation");
		}).arg("--option");
		try {
			cmdLine.process();
		} catch (CmdLineException e) {
			Assertions.assertEquals(cmdLine.toString(), e.cmdLine());
			Assertions.assertEquals("--option", e.arg());
		}
	}

}
