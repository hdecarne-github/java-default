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
package de.carne.test.util;

import org.junit.Test;

import de.carne.util.Exceptions;
import de.carne.util.ShutdownHooks;
import de.carne.util.logging.Log;

/**
 * Test {@linkplain ShutdownHooks} class.
 */
public class ShutdownHooksTest {

	private static final Log LOG = new Log();

	/**
	 * Test {@linkplain Exceptions#toRuntime(Throwable)} with checked {@linkplain Exception}.
	 */
	@Test
	public void testToRuntimeFromChecked() {
		ShutdownHooks.add(() -> {
			LOG.notice("Shutdown hook invoked");
		});
		ShutdownHooks.add(() -> {
			throw new IllegalStateException();
		});
	}

}
