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

import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Wrapper class for the JDK's {@link Logger} class to make logging easy and efficient.
 */
public final class Log {

	private final Logger logger;

	/**
	 * Construct {@link Log}.
	 * <p>
	 * The created {@link Logger} is named after the calling class' name.
	 */
	public Log() {
		this(Logger.getLogger(getCallerClassName()));
	}

	/**
	 * Construct {@link Log}.
	 * <p>
	 * The created {@link Logger} is named after the calling class' name.
	 *
	 * @param resourceBundle The {@link ResourceBundle} to use for log message localization.
	 */
	public Log(ResourceBundle resourceBundle) {
		this(Logger.getLogger(getCallerClassName(), resourceBundle.getBaseBundleName()));
	}

	private Log(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Get the {@link Logger} represented by this instance.
	 *
	 * @return The {@link Logger} represented by this instance.
	 */
	public Logger logger() {
		return this.logger;
	}

	private static String getCallerClassName() {
		int steIndex = 0;
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		String myClassName = Log.class.getName();

		while (steIndex < stes.length && !myClassName.equals(stes[steIndex].getClassName())) {
			steIndex++;
		}
		while (steIndex < stes.length && myClassName.equals(stes[steIndex].getClassName())) {
			steIndex++;
		}
		return (steIndex < stes.length ? stes[steIndex].getClassName() : myClassName);
	}

}
