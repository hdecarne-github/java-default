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
 * This exception indicates a problem during command line evaluation.
 *
 * @see CmdLine#eval()
 */
public class CmdLineException extends Exception {

	/**
	 * Serialization support.
	 */
	private static final long serialVersionUID = -8194539455201796523L;

	private final String arg;

	@Nullable
	private final String option;

	CmdLineException(CmdLineActionException cause) {
		super(cause.getCause());
		this.arg = cause.getArg();
		this.option = cause.getOption();
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
