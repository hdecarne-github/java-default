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

import java.util.logging.Level;

/**
 * Custom level class for supporting the level names as defined by the {@link Log} class.
 */
public final class LogLevel extends Level {

	/**
	 * Serialization support.
	 */
	private static final long serialVersionUID = -5677630927546103691L;

	/**
	 * Level Notice
	 */
	public static final LogLevel LEVEL_NOTICE = new LogLevel("LEVEL_NOTICE", Level.OFF.intValue());

	/**
	 * Level Error
	 */
	public static final LogLevel LEVEL_ERROR = new LogLevel("LEVEL_ERROR", Level.SEVERE.intValue());

	/**
	 * Level Warning
	 */
	public static final LogLevel LEVEL_WARNING = new LogLevel("LEVEL_WARNING", Level.WARNING.intValue());

	/**
	 * Level Info
	 */
	public static final LogLevel LEVEL_INFO = new LogLevel("LEVEL_INFO", Level.INFO.intValue());

	/**
	 * Level Debug
	 */
	public static final LogLevel LEVEL_DEBUG = new LogLevel("LEVEL_DEBUG", Level.FINE.intValue());

	/**
	 * Level Trace
	 */
	public static final LogLevel LEVEL_TRACE = new LogLevel("LEVEL_TRACE", Level.FINEST.intValue());

	private LogLevel(String name, int value) {
		super(name, value);
	}

	/*
	 * Dummy function enabling {@link LogConfig} to access this class and by this trigger the registration of the custom
	 * levels defined above.
	 */
	static void init() {
		// Nothing to do here
	}

}
