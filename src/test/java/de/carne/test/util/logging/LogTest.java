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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.logging.Log;
import de.carne.util.logging.Logs;

/**
 * Test {@link Log} class.
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
	 * Test {@link Logs#readConfig(String)} with valid configs.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testLogConfigSuccess() throws IOException {
		Log log = new Log();

		Logs.readConfig("logging-notice.properties");
		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertFalse(log.isErrorLoggable());
		logTestMessages(log);
		Logs.readConfig("logging-error.properties");
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertFalse(log.isWarningLoggable());
		logTestMessages(log);
		Logs.readConfig("logging-warning.properties");
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		logTestMessages(log);
		Logs.readConfig("logging-info.properties");
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		logTestMessages(log);
		Logs.readConfig("logging-debug.properties");
		Assert.assertTrue(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());
		logTestMessages(log);
		Logs.readConfig("logging-trace.properties");
		Assert.assertTrue(log.isTraceLoggable());
		logTestMessages(log);
	}

	private void logTestMessages(Log log) {
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
	}

	/**
	 * Test {@link Logs#readConfig(String)} with non-existent config.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test(expected = FileNotFoundException.class)
	public void testLogConfigFailure() throws IOException {
		Logs.readConfig("logging-unknown.properties");
	}

}
