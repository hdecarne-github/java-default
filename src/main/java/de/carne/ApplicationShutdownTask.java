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
package de.carne;

import java.util.ArrayList;
import java.util.List;

import de.carne.util.logging.Log;

/**
 * Utility class used to perform necessary tasks during VM (Application) shutdown.
 */
public final class ApplicationShutdownTask {

	private static final Log LOG = new Log();

	private static final List<ApplicationShutdownTask> APPLICATION_CLEANUP_TASKS = new ArrayList<>();

	static {
		LOG.info("Registering shutdown hook...");

		Runtime.getRuntime().addShutdownHook(
				new Thread(ApplicationShutdownTask::trigger, ApplicationShutdownTask.class.getSimpleName()));
	}

	/**
	 * Register application shutdown task.
	 *
	 * @param name The task name to use for error handling and logging.
	 * @param runnable The task {@link Runnable} to invoke on shutdown.
	 */
	public static synchronized void register(String name, Runnable runnable) {
		APPLICATION_CLEANUP_TASKS.add(new ApplicationShutdownTask(name, runnable));
	}

	/**
	 * Trigger invocation of the currently registered shutdown tasks.
	 */
	public static synchronized void trigger() {
		LOG.info("Triggering shutdown tasks...");

		for (ApplicationShutdownTask task : APPLICATION_CLEANUP_TASKS) {
			task.run();
		}
	}

	private final String name;
	private final Runnable runnable;
	private boolean triggered = false;

	private ApplicationShutdownTask(String name, Runnable runnable) {
		this.name = name;
		this.runnable = runnable;
	}

	private void run() {
		if (!this.triggered) {
			LOG.info(" {0}...", this.name);
			try {
				this.triggered = true;
				this.runnable.run();
			} catch (Exception e) {
				LOG.warning(e, "An exception occurred while running shutdown task {0}", this.name);
			}
		}
	}

}
