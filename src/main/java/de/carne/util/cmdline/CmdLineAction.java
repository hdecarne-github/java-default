/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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
 * </p>
 *
 * @see CmdLineProcessor#onSwitch(java.util.function.Consumer)
 * @see CmdLineProcessor#onOption(java.util.function.BiConsumer)
 */
public abstract class CmdLineAction {

	private final Set<String> args = new HashSet<>();

	/**
	 * Adds an argument string that should trigger this {@linkplain CmdLineAction} instance.
	 *
	 * @param arg the argument string to be associated with this {@linkplain CmdLineAction} instance.
	 * @return the updated {@linkplain CmdLineAction} instance.
	 */
	public CmdLineAction arg(String arg) {
		if (!CmdLineProcessor.isActionArg(arg)) {
			throw new IllegalArgumentException(arg);
		}
		this.args.add(arg);
		return this;
	}

	/**
	 * Checks whether the submitted argument string is a trigger for this {@linkplain CmdLineAction} instance.
	 *
	 * @param arg the argument string to check.
	 * @return {@code true} if the argument string is associated with this {@linkplain CmdLineAction} instance.
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
