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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import de.carne.util.PropertiesHelper;

/**
 * This class is used to collect log messages in a selective manner (for example
 * to display it to the user after a long running task which can collect several
 * warnings/message or the like).
 */
public class LogBuffer extends Handler implements AutoCloseable {

	private final static int BUFFER_LIMIT = PropertiesHelper.getInt(LogBuffer.class, ".limit", 100);

	private final Collection<Logger> loggers = new ArrayList<>();

	private final Set<Long> threadIds = new HashSet<>();

	private final Deque<LogRecord> buffer = new LinkedList<>();

	private final LogLevel level;

	/**
	 * Construct {@code LogBuffer}.
	 *
	 * @param level The log level to use for filtering.
	 */
	public LogBuffer(LogLevel level) {
		this.level = level;
	}

	/**
	 * Add a {@link Log} to the buffer for monitoring.
	 *
	 * @param log The log to add.
	 */
	public void includeLog(Log log) {
		assert log != null;

		includeLogger(log.getLogger());
	}

	/**
	 * Add all loggers for specific {@link Package} to the buffer for
	 * monitoring.
	 *
	 * @param pkg The package to add.
	 */
	public void includePackage(Package pkg) {
		assert pkg != null;

		includeLogger(Logger.getLogger(pkg.getName()));
	}

	/**
	 * Add a {@link Logger} to the buffer for monitoring.
	 *
	 * @param logger The logger to add.
	 */
	public synchronized void includeLogger(Logger logger) {
		assert logger != null;

		this.loggers.add(logger);
		logger.addHandler(this);
	}

	/**
	 * Add a {@link Thread} to the buffer for monitoring.
	 *
	 * @param thread The thread to add.
	 * @see #excludeThread(Thread)
	 */
	public synchronized void includeThread(Thread thread) {
		assert thread != null;

		this.threadIds.add(thread.getId());
	}

	/**
	 * Remove a previously added {@link Thread} from the buffer.
	 *
	 * @param thread The thread to remove.
	 * @see #includeThread(Thread)
	 */
	public synchronized void excludeThread(Thread thread) {
		assert thread != null;

		this.threadIds.remove(thread.getId());
	}

	/**
	 * Check whether this buffer is empty and does not contain any records.
	 *
	 * @return {@code true} if this buffer is empty and does not contain any
	 *         records.
	 */
	public synchronized boolean isEmpty() {
		return this.buffer.isEmpty();
	}

	/**
	 * Get the {@link LogRecord} collected by this buffer.
	 *
	 * @return The {@link LogRecord} collected by this buffer.
	 */
	public synchronized Collection<LogRecord> getRecords() {
		return Collections.unmodifiableCollection(this.buffer);
	}

	@Override
	public synchronized void publish(LogRecord record) {
		if (record.getLevel().intValue() >= this.level.intValue()
				&& (this.threadIds.isEmpty() || this.threadIds.contains(record.getThreadID()))) {
			if (this.buffer.size() < BUFFER_LIMIT) {
				this.buffer.addLast(record);
			}
		}
	}

	@Override
	public void flush() {
		// Nothing to do here
	}

	@Override
	public void close() {
		for (Logger logger : this.loggers) {
			logger.removeHandler(this);
		}
		this.loggers.clear();
		this.buffer.clear();
	}

}
