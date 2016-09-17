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
package de.carne.util.logging;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Simple wrapper for JDK's {@link Logger} class to have a minimum level of
 * abstraction and a clear level semantics.
 */
public final class Log {

	private Logger logger;

	/**
	 * Construct {@code Log}.
	 * <p>
	 * The logger name is automatically derived from the calling class.
	 * </p>
	 */
	public Log() {
		this(getCallerClassName());
	}

	/**
	 * Construct {@code Log}.
	 * <p>
	 * The logger name is automatically derived from the calling class.
	 * </p>
	 *
	 * @param bundle The {@code ResourceBundle} to use for message localization.
	 */
	public Log(ResourceBundle bundle) {
		this(getCallerClassName(), bundle);
	}

	/**
	 * Construct {@code Log}.
	 *
	 * @param clazz The {@code Class} defining the logger name.
	 */
	public Log(Class<?> clazz) {
		this(clazz.getName());
	}

	/**
	 * Construct {@code Log}.
	 *
	 * @param clazz The {@code Class} defining the logger name.
	 * @param bundle The {@code ResourceBundle} to use for message localization.
	 */
	public Log(Class<?> clazz, ResourceBundle bundle) {
		this(clazz.getName(), bundle);
	}

	/**
	 * Construct {@code Log}.
	 *
	 * @param name The logger name to use.
	 */
	public Log(String name) {
		this(name, null);
	}

	/**
	 * Construct {@code Log}.
	 *
	 * @param name The logger name to use.
	 * @param bundle The {@code ResourceBundle} to use for message localization.
	 */
	public Log(String name, ResourceBundle bundle) {
		assert name != null;

		this.logger = Logger.getLogger(name, (bundle != null ? bundle.getBaseBundleName() : null));
	}

	/**
	 * Check whether this log is active for a specific level.
	 *
	 * @param level The level to check.
	 * @return {@code true} if this log is active for the submitted level.
	 */
	public boolean isLoggable(Level level) {
		assert level != null;

		return this.logger.isLoggable(level);
	}

	/**
	 * Issue a log message.
	 *
	 * @param level The log level to use.
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void log(Level level, Throwable thrown, String msg, Object... parameters) {
		assert level != null;
		assert msg != null;

		if (this.logger.isLoggable(level)) {
			LogRecord record = new LogRecord(level, msg);

			record.setResourceBundle(this.logger.getResourceBundle());
			record.setThrown(thrown);
			record.setParameters(parameters);
			record.setLoggerName(this.logger.getName());
			this.logger.log(record);
		}
	}

	/**
	 * Check whether this log is active for level notice.
	 *
	 * @return {@code true} if this log is active for level notice.
	 */
	public boolean isNoticeLoggable() {
		return isLoggable(LogLevel.LEVEL_NOTICE);
	}

	/**
	 * Log notice message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void notice(String msg, Object... parameters) {
		log(LogLevel.LEVEL_NOTICE, null, msg, parameters);
	}

	/**
	 * Log notice message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void notice(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_NOTICE, thrown, msg, parameters);
	}

	/**
	 * Check whether this log is active for level error.
	 *
	 * @return {@code true} if this log is active for level error.
	 */
	public boolean isErrorLoggable() {
		return isLoggable(LogLevel.LEVEL_ERROR);
	}

	/**
	 * Log error message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void error(String msg, Object... parameters) {
		log(LogLevel.LEVEL_ERROR, null, msg, parameters);
	}

	/**
	 * Log error message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void error(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_ERROR, thrown, msg, parameters);
	}

	/**
	 * Check whether this log is active for level warning.
	 *
	 * @return {@code true} if this log is active for level warning.
	 */
	public boolean isWarningLoggable() {
		return isLoggable(LogLevel.LEVEL_WARNING);
	}

	/**
	 * Log warning message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void warning(String msg, Object... parameters) {
		log(LogLevel.LEVEL_WARNING, null, msg, parameters);
	}

	/**
	 * Log warning message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void warning(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_WARNING, thrown, msg, parameters);
	}

	/**
	 * Check whether this log is active for level info.
	 *
	 * @return {@code true} if this log is active for level info.
	 */
	public boolean isInfoLoggable() {
		return isLoggable(LogLevel.LEVEL_INFO);
	}

	/**
	 * Log info message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void info(String msg, Object... parameters) {
		log(LogLevel.LEVEL_INFO, null, msg, parameters);
	}

	/**
	 * Log info message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void info(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_INFO, thrown, msg, parameters);
	}

	/**
	 * Check whether this log is active for level debug.
	 *
	 * @return {@code true} if this log is active for level debug.
	 */
	public boolean isDebugLoggable() {
		return isLoggable(LogLevel.LEVEL_DEBUG);
	}

	/**
	 * Log debug message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void debug(String msg, Object... parameters) {
		log(LogLevel.LEVEL_DEBUG, null, msg, parameters);
	}

	/**
	 * Log debug message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void debug(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_DEBUG, thrown, msg, parameters);
	}

	/**
	 * Check whether this log is active for level trace.
	 *
	 * @return {@code true} if this log is active for level trace.
	 */
	public boolean isTraceLoggable() {
		return isLoggable(LogLevel.LEVEL_TRACE);
	}

	/**
	 * Log trace message.
	 *
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void trace(String msg, Object... parameters) {
		log(LogLevel.LEVEL_TRACE, null, msg, parameters);
	}

	/**
	 * Log trace message.
	 *
	 * @param thrown The optional {@code Throwable} to log.
	 * @param msg The message to log.
	 * @param parameters The message parameters to log.
	 */
	public void trace(Throwable thrown, String msg, Object... parameters) {
		log(LogLevel.LEVEL_TRACE, thrown, msg, parameters);
	}

	private static String getCallerClassName() {
		int steIndex = 0;
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		String thisClassName = Log.class.getName();

		while (steIndex < stes.length && !thisClassName.equals(stes[steIndex].getClassName())) {
			steIndex++;
		}
		while (steIndex < stes.length && thisClassName.equals(stes[steIndex].getClassName())) {
			steIndex++;
		}
		return (steIndex < stes.length ? stes[steIndex].getClassName() : thisClassName);
	}

}
