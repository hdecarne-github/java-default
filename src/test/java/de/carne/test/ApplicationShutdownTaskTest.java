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
package de.carne.test;

import org.junit.Assert;
import org.junit.Test;

import de.carne.ApplicationShutdownTask;

/**
 * Test {@link ApplicationShutdownTask} class.
 */
public class ApplicationShutdownTaskTest {

	private int shutdownTaskCounter = 0;

	/**
	 * Test {@link ApplicationShutdownTask} class.
	 */
	@Test
	public void testApplicationShutdownTask() {
		ApplicationShutdownTask.register("increment1", () -> this.shutdownTaskCounter++);
		ApplicationShutdownTask.register("increment2", () -> this.shutdownTaskCounter++);
		Assert.assertEquals(0, this.shutdownTaskCounter);
		ApplicationShutdownTask.trigger();
		Assert.assertEquals(2, this.shutdownTaskCounter);
		ApplicationShutdownTask.trigger();
		Assert.assertEquals(2, this.shutdownTaskCounter);
		ApplicationShutdownTask.register("increment3", () -> this.shutdownTaskCounter++);
		ApplicationShutdownTask.trigger();
		Assert.assertEquals(3, this.shutdownTaskCounter);
	}

}
