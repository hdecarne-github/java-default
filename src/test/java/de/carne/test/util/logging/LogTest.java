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
package de.carne.test.util.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Assert;
import org.junit.Test;

import de.carne.io.IOUtil;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogLevel;
import de.carne.util.logging.LogRecorder;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain Log} class.
 */
public class LogTest {

	/**
	 * Test logger names.
	 */
	@Test
	public void testLogNames() {
		Log defaultLog = new Log();

		Assert.assertEquals(getClass().getName(), defaultLog.logger().getName());

		Log customLog = new Log(Object.class);

		Assert.assertEquals(Object.class.getName(), customLog.logger().getName());
	}

	/**
	 * Test {@linkplain Logs#readConfig(String)} with valid configs.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testLogConfigSuccess() throws IOException {
		Log log = new Log();

		Logs.readConfig("logging-notice.properties");
		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertFalse(log.isErrorLoggable());
		logTestMessages(log, 2);
		Logs.readConfig("logging-error.properties");
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertFalse(log.isWarningLoggable());
		logTestMessages(log, 4);
		Logs.readConfig("logging-warning.properties");
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		logTestMessages(log, 6);
		Logs.readConfig("logging-info.properties");
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		logTestMessages(log, 8);
		Logs.readConfig("logging-debug.properties");
		Assert.assertTrue(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());
		logTestMessages(log, 10);
		Logs.readConfig("logging-trace.properties");
		Assert.assertTrue(log.isTraceLoggable());
		logTestMessages(log, 12);

		File configFile = Files.createTempFile(getClass().getName(), ".properties").toFile();

		configFile.deleteOnExit();

		IOUtil.copyUrl(configFile, getClass().getResource("/logging-warning.properties"));

		Logs.readConfig(configFile.getAbsolutePath());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		logTestMessages(log, 6);
	}

	private void logTestMessages(Log log, int expectedRecordCount) {
		LogRecorder recorder = new LogRecorder(LogLevel.LEVEL_TRACE);

		recorder.includeRecord(record -> true);
		recorder.addLog(log);

		try (LogRecorder.Session session = recorder.start(true)) {
			session.includeThread(thread -> true);

			Exception thrown = new IllegalStateException();

			log.trace("Trace message");
			log.trace(thrown, "Trace message (with exception)");
			log.debug("Debug message");
			log.debug(thrown, "Debug message (with exception)");
			log.info("Info message");
			log.info(thrown, "Info message (with exception)");
			log.warning("Warning message");
			log.warning(thrown, "Warning message (with exception)");
			log.error("Error message");
			log.error(thrown, "Error message (with exception)");
			log.notice("Notice message");
			log.notice(thrown, "Notice message (with exception)");
			Assert.assertEquals(expectedRecordCount, session.getRecords().size());
		}
	}

	/**
	 * Test {@linkplain Logs#readConfig(String)} with non-existent config.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test(expected = FileNotFoundException.class)
	public void testLogConfigFailure() throws IOException {
		Logs.readConfig("logging-unknown.properties");
	}

}
