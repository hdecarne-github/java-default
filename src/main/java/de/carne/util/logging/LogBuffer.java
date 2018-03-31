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
package de.carne.util.logging;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import de.carne.check.Nullable;

/**
 * {@linkplain Handler} implementation used to buffer {@linkplain LogRecord}s (e.g. to access log messages issued during
 * application startup).
 */
public class LogBuffer extends Handler {

	private final int limit;
	private final Queue<LogRecord> buffer = new ArrayDeque<>();
	private final Set<Handler> handlers = new HashSet<>();
	private final AtomicBoolean locked = new AtomicBoolean();

	/**
	 * Construct {@linkplain LogBuffer}.
	 */
	public LogBuffer() {
		LogManager manager = LogManager.getLogManager();
		String propertyBase = getClass().getName();

		this.limit = Logs.getIntProperty(manager, propertyBase + ".limit", 1000);
		setLevel(Logs.getLevelProperty(manager, propertyBase + ".level", LogLevel.LEVEL_WARNING));
		setFilter(Logs.getFilterProperty(manager, propertyBase + ".filter", null));
	}

	/**
	 * Get the {@linkplain LogBuffer} attached to the submitted {@linkplain Log} (if any).
	 *
	 * @param log The {@linkplain Log} to get the {@linkplain LogBuffer} for.
	 * @return The found {@linkplain LogBuffer} or {@code null} if none has been configured.
	 */
	@Nullable
	public static LogBuffer get(Log log) {
		return get(log.logger());
	}

	/**
	 * Get the {@linkplain LogBuffer} attached to the submitted {@linkplain Logger} (if any).
	 *
	 * @param logger The {@linkplain Logger} to get the {@linkplain LogBuffer} for.
	 * @return The found {@linkplain LogBuffer} or {@code null} if none has been configured.
	 */
	@Nullable
	public static LogBuffer get(Logger logger) {
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
	 * Add a {@linkplain Handler} to a {@linkplain LogBuffer} for {@linkplain LogRecord} consuming.
	 * <p>
	 * If the submitted {@linkplain Log} has no {@linkplain LogBuffer} attached the call is ignored.
	 *
	 * @param log The {@linkplain Log} identifying the {@linkplain LogBuffer} to add to.
	 * @param handler The {@linkplain Handler} to add.
	 * @see #get(Log)
	 * @see #addHandler(Handler)
	 */
	public static void addHandler(Log log, Handler handler) {
		addHandler(log.logger(), handler);
	}

	/**
	 * Add a {@linkplain Handler} to a {@linkplain LogBuffer} for {@linkplain LogRecord} consuming.
	 * <p>
	 * If the submitted {@linkplain Logger} has no {@linkplain LogBuffer} attached the call is ignored.
	 *
	 * @param logger The {@linkplain Logger} identifying the {@linkplain LogBuffer} to add to.
	 * @param handler The {@linkplain Handler} to add.
	 * @see #get(Logger)
	 * @see #addHandler(Handler)
	 */
	public static void addHandler(Logger logger, Handler handler) {
		LogBuffer logBuffer = get(logger);

		if (logBuffer != null) {
			logBuffer.addHandler(handler);
		}
	}

	/**
	 * Add a {@linkplain Handler} to the {@linkplain LogBuffer} for {@linkplain LogRecord} consuming.
	 * <p>
	 * Any already buffered {@linkplain LogRecord} is sent to the {@linkplain Handler} during this operation.
	 *
	 * @param handler The {@linkplain Handler} to add.
	 */
	public synchronized void addHandler(Handler handler) {
		for (LogRecord record : this.buffer) {
			handler.publish(record);
		}
		this.handlers.add(handler);
	}

	/**
	 * Remove a previously added {@linkplain Handler} from a {@linkplain LogBuffer}.
	 * <p>
	 * If the submitted {@linkplain Log} has no {@linkplain LogBuffer} attached the call is ignored.
	 *
	 * @param log The {@linkplain Log} identifying the {@linkplain LogBuffer} to remove from.
	 * @param handler The {@linkplain Handler} to remove.
	 * @see #addHandler(Log, Handler)
	 * @see #get(Log)
	 * @see #removeHandler(Handler)
	 */
	public static void removeHandler(Log log, Handler handler) {
		removeHandler(log.logger(), handler);
	}

	/**
	 * Remove a previously added {@linkplain Handler} from a {@linkplain LogBuffer}.
	 * <p>
	 * If the submitted {@linkplain Logger} has no {@linkplain LogBuffer} attached the call is ignored.
	 *
	 * @param logger The {@linkplain Logger} identifying the {@linkplain LogBuffer} to remove from.
	 * @param handler The {@linkplain Handler} to remove.
	 * @see #addHandler(Logger, Handler)
	 * @see #get(Logger)
	 * @see #removeHandler(Handler)
	 */
	public static void removeHandler(Logger logger, Handler handler) {
		LogBuffer logBuffer = get(logger);

		if (logBuffer != null) {
			logBuffer.removeHandler(handler);
		}
	}

	/**
	 * Remove a previously added {@linkplain Handler} from the {@linkplain LogBuffer}.
	 * <p>
	 * After removal the {@linkplain Handler} will no longer receive any {@linkplain LogRecord}s.
	 *
	 * @param handler The {@linkplain Handler} to remove.
	 * @see #addHandler(Handler)
	 */
	public synchronized void removeHandler(Handler handler) {
		this.handlers.remove(handler);
	}

	/**
	 * Perform a flush operation on a {@linkplain LogBuffer}.
	 * <p>
	 * The flush request is forwarded to any currently registered {@linkplain Handler} and any buffered
	 * {@linkplain LogRecord} will be discarded. If the submitted {@linkplain Logger} has no {@linkplain LogBuffer}
	 * attached the call is ignored.
	 *
	 * @param log The {@linkplain Log} identifying the {@linkplain LogBuffer} to flush.
	 * @see #get(Log)
	 */
	public static void flush(Log log) {
		flush(log.logger());
	}

	/**
	 * Perform a flush operation on a {@linkplain LogBuffer}.
	 * <p>
	 * The flush request is forwarded to any currently registered {@linkplain Handler} and any buffered
	 * {@linkplain LogRecord} will be discarded. If the submitted {@linkplain Logger} has no {@linkplain LogBuffer}
	 * attached the call is ignored.
	 *
	 * @param logger The {@linkplain Logger} identifying the {@linkplain LogBuffer} to flush.
	 * @see #get(Logger)
	 */
	public static void flush(Logger logger) {
		LogBuffer logBuffer = get(logger);

		if (logBuffer != null) {
			logBuffer.flush();
		}
	}

	@Override
	public void publish(@Nullable LogRecord record) {
		if (record != null && isLoggable(record) && this.locked.compareAndSet(false, true)) {
			try {
				synchronized (this) {
					while (this.buffer.size() >= this.limit) {
						this.buffer.remove();
					}
					this.buffer.add(record);
					this.handlers.forEach(handler -> handler.publish(record));
				}
			} finally {
				this.locked.set(false);
			}
		}
	}

	@Override
	public synchronized void flush() {
		this.handlers.forEach(Handler::flush);
		this.buffer.clear();
	}

	@Override
	public synchronized void close() {
		this.handlers.forEach(Handler::close);
		this.handlers.clear();
	}

}
