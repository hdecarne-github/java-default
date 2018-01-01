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
package de.carne.test.util.logging;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.check.Check;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogBuffer;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain LogBuffer} class.
 */
public class LogBufferTest {

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty(LogBuffer.class.getName() + ".LIMIT", "5");
	}

	/**
	 * Test {@linkplain LogBuffer}.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testLogBuffer() throws IOException {
		Logs.readConfig(Logs.CONFIG_DEFAULT);

		Log log = new Log();

		Assert.assertNotNull(LogBuffer.get(log));

		LogBuffer.flush(log);

		LogRecordCounter counter1 = new LogRecordCounter();

		LogBuffer.addHandler(log, counter1);

		Assert.assertEquals(0, counter1.getPublishCount());
		Assert.assertEquals(0, counter1.getFlushCount());
		Assert.assertEquals(0, counter1.getCloseCount());

		LoggingTests.logTestMessages(log, 6);

		Assert.assertEquals(6, counter1.getPublishCount());

		LogRecordCounter counter2 = new LogRecordCounter();

		LogBuffer.addHandler(log, counter2);

		Assert.assertEquals(5, counter2.getPublishCount());

		LogBuffer.removeHandler(log, counter2);
		LogBuffer.flush(log);

		Assert.assertEquals(1, counter1.getFlushCount());
		Assert.assertEquals(0, counter2.getFlushCount());

		LogBuffer.flush(log);

		Assert.assertEquals(2, counter1.getFlushCount());

		Check.notNull(LogBuffer.get(log)).close();

		Assert.assertEquals(1, counter1.getCloseCount());

		Check.notNull(LogBuffer.get(log)).close();

		Assert.assertEquals(1, counter1.getCloseCount());
	}

}
