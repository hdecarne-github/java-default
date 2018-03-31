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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.check.Check;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogBuffer;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain LogBuffer} class.
 */
class LogBufferTest {

	@Test
	void testLogBuffer() throws IOException {
		Logs.readConfig(Logs.CONFIG_DEFAULT);

		Log log = new Log();

		Assertions.assertNotNull(LogBuffer.get(log));

		LogBuffer.flush(log);

		LogRecordCounter counter1 = new LogRecordCounter();

		LogBuffer.addHandler(log, counter1);

		Assertions.assertEquals(0, counter1.getPublishCount());
		Assertions.assertEquals(0, counter1.getFlushCount());
		Assertions.assertEquals(0, counter1.getCloseCount());

		LoggingTests.logTestMessages(log, 6);

		Assertions.assertEquals(6, counter1.getPublishCount());

		LogRecordCounter counter2 = new LogRecordCounter();

		LogBuffer.addHandler(log, counter2);

		Assertions.assertEquals(5, counter2.getPublishCount());

		LogBuffer.removeHandler(log, counter2);
		LogBuffer.flush(log);

		Assertions.assertEquals(1, counter1.getFlushCount());
		Assertions.assertEquals(0, counter2.getFlushCount());

		LogBuffer.flush(log);

		Assertions.assertEquals(2, counter1.getFlushCount());

		Check.notNull(LogBuffer.get(log)).close();

		Assertions.assertEquals(1, counter1.getCloseCount());

		Check.notNull(LogBuffer.get(log)).close();

		Assertions.assertEquals(1, counter1.getCloseCount());
	}

}
