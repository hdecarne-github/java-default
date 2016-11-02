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
 * This class is used to monitor and collect log messages in a selective manner
 * (for example to display them to the user after a long running task).
 */
public class LogMonitor extends Handler implements AutoCloseable {

	private final static int BUFFER_LIMIT = PropertiesHelper.getInt(LogMonitor.class, ".limit", 100);

	private final Collection<Logger> loggers = new ArrayList<>();

	private final Set<Long> threadIds = new HashSet<>();

	private final Deque<LogRecord> buffer = new LinkedList<>();

	private final LogLevel level;

	/**
	 * Construct {@code LogMonitor}.
	 *
	 * @param level The log level to use for monitoring.
	 */
	public LogMonitor(LogLevel level) {
		this.level = level;
	}

	/**
	 * Include a {@link Log} in monitoring.
	 *
	 * @param log The log to monitor.
	 */
	public void includeLog(Log log) {
		assert log != null;

		includeLogger(log.getLogger());
	}

	/**
	 * Include the loggers for a specific {@link Package} in monitoring.
	 *
	 * @param pkg The package to monitor.
	 */
	public void includePackage(Package pkg) {
		assert pkg != null;

		includeLogger(Logger.getLogger(pkg.getName()));
	}

	/**
	 * Include a {@link Logger} in monitoring.
	 *
	 * @param logger The logger to monitor.
	 */
	public synchronized void includeLogger(Logger logger) {
		assert logger != null;

		this.loggers.add(logger);
		logger.addHandler(this);
	}

	/**
	 * Include a {@link Thread} in monitoring.
	 *
	 * @param thread The thread to monitor.
	 * @see #excludeThread(Thread)
	 */
	public synchronized void includeThread(Thread thread) {
		assert thread != null;

		this.threadIds.add(thread.getId());
	}

	/**
	 * Exclude a {@link Thread} from monitoring.
	 *
	 * @param thread The thread to remove.
	 * @see #includeThread(Thread)
	 */
	public synchronized void excludeThread(Thread thread) {
		assert thread != null;

		this.threadIds.remove(thread.getId());
	}

	/**
	 * Check whether this monitor's buffer is empty and does not contain any
	 * records.
	 *
	 * @return {@code true} if this monitor's buffer is empty and does not
	 *         contain any records.
	 */
	public synchronized boolean isEmpty() {
		return this.buffer.isEmpty();
	}

	/**
	 * Get the {@link LogRecord}s collected by this monitor.
	 *
	 * @return The {@link LogRecord}s collected by this monitor.
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
