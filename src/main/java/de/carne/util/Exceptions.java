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
package de.carne.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import de.carne.util.logging.Log;

/**
 * Utility class providing {@link Exception} related functions.
 */
public final class Exceptions {

	private Exceptions() {
		// Make sure this class is not instantiated from outside
	}

	private static final Log LOG = new Log();

	/**
	 * Ignore an {@link Exception}.
	 * <p>
	 * This function simply logs the exception using the trace log level.
	 *
	 * @param exception The exception to ignore (may be {@code null}).
	 */
	public static void ignore(Throwable exception) {
		if (exception != null) {
			LOG.trace(exception, "Ignoring exception {0}", exception.getClass());
		}
	}

	/**
	 * Get an {@link Exception}'s stack trace.
	 *
	 * @param exception The exception to get the stack trace for.
	 * @return The exception's stack trace.
	 */
	public static String getStackTrace(Throwable exception) {
		assert exception != null;

		String stackTrace = "<no stack trace>";

		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			exception.printStackTrace(pw);
			pw.flush();
			stackTrace = sw.toString();
		} catch (IOException e) {
			ignore(e);
		}
		return stackTrace;
	}

}
