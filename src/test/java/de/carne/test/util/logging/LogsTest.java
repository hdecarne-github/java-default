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
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import org.junit.Assert;
import org.junit.Test;

import de.carne.io.IOUtil;
import de.carne.util.logging.LocalizedFilter;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogLevel;
import de.carne.util.logging.LogRecorder;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain Logs} class.
 */
public class LogsTest {

	/**
	 * Test {@linkplain Logs#readConfig(String)} with valid configs as well as the {@linkplain LogRecorder}.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testLogConfigSuccess() throws IOException {
		Log log = new Log();

		Logs.readConfig("logging-notice.properties");
		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertFalse(log.isErrorLoggable());
		LoggingTests.logTestMessages(log, 2);
		Logs.readConfig("logging-error.properties");
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertFalse(log.isWarningLoggable());
		LoggingTests.logTestMessages(log, 4);
		Logs.readConfig("logging-warning.properties");
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		LoggingTests.logTestMessages(log, 6);
		Logs.readConfig("logging-info.properties");
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		LoggingTests.logTestMessages(log, 8);
		Logs.readConfig("logging-debug.properties");
		Assert.assertTrue(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());
		LoggingTests.logTestMessages(log, 10);
		Logs.readConfig("logging-trace.properties");
		Assert.assertTrue(log.isTraceLoggable());
		LoggingTests.logTestMessages(log, 12);

		File configFile = Files.createTempFile(getClass().getName(), ".properties").toFile();

		configFile.deleteOnExit();

		IOUtil.copyUrl(configFile, getClass().getResource("/logging-warning.properties"));

		Logs.readConfig(configFile.getAbsolutePath());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		LoggingTests.logTestMessages(log, 6);
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

	/**
	 * Test {@linkplain LogManager} properties access.
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testLogManagerProperties() throws IOException {
		Logs.readConfig(Logs.CONFIG_DEFAULT);

		LogManager manager = LogManager.getLogManager();
		String propertyBase = getClass().getName();

		Assert.assertTrue(Logs.getBooleanProperty(manager, propertyBase + ".booleanTrue", false));
		Assert.assertFalse(Logs.getBooleanProperty(manager, propertyBase + ".booleanFalse", true));
		Assert.assertTrue(Logs.getBooleanProperty(manager, propertyBase + ".booleanUnknown", true));

		Assert.assertEquals(LogLevel.LEVEL_DEBUG,
				Logs.getLevelProperty(manager, propertyBase + ".levelDebug", LogLevel.LEVEL_ERROR));
		Assert.assertEquals(LogLevel.LEVEL_WARNING,
				Logs.getLevelProperty(manager, propertyBase + ".levelWarning", LogLevel.LEVEL_ERROR));
		Assert.assertEquals(LogLevel.LEVEL_ERROR,
				Logs.getLevelProperty(manager, propertyBase + ".levelUnknown", LogLevel.LEVEL_ERROR));

		Assert.assertTrue(Logs.getFilterProperty(manager, propertyBase + ".filter", null) instanceof LocalizedFilter);

		Assert.assertTrue(Logs.getFormatterProperty(manager, propertyBase + ".formatter",
				new SimpleFormatter()) instanceof XMLFormatter);
	}

}
