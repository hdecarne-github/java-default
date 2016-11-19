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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.Nullable;

import de.carne.util.PropertiesHelper;

/**
 * {@link Handler} implementation used to buffer log records for later
 * publishing by one or more dynamically defined {@link Handler} instances.
 */
public class LogBuffer extends Handler {

	/**
	 * The maximum number of log records stored in the buffer.
	 */
	public final static int BUFFER_LIMIT = PropertiesHelper.getInt(LogMonitor.class, ".limit", 100);

	private final Deque<LogRecord> buffer = new ArrayDeque<>(BUFFER_LIMIT);

	private final Set<Handler> handlers = new HashSet<>();

	private final AtomicBoolean locked = new AtomicBoolean(false);

	/**
	 * Get the {@code LogBuffer} instance attached to a given {@link Logger}.
	 *
	 * @param logger The logger to get the log buffer for.
	 * @return The attached log buffer, or {@code null} if none has been
	 *         configured.
	 */
	@Nullable
	public static LogBuffer getInstance(Logger logger) {
		assert logger != null;

		LogBuffer logBuffer = null;
		Logger currentLogger = logger;

		while (logBuffer == null && currentLogger != null) {
			for (Handler handler : currentLogger.getHandlers()) {
				if (handler instanceof LogBuffer) {
					logBuffer = (LogBuffer) handler;
					break;
				}
			}
			currentLogger = currentLogger.getParent();
		}
		return logBuffer;
	}

	/**
	 * Get the {@code LogBuffer} and add a {@link Handler}.
	 * <p>
	 * If no log buffer has been configured for the submitted logger, this
	 * function does nothing.
	 *
	 * @param logger The logger to get the log buffer for.
	 * @param handler The handler to add.
	 * @see #getInstance(Logger)
	 * @see #addHandler(Handler)
	 */
	public static void addHandler(Logger logger, Handler handler) {
		LogBuffer logBuffer = getInstance(logger);

		if (logBuffer != null) {
			logBuffer.addHandler(handler);
		}
	}

	/**
	 * Add a {@link Handler}.
	 * <p>
	 * Any already buffered log records is published to the submitted handler.
	 *
	 * @param handler The handler to add.
	 */
	public synchronized void addHandler(Handler handler) {
		assert handler != null;

		for (LogRecord record : this.buffer) {
			handler.publish(record);
		}
		this.handlers.add(handler);
	}

	/**
	 * Get the {@code LogBuffer} and remove a {@link Handler}.
	 * <p>
	 * If no log buffer has been configured for the submitted logger, this
	 * function does nothing.
	 *
	 * @param logger The logger to get the log buffer for.
	 * @param handler The handler to remove.
	 * @see #getInstance(Logger)
	 * @see #removeHandler(Handler)
	 */
	public static void removeHandler(Logger logger, Handler handler) {
		LogBuffer logBuffer = getInstance(logger);

		if (logBuffer != null) {
			logBuffer.removeHandler(handler);
		}
	}

	/**
	 * Remove a {@link Handler}.
	 *
	 * @param handler The handler to remove.
	 */
	public synchronized void removeHandler(Handler handler) {
		assert handler != null;

		this.handlers.remove(handler);
	}

	/**
	 * Get the {@code LogBuffer} and clear it's content.
	 * <p>
	 * If no log buffer has been configured for the submitted logger, this
	 * function does nothing.
	 *
	 * @param logger The logger to get the log buffer for.
	 * @see #getInstance(Logger)
	 * @see #clear()
	 */
	public static void clear(Logger logger) {
		LogBuffer logBuffer = getInstance(logger);

		if (logBuffer != null) {
			logBuffer.clear();
		}
	}

	/**
	 * Clear this log buffer.
	 */
	public synchronized void clear() {
		this.buffer.clear();
	}

	/**
	 * Get the {@code LogBuffer} and export it's content.
	 * <p>
	 * If no log buffer has been configured for the submitted logger, this
	 * function does nothing.
	 *
	 * @param logger The logger to get the log buffer for.
	 * @param file The file to export to.
	 * @throws IOException if an I/O error occurs.
	 * @see #getInstance(Logger)
	 * @see #exportTo(File)
	 */
	public static void exportTo(Logger logger, File file) throws IOException {
		LogBuffer logBuffer = getInstance(logger);

		if (logBuffer != null) {
			logBuffer.exportTo(file);
		}
	}

	/**
	 * Export this log buffer's content to a file.
	 *
	 * @param file The file to export to.
	 * @throws IOException if an I/O error occurs.
	 */
	public synchronized void exportTo(File file) throws IOException {
		assert file != null;

		try (Writer writer = new FileWriter(file, false)) {
			LogLineFormatter formatter = new LogLineFormatter();

			for (LogRecord record : this.buffer) {
				writer.write(formatter.format(record));
			}
		}
	}

	@Override
	public void publish(LogRecord record) {
		if (this.locked.compareAndSet(false, true)) {
			synchronized (this) {
				try {
					while (this.buffer.size() >= BUFFER_LIMIT) {
						this.buffer.removeFirst();
					}
					this.buffer.addLast(record);
					this.handlers.forEach((handler) -> handler.publish(record));
				} finally {
					this.locked.set(false);
				}
			}
		}
	}

	@Override
	public void flush() {
		if (this.locked.compareAndSet(false, true)) {
			synchronized (this) {
				try {
					this.handlers.forEach((handler) -> handler.flush());
				} finally {
					this.locked.set(false);
				}
			}
		}
	}

	@Override
	public void close() throws SecurityException {
		if (this.locked.compareAndSet(false, true)) {
			synchronized (this) {
				try {
					this.handlers.forEach((handler) -> handler.close());
				} finally {
					this.locked.set(false);
				}
			}
		}
	}

}
