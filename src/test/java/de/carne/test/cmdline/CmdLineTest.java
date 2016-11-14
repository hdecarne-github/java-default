/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.cmdline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.cmdline.CmdLine;
import de.carne.util.cmdline.CmdLineActionException;
import de.carne.util.cmdline.CmdLineException;

/**
 * Test {@link CmdLine} class functionality.
 */
public class CmdLineTest {

	/**
	 * Test basic command line evaluation.
	 */
	@Test
	public void testCmdLineActions() {
		List<String> in = Arrays.asList("--arg1", "--arg2", "option1", "-1", "option2", "--arg3");
		List<String> out = new ArrayList<>();
		CmdLine cmdLine = new CmdLine(in);

		cmdLine.switchAction((arg) -> out.add(arg)).arg("--arg1").arg("-1");
		cmdLine.namedOptionAction((arg, option) -> {
			out.add(arg);
			out.add(option);
		}).arg("--arg1");
		cmdLine.unnamedOptionAction((option) -> out.add(option));
		cmdLine.unknownArgument((arg) -> out.add(arg));
		try {
			cmdLine.eval();
		} catch (CmdLineException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(in, out);
	}

	/**
	 * Test evaluation exception.
	 */
	@Test
	public void testCmdLineException() {
		CmdLine cmdLine = new CmdLine(Arrays.asList("--throw"));

		cmdLine.unknownArgument((arg) -> {
			throw new CmdLineActionException(arg);
		});
		try {
			cmdLine.eval();
			Assert.fail();
		} catch (CmdLineException e) {
			System.out.println(e);
		}
	}

}
