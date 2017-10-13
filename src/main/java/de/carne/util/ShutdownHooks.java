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
package de.carne.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility class providing the same function as {@linkplain Runtime#addShutdownHook(Thread)} while sharing a single
 * {@linkplain Thread} for all hooks.
 */
public final class ShutdownHooks {

	private ShutdownHooks() {
		// Prevent instantiation
	}

	private static final List<Runnable> HOOKS = new LinkedList<>();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			synchronized (HOOKS) {
				for (Runnable hook : HOOKS) {
					try {
						hook.run();
					} catch (Exception e) {
						Exceptions.warn(e);
					}
				}
			}
		}, ShutdownHooks.class.getSimpleName()));
	}

	/**
	 * Add a shutdown hook to be invoked during VM shutdown.
	 *
	 * @param hook The shutdown hook to invoke.
	 * @see Runtime#addShutdownHook(Thread)
	 */
	public static void add(Runnable hook) {
		synchronized (HOOKS) {
			HOOKS.add(hook);
		}
	}

}
