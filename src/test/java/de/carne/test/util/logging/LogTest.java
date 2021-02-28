/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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
import java.util.Collection;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.logging.Log;
import de.carne.util.logging.LogLevel;
import de.carne.util.logging.LogRecorder;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain Log} class.
 */
class LogTest {

	@Test
	void testLogNames() {
		Log defaultLog = new Log();

		Assertions.assertEquals(getClass().getName(), defaultLog.logger().getName());
		Assertions.assertEquals(getClass().getName(), defaultLog.toString());

		Log customLog = new Log(Object.class);

		Assertions.assertEquals(Object.class.getName(), customLog.logger().getName());
		Assertions.assertEquals(Object.class.getName(), customLog.toString());

		Log rootLog = Log.root();

		Assertions.assertEquals("", rootLog.logger().getName());
		Assertions.assertEquals("", rootLog.toString());
	}

	@Test
	void testLogLevel() {
		Log log = new Log();

		Assertions.assertEquals(LogLevel.LEVEL_DEBUG, log.level());

		Log rootLog = Log.root();

		Assertions.assertEquals(LogLevel.LEVEL_INFO, rootLog.level());
	}

	@Test
	void testLogCallee() throws IOException {
		Logs.readConfig("logging-debug.properties");

		Log log = new Log();
		LogRecorder recorder = new LogRecorder(LogLevel.LEVEL_TRACE);

		recorder.includeRecord(record -> true);
		recorder.addLog(log);
		try (LogRecorder.Session session = recorder.start(true)) {
			session.includeThread(thread -> true);
			logCalleeDefault(log);

			Collection<LogRecord> records = session.getRecords();

			Assertions.assertEquals(1, records.size());
			Assertions.assertEquals("de.carne.test.util.logging.LogTest.logCalleeDefault(LogTest.java:97)",
					records.iterator().next().getMessage());
		}
		try (LogRecorder.Session session = recorder.start(true)) {
			session.includeThread(thread -> true);
			logCalleeNotice(log);

			Collection<LogRecord> records = session.getRecords();

			Assertions.assertEquals(1, records.size());
			Assertions.assertEquals("de.carne.test.util.logging.LogTest.logCalleeNotice(LogTest.java:101)",
					records.iterator().next().getMessage());
		}
	}

	private void logCalleeDefault(Log log) {
		log.callee();
	}

	private void logCalleeNotice(Log log) {
		log.callee(LogLevel.LEVEL_NOTICE);
	}

}
