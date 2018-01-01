/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.cmdline.CmdLineException;
import de.carne.util.cmdline.CmdLineProcessor;

/**
 * Test {@linkplain CmdLineProcessor} class.
 */
public class CmdLineProcessorTest {

	/**
	 * Test successful processing.
	 *
	 * @throws CmdLineException if the command line is invalid.
	 */
	@Test
	public void testProcessingSuccess() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--switch", "-s", "--option", "option", "-o", "option", "--unknown", "unnamed"));

		cmdLine.onSwitch(arg -> {
			Assert.assertEquals("--switch", arg);
		}).arg("--switch");
		cmdLine.onSwitch(arg -> {
			Assert.assertEquals("-s", arg);
		}).arg("-s");
		cmdLine.onOption((arg, option) -> {
			Assert.assertEquals("--option", arg);
			Assert.assertEquals("option", option);
		}).arg("--option");
		cmdLine.onOption((arg, option) -> {
			Assert.assertEquals("-o", arg);
			Assert.assertEquals("option", option);
		}).arg("-o");
		cmdLine.onUnknownArg(option -> {
			Assert.assertEquals("--unknown", option);
		});
		cmdLine.onUnnamedOption(option -> {
			Assert.assertEquals("unnamed", option);
		});
		cmdLine.process();
	}

	/**
	 * Test processing failure (due to unknown argument).
	 *
	 * @throws CmdLineException if the command line is invalid.
	 */
	@Test(expected = CmdLineException.class)
	public void testUnknownArgumentFailure() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--switch"));

		cmdLine.process();
	}

	/**
	 * Test processing failure (due to unnamed option).
	 *
	 * @throws CmdLineException if the command line is invalid.
	 */
	@Test(expected = CmdLineException.class)
	public void testUnnamedOptionFailure() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--switch"));

		cmdLine.process();
	}

	/**
	 * Test processing failure (due to missing option).
	 *
	 * @throws CmdLineException if the command line is invalid.
	 */
	@Test(expected = CmdLineException.class)
	public void testMissingOptionFailure() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), Arrays.asList("--option"));

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("--option");
		cmdLine.process();
	}

	/**
	 * Test processing failure (due to invalid option).
	 *
	 * @throws CmdLineException if the command line is invalid.
	 */
	@Test(expected = CmdLineException.class)
	public void testInvalidOptionFailure() throws CmdLineException {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--option", "--unexpected"));

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("--option");
		cmdLine.process();
	}

	/**
	 * Test setup failure (due to an invalid argument string).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArg1Failure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), new String[0]);

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("o");
	}

	/**
	 * Test setup failure (due to an invalid argument string).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArg2Failure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), new String[0]);

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("---o");
	}

	/**
	 * Test setup failure (due to an invalid short argument string).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidShortArgFailure() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(), new String[0]);

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("-option");
	}

	/**
	 * Test {@linkplain CmdLineException}.
	 */
	@Test
	public void testCmdLineException() {
		CmdLineProcessor cmdLine = new CmdLineProcessor(getClass().getSimpleName(),
				Arrays.asList("--option", "--unexpected"));

		cmdLine.onOption((arg, option) -> {
			Assert.fail();
		}).arg("--option");
		try {
			cmdLine.process();
		} catch (CmdLineException e) {
			Assert.assertEquals(cmdLine.toString(), e.cmdLine());
			Assert.assertEquals("--option", e.arg());
		}
	}

}
