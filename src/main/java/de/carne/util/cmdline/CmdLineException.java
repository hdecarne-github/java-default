/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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
	 * Constructs a new {@linkplain CmdLineException} instance.
	 *
	 * @param cmdLine the {@linkplain CmdLineProcessor} throwing this exception.
	 * @param arg the argument string causing this exception.
	 */
	public CmdLineException(CmdLineProcessor cmdLine, String arg) {
		super(exceptionMessage(cmdLine, arg));
		this.cmdLine = cmdLine.toString();
		this.arg = arg;
	}

	/**
	 * Constructs a new {@linkplain CmdLineException} instance.
	 *
	 * @param cmdLine the {@linkplain CmdLineProcessor} throwing this exception.
	 * @param arg the argument string causing this exception.
	 * @param cause the causing exception.
	 */
	public CmdLineException(CmdLineProcessor cmdLine, String arg, Throwable cause) {
		super(exceptionMessage(cmdLine, arg), cause);
		this.cmdLine = cmdLine.toString();
		this.arg = arg;
	}

	private static String exceptionMessage(CmdLineProcessor cmdLine, String arg) {
		return "Unable to process command line '" + cmdLine + "' (processing failed at '" + arg + "')";
	}

	/**
	 * Gets the command line string for which the processing failed.
	 *
	 * @return the command line string for which the processing failed.
	 */
	public String cmdLine() {
		return this.cmdLine;
	}

	/**
	 * Gets the argument string causing the processing failure.
	 *
	 * @return the argument string causing the processing failure.
	 */
	public String arg() {
		return this.arg;
	}

}
