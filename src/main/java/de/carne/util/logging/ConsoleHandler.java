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
package de.carne.util.logging;

import java.io.Console;
import java.util.logging.ErrorManager;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import de.carne.check.Nullable;

/**
 * An enhanced console handler which makes use of the {@linkplain Console} class.
 * <p>
 * If the current VM has a {@linkplain Console} attached any log messages issued to this handler will be written to this
 * console. If the current VM has no {@linkplain Console} attached the behavior depends on the handler's
 * {@code consoleOnly} property. If this property is set to {@code true} (default) log message are ignored. If this
 * property is set to {@code false} log messages are written to {@linkplain System#out}.
 */
public class ConsoleHandler extends StreamHandler {

	private final boolean consoleOnly;

	/**
	 *
	 */
	public ConsoleHandler() {
		LogManager manager = LogManager.getLogManager();
		String propertyBase = getClass().getName();

		this.consoleOnly = Logs.getBooleanProperty(manager, propertyBase + ".consoleOnly", true);
		setOutputStream(System.out);
	}

	@Override
	public synchronized void publish(@Nullable LogRecord record) {
		Console console = System.console();

		if (record != null && console != null) {
			if (isLoggable(record)) {
				publishToConsole(console, record);
			}
		} else if (!this.consoleOnly) {
			super.publish(record);
		}
	}

	private void publishToConsole(Console console, LogRecord record) {
		String message = null;

		try {
			message = getFormatter().format(record);
		} catch (Exception e) {
			reportError(null, e, ErrorManager.FORMAT_FAILURE);
		}
		if (message != null) {
			try {
				console.writer().write(message);
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
		}
		super.flush();
	}

}
