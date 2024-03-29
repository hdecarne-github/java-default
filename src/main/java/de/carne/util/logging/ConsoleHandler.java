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
package de.carne.util.logging;

import java.io.Console;
import java.io.PrintWriter;
import java.util.logging.ErrorManager;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * A {@linkplain java.util.logging.Handler} which makes use of the {@linkplain Console} class.
 * <p>
 * If the current VM has a {@linkplain Console} attached any log messages issued to this handler will be written to this
 * console. If the current VM has no {@linkplain Console} attached the behavior depends on the handler's
 * {@code consoleOnly} property. If this property is set to {@code true} (default) log message are ignored. If this
 * property is set to {@code false} log messages are written to {@linkplain System#out}.
 */
public class ConsoleHandler extends StreamHandler {

	private final PublishLock lock = PublishLock.getInstance();
	private final boolean consoleOnly;

	/**
	 * Construct {@linkplain ConsoleHandler}.
	 */
	@SuppressWarnings("squid:S106")
	public ConsoleHandler() {
		LogManager manager = LogManager.getLogManager();
		String propertyBase = getClass().getName();

		this.consoleOnly = Logs.getBooleanProperty(manager, propertyBase + ".consoleOnly", false);
		setOutputStream(System.out);
	}

	@Override
	public synchronized void publish(LogRecord logRecord) {
		this.lock.ifNotLocked(() -> publish0(logRecord));
	}

	private void publish0(LogRecord logRecord) {
		Console console = System.console();

		if (console != null) {
			if (isLoggable(logRecord)) {
				publishToConsole(console, logRecord, true);
			}
		} else if (!this.consoleOnly) {
			super.publish(logRecord);
			super.flush();
		}
	}

	private void publishToConsole(Console console, LogRecord logRecord, boolean flush) {
		String message = null;

		try {
			message = getFormatter().format(logRecord);
		} catch (Exception e) {
			reportError(null, e, ErrorManager.FORMAT_FAILURE);
		}
		if (message != null) {
			@SuppressWarnings("resource") PrintWriter writer = console.writer();

			try {
				writer.write(message);
				if (flush) {
					writer.flush();
				}
			} catch (Exception e) {
				reportError(null, e, ErrorManager.WRITE_FAILURE);
			}
		}
	}

	@Override
	public synchronized void flush() {
		Console console = System.console();

		if (console != null) {
			try {
				console.flush();
			} catch (Exception e) {
				reportError(null, e, ErrorManager.FLUSH_FAILURE);
			}
		} else {
			super.flush();
		}
	}

	@Override
	public synchronized void close() {
		this.lock.close();
		flush();
	}

}
