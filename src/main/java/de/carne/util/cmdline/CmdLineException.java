/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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

/**
 * This {@linkplain Exception} indicates an error during command line processing.
 *
 * @see CmdLineProcessor
 */
public class CmdLineException extends Exception {

	private static final long serialVersionUID = 5627984772758323334L;

	private final String cmdLine;

	private final String arg;

	/**
	 * Construct {@linkplain CmdLineException}.
	 *
	 * @param cmdLine The {@linkplain CmdLineProcessor} throwing this exception.
	 * @param arg The argument string causing this exception.
	 */
	public CmdLineException(CmdLineProcessor cmdLine, String arg) {
		super("Unable to process command line '" + cmdLine + "' (processing failed at '" + arg + "')");
		this.cmdLine = cmdLine.toString();
		this.arg = arg;
	}

	/**
	 * Get the command line string for which the processing failed.
	 *
	 * @return The command line string for which the processing failed.
	 */
	public String cmdLine() {
		return this.cmdLine;
	}

	/**
	 * Get the argument string causing the processing failure.
	 *
	 * @return The argument string causing the processing failure.
	 */
	public String arg() {
		return this.arg;
	}

}
