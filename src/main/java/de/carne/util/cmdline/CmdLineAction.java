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
import java.util.stream.Collectors;

/**
 * This class represents an action associated with a set of recognized command line args.
 * <p>
 * An action can either be a switch (e.g. {@code --switch}) or an option (e.g. {«code --option value}).
 *
 * @see CmdLineProcessor#onSwitch(java.util.function.Consumer)
 * @see CmdLineProcessor#onOption(java.util.function.BiConsumer)
 */
public abstract class CmdLineAction {

	private final Set<String> args = new HashSet<>();

	/**
	 * Add an argument string that should be trigger this {@link CmdLineAction}.
	 *
	 * @param arg The argument string to be associated with this {@link CmdLineAction}.
	 * @return The updated {@link CmdLineAction}.
	 */
	public CmdLineAction arg(String arg) {
		if (!CmdLineProcessor.isActionArg(arg)) {
			throw new IllegalArgumentException(arg);
		}
		this.args.add(arg);
		return this;
	}

	/**
	 * Check whether the submitted argument string is a trigger for this {@link CmdLineAction}.
	 *
	 * @param arg The argument string to check.
	 * @return {@code true} if the argument string is associated with this {@link CmdLineAction}.
	 * @see #arg(String)
	 */
	public boolean contains(String arg) {
		return this.args.contains(arg);
	}

	@Override
	public String toString() {
		return "'" + this.args.stream().collect(Collectors.joining("|")) + "'";
	}

}
