/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.logging;

import org.junit.Assert;
import org.junit.Test;

import de.carne.util.logging.Log;
import de.carne.util.logging.LogConfig;

/**
 * Test {@link Log} class functionality.
 */
public class LoggingTest {

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
	 * Test runtime log configuration.
	 */
	@Test
	public void testLogConfig() {
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
	}

}
