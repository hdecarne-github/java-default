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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.check.Nullable;
import de.carne.io.IOHelper;
import de.carne.util.logging.Log;
import de.carne.util.logging.LogBuffer;
import de.carne.util.logging.LogConfig;
import de.carne.util.logging.LogLevel;
import de.carne.util.logging.LogMonitor;

/**
 * Test {@link Log} class functionality.
 */
public class LoggingTest {

	private static final Log LOG = new Log();

	private static final String LOG_MESSAGE = LoggingTest.class.getSimpleName();

	/**
	 * Setup default log config.
	 */
	@BeforeClass
	public static void setupLogConfig() {
		new LogConfig();
	}

	/**
	 * Test basic log functions.
	 */
	@Test
	public void testBasicLogFunctions() {
		LogConfig.applyConfig(LogConfig.CONFIG_DEFAULT);

		Log log = new Log();

		log.notice(new Throwable(), "Notice");
		log.error(new Throwable(), "Error");
		log.warning(new Throwable(), "Warning");
		log.info(new Throwable(), "Info");
		log.debug(new Throwable(), "Debug");
		log.trace(new Throwable(), "Trace");
	}

	/**
	 * Test log creation.
	 */
	@Test
	public void testLogCreation() {
		Assert.assertEquals(getClass().getName(), new Log().getLogger().getName());
		Assert.assertEquals(getClass().getName(), new Log(getClass()).getLogger().getName());
	}

	/**
	 * Test runtime log configuration.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testLogConfig() throws IOException {
		Log log = new Log();

		LogConfig.applyConfig(LogConfig.CONFIG_DEFAULT);
		log.notice(LogConfig.CONFIG_DEFAULT);

		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());

		LogConfig.applyConfig(LogConfig.CONFIG_VERBOSE);
		log.info(LogConfig.CONFIG_VERBOSE);

		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());

		LogConfig.applyConfig(LogConfig.CONFIG_DEBUG);
		log.debug(LogConfig.CONFIG_DEBUG);

		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertTrue(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());

		LogConfig.applyConfig("Unknown");

		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertTrue(log.isInfoLoggable());
		Assert.assertTrue(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());

		LogConfig.applyConfig(null);
		Path configFile = IOHelper.createTempFileFromResource(getClass().getResource("/logging-default.properties"),
				getClass().getSimpleName(), null);

		try {
			LogConfig.applyConfig(configFile.toString());
		} finally {
			Files.delete(configFile);
		}

		Assert.assertTrue(log.isNoticeLoggable());
		Assert.assertTrue(log.isErrorLoggable());
		Assert.assertTrue(log.isWarningLoggable());
		Assert.assertFalse(log.isInfoLoggable());
		Assert.assertFalse(log.isDebugLoggable());
		Assert.assertFalse(log.isTraceLoggable());
	}

	/**
	 * Test log buffer.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testLogBuffer() throws IOException {
		LogBuffer logBuffer = LogBuffer.getInstance(LOG.getLogger());

		Assert.assertNotNull(logBuffer);

		logBuffer.clear();

		LOG.notice(LOG_MESSAGE);

		LogCounter logCounter1 = new LogCounter();

		logBuffer.addHandler(logCounter1);

		Assert.assertEquals(1, logCounter1.getPublishCounter());

		LOG.notice(LOG_MESSAGE);

		Assert.assertEquals(2, logCounter1.getPublishCounter());

		logBuffer.flush();

		Assert.assertEquals(1, logCounter1.getFlushCounter());

		logBuffer.removeHandler(logCounter1);

		File exportFile = Files.createTempFile(getClass().getSimpleName(), null).toFile();

		assert exportFile != null;

		try {
			logBuffer.exportTo(exportFile);
			Assert.assertTrue(exportFile.length() > 0);
		} finally {
			exportFile.delete();
		}
		try {
			LogBuffer.exportTo(LOG.getLogger(), exportFile);
			Assert.assertTrue(exportFile.length() > 0);
		} finally {
			exportFile.delete();
		}

		LogCounter logCounter2 = new LogCounter();

		LogBuffer.clear(LOG.getLogger());
		LogBuffer.addHandler(LOG.getLogger(), logCounter2);

		Assert.assertEquals(0, logCounter2.getPublishCounter());

		LogBuffer.removeHandler(LOG.getLogger(), logCounter2);
	}

	private static class LogCounter extends Handler {

		private int publishCounter = 0;
		private int flushCounter = 0;

		LogCounter() {
			// Just to prevent access warning
		}

		public int getPublishCounter() {
			return this.publishCounter;
		}

		public int getFlushCounter() {
			return this.flushCounter;
		}

		@Override
		public void publish(@Nullable LogRecord record) {
			this.publishCounter++;
		}

		@Override
		public void flush() {
			this.flushCounter++;
		}

		@Override
		public void close() throws SecurityException {
			// Nothing to do here
		}

	}

	/**
	 * Test log monitor.
	 */
	@Test
	public void testLogMonitor() {
		LogMonitor monitor1 = new LogMonitor(LogLevel.LEVEL_ERROR);

		try (LogMonitor.Session session1 = monitor1.start()) {
			session1.includeLog(LOG);
			LOG.warning(LOG_MESSAGE);
			LOG.error(LOG_MESSAGE);
		}

		Assert.assertTrue(monitor1.notEmpty());
		Assert.assertEquals(1, monitor1.getRecords().size());

		LogMonitor monitor2 = new LogMonitor(LogLevel.LEVEL_WARNING);

		monitor2.includeThread(Thread.currentThread());
		try (LogMonitor.Session session2 = monitor2.start()) {
			session2.includeLogger(LOG.getLogger());
			LOG.warning(LOG_MESSAGE);
		}
		monitor2.excludeThread(Thread.currentThread());

		Assert.assertTrue(monitor2.notEmpty());
		Assert.assertEquals(1, monitor2.getRecords().size());
	}

}
