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

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to define an action for command line evaluation.
 * <p>
 * An action can be of three different kinds:
 * <ul>
 * <li>Switch (e.g. --verbose)</li>
 * <li>Named Option (e.g. --file some_file.txt)</li>
 * <li>Unnamed Option (e.g. some_file.txt)</li>
 * </ul>
 * An action can be associated with one or more arguments (e.g --file and -f).
 * The parsing algorithm in class {@link CmdLine} assumes that arguments
 * prefixed with a single '-' are one char only (e.g. -f) or all other arguments
 * are either an unnamed option or are prefixed with '--' (e.g. --file).
 * <p>
 *
 * @see CmdLine#switchAction(java.util.function.Consumer)
 * @see CmdLine#namedOptionAction(java.util.function.BiConsumer)
 * @see CmdLine#unnamedOptionAction(java.util.function.Consumer)
 */
public abstract class CmdLineAction {

	private Set<String> args = new HashSet<>();

	/**
	 * Assign an argument string to this action.
	 *
	 * @param arg The argument string to add.
	 * @return The updated {@code CmdLineAction}.
	 */
	public CmdLineAction arg(String arg) {
		assert arg != null;
		assert (arg.length() == 2 && arg.charAt(0) == '-' && arg.charAt(1) != '-')
				|| (arg.length() > 2 && arg.startsWith("--"));

		this.args.add(arg);
		return this;
	}

	boolean match(String arg) {
		assert this.args.size() > 0;

		return this.args.contains(arg);
	}

}
