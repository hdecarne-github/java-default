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

import de.carne.check.NonNullByDefault;
import de.carne.check.Nullable;
import de.carne.util.PropertiesHelper;

/**
 * This class is used to monitor and record log messages in a selective manner and only during a defined period (for
 * example to display them to the user after a long running task).
 */
@NonNullByDefault
public class LogMonitor extends Handler {

	private final static int BUFFER_LIMIT = PropertiesHelper.getInt(LogMonitor.class, ".limit", 100);

	private final Deque<LogRecord> buffer = new LinkedList<>();

	private final Set<Long> threadIds = new HashSet<>();

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
	 * Check whether this monitor's buffer is not empty and does contain any records.
	 *
	 * @return {@code true} if this monitor's buffer is not empty and does contain any records.
	 */
	public synchronized boolean notEmpty() {
		return !this.buffer.isEmpty();
	}

	/**
	 * Get the {@link LogRecord}s collected by this monitor.
	 *
	 * @return The {@link LogRecord}s collected by this monitor.
	 */
	public synchronized Collection<LogRecord> getRecords() {
		return Collections.unmodifiableCollection(this.buffer);
	}

	/**
	 * Start a monitoring session.
	 *
	 * @return The session object.
	 * @see Session
	 */
	public Session start() {
		return new Session();
	}

	@Override
	public synchronized void publish(@Nullable LogRecord record) {
		if (record != null && record.getLevel().intValue() >= this.level.intValue() && (this.threadIds.isEmpty()
				|| this.threadIds.contains(Long.valueOf(record.getThreadID() & 0xffffffffl)))) {
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
		this.buffer.clear();
	}

	/**
	 * This class represents a running monitoring session.
	 */
	public class Session implements AutoCloseable {

		private final Collection<Logger> loggers = new ArrayList<>();

		Session() {
			// Make sure this class is not instantiated from outside
		}

		/**
		 * Include the loggers for a specific {@link Package} in monitoring.
		 *
		 * @param pkg The package to monitor.
		 * @return This session.
		 */
		public Session includePackage(Package pkg) {
			assert pkg != null;

			return includeLogger(Logger.getLogger(pkg.getName()));
		}

		/**
		 * Include a {@link Log} in the monitoring session.
		 *
		 * @param log The log to monitor.
		 * @return This session.
		 */
		public Session includeLog(Log log) {
			assert log != null;

			return includeLogger(log.getLogger());
		}

		/**
		 * Include a {@link Logger} in the monitoring session.
		 *
		 * @param logger The logger to monitor.
		 * @return This session.
		 */
		public synchronized Session includeLogger(Logger logger) {
			assert logger != null;

			this.loggers.add(logger);
			logger.addHandler(LogMonitor.this);
			return this;
		}

		@Override
		public void close() {
			for (Logger logger : this.loggers) {
				logger.removeHandler(LogMonitor.this);
			}
			this.loggers.clear();
		}

	}

}
