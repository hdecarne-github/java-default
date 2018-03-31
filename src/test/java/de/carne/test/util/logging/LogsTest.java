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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.carne.io.IOUtil;
import de.carne.util.logging.Config;
import de.carne.util.logging.LocalizedFilter;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogLevel;
import de.carne.util.logging.Logs;

/**
 * Test {@linkplain Logs} class.
 */
class LogsTest {

	@BeforeAll
	static void setUpSystemProperties() {
		System.setProperty("java.util.logging.config.class", Config.class.getName());
	}

	@Test
	void testLogConfigSuccess() throws IOException {
		Log log = new Log();

		Assertions.assertTrue(log.isWarningLoggable());
		Assertions.assertFalse(log.isInfoLoggable());
		LoggingTests.logTestMessages(log, 6);
		Logs.readConfig("logging-notice.properties");
		Assertions.assertTrue(log.isNoticeLoggable());
		Assertions.assertFalse(log.isErrorLoggable());
		LoggingTests.logTestMessages(log, 2);
		Logs.readConfig("logging-error.properties");
		Assertions.assertTrue(log.isErrorLoggable());
		Assertions.assertFalse(log.isWarningLoggable());
		LoggingTests.logTestMessages(log, 4);
		Logs.readConfig("logging-warning.properties");
		Assertions.assertTrue(log.isWarningLoggable());
		Assertions.assertFalse(log.isInfoLoggable());
		LoggingTests.logTestMessages(log, 6);
		Logs.readConfig("logging-info.properties");
		Assertions.assertTrue(log.isInfoLoggable());
		Assertions.assertFalse(log.isDebugLoggable());
		LoggingTests.logTestMessages(log, 8);
		Logs.readConfig("logging-debug.properties");
		Assertions.assertTrue(log.isDebugLoggable());
		Assertions.assertFalse(log.isTraceLoggable());
		LoggingTests.logTestMessages(log, 10);
		Logs.readConfig("logging-trace.properties");
		Assertions.assertTrue(log.isTraceLoggable());
		LoggingTests.logTestMessages(log, 12);

		File configFile = Files.createTempFile(getClass().getName(), ".properties").toFile();

		configFile.deleteOnExit();

		IOUtil.copyUrl(configFile, getClass().getResource("/logging-warning.properties"));

		Logs.readConfig(configFile.getAbsolutePath());
		Assertions.assertTrue(log.isWarningLoggable());
		Assertions.assertFalse(log.isInfoLoggable());
		LoggingTests.logTestMessages(log, 6);
	}

	@Test
	void testLogConfigFailure() {
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			Logs.readConfig("logging-unknown.properties");
		});
	}

	@Test
	void testLogManagerProperties() throws IOException {
		Logs.readConfig(Logs.CONFIG_DEFAULT);

		LogManager manager = LogManager.getLogManager();
		String propertyBase = getClass().getName();

		Assertions.assertEquals("Invalid", Logs.getStringProperty(manager, propertyBase + ".invalid", ""));
		Assertions.assertEquals("Unknown", Logs.getStringProperty(manager, propertyBase + ".unnown", "Unknown"));

		Assertions.assertEquals(Logs.getIntProperty(manager, propertyBase + ".intOne", -1), 1);
		Assertions.assertEquals(Logs.getIntProperty(manager, propertyBase + ".intTwo", -1), 2);
		Assertions.assertEquals(Logs.getIntProperty(manager, propertyBase + ".invalid", -1), -1);

		Assertions.assertTrue(Logs.getBooleanProperty(manager, propertyBase + ".booleanTrue", false));
		Assertions.assertFalse(Logs.getBooleanProperty(manager, propertyBase + ".booleanFalse", true));
		Assertions.assertTrue(Logs.getBooleanProperty(manager, propertyBase + ".booleanUnknown", true));
		Assertions.assertFalse(Logs.getBooleanProperty(manager, propertyBase + ".invalid", true));

		Assertions.assertEquals(LogLevel.LEVEL_DEBUG,
				Logs.getLevelProperty(manager, propertyBase + ".levelDebug", LogLevel.LEVEL_ERROR));
		Assertions.assertEquals(LogLevel.LEVEL_WARNING,
				Logs.getLevelProperty(manager, propertyBase + ".levelWarning", LogLevel.LEVEL_ERROR));
		Assertions.assertEquals(LogLevel.LEVEL_ERROR,
				Logs.getLevelProperty(manager, propertyBase + ".levelUnknown", LogLevel.LEVEL_ERROR));
		Assertions.assertEquals(LogLevel.LEVEL_DEBUG,
				Logs.getLevelProperty(manager, propertyBase + ".invalid", LogLevel.LEVEL_DEBUG));

		Assertions
				.assertTrue(Logs.getFilterProperty(manager, propertyBase + ".filter", null) instanceof LocalizedFilter);
		Assertions.assertNull(Logs.getFilterProperty(manager, propertyBase + ".invalid", null));

		Assertions.assertTrue(Logs.getFormatterProperty(manager, propertyBase + ".formatter",
				new SimpleFormatter()) instanceof XMLFormatter);
		Assertions.assertTrue(Logs.getFormatterProperty(manager, propertyBase + ".invalid",
				new SimpleFormatter()) instanceof SimpleFormatter);
	}

	@Test
	void testFlush() {
		Logs.flush();
	}

}
