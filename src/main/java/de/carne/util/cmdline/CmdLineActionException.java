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
package de.carne.util.cmdline;

import de.carne.check.Nullable;

/**
 * This exception indicates a problem while invoking a command line action {@link CmdLineAction}.
 * <p>
 * This exception can be thrown by the command line action's consumer expressions to indicate a problem during command
 * line evaluation. The command line evaluation is stopped as soon as this exception is encountered.
 */
public class CmdLineActionException extends RuntimeException {

	/**
	 * Serialization support.
	 */
	private static final long serialVersionUID = -2075836295566247433L;

	private final String arg;

	@Nullable
	private final String option;

	/**
	 * Construct {@code CmdLineActionException}.
	 *
	 * @param arg The causing argument.
	 */
	public CmdLineActionException(String arg) {
		this(arg, null, null);
	}

	/**
	 * Construct {@code CmdLineActionException}.
	 *
	 * @param arg The causing argument.
	 * @param cause The causing exception.
	 */
	public CmdLineActionException(String arg, Throwable cause) {
		this(arg, null, cause);
	}

	/**
	 * Construct {@code CmdLineActionException}.
	 *
	 * @param arg The causing argument.
	 * @param option The option argument option.
	 */
	public CmdLineActionException(String arg, String option) {
		this(arg, option, null);
	}

	/**
	 * Construct {@code CmdLineActionException}.
	 *
	 * @param arg The causing argument.
	 * @param option The option argument option.
	 * @param cause The causing exception.
	 */
	public CmdLineActionException(String arg, @Nullable String option, @Nullable Throwable cause) {
		super(cause);
		this.arg = arg;
		this.option = option;
	}

	/**
	 * Get the causing argument.
	 *
	 * @return The causing argument.
	 */
	public String getArg() {
		return this.arg;
	}

	/**
	 * Get the argument option (if existent).
	 *
	 * @return The argument option (may be {@code null}).
	 */
	@Nullable
	public String getOption() {
		return this.option;
	}

}
