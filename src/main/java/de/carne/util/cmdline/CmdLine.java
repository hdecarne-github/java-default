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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import de.carne.check.Nullable;
import de.carne.util.Strings;

/**
 * Utility class for command line evaluation.
 */
public final class CmdLine {

	private final Iterable<String> args;

	private final List<CmdLineActionConsumer> switchActions = new ArrayList<>();

	private final List<CmdLineActionBiConsumer> namedOptionActions = new ArrayList<>();

	@Nullable
	private CmdLineActionConsumer unnamedOptionAction = null;

	@Nullable
	private CmdLineActionConsumer unknownArgumentAction = null;

	/**
	 * Construct {@code CmdLine}.
	 *
	 * @param args The command line to evaluate.
	 */
	public CmdLine(String[] args) {
		this.args = Arrays.asList(args);
	}

	/**
	 * Construct {@code CmdLine}.
	 *
	 * @param args The command line to evaluate.
	 */
	public CmdLine(Iterable<String> args) {
		this.args = args;
	}

	/**
	 * Evaluate the command line and invoke the corresponding actions.
	 *
	 * @throws CmdLineException If one of the actions fails with a {@link CmdLineActionException}.
	 */
	public void eval() throws CmdLineException {
		CmdLineActionBiConsumer pendingNamedOptionAction = null;
		String pendingArg = null;

		try {
			for (String arg : this.args) {
				// Ignore empty strings
				if (Strings.isEmpty(arg)) {
					continue;
				}

				// Check if this argument is an option for a previously
				// encountered
				// named option argument and if this the case, invoke the action
				if (pendingNamedOptionAction != null) {
					pendingNamedOptionAction.accept(pendingArg, arg);
					pendingNamedOptionAction = null;
					pendingArg = null;
					continue;
				}

				// Check if the argument is a named option argument and if this
				// is
				// the case, continue with the next argument
				pendingNamedOptionAction = matchAction(this.namedOptionActions, arg);
				if (pendingNamedOptionAction != null) {
					pendingArg = arg;
					continue;
				}

				// Check if the argument is switch option argument and if this
				// is the case, invoke the action
				CmdLineActionConsumer matchingSwitchAction = matchAction(this.switchActions, arg);

				if (matchingSwitchAction != null) {
					matchingSwitchAction.accept(arg);
					continue;
				}
				// Must be an unnamed option
				if (this.unnamedOptionAction != null) {
					this.unnamedOptionAction.accept(arg);
					continue;
				}
				// We found no suitable action
				if (this.unknownArgumentAction != null) {
					this.unknownArgumentAction.accept(arg);
				}
			}
			if (pendingNamedOptionAction != null) {
				pendingNamedOptionAction.accept(pendingArg, "");
				pendingNamedOptionAction = null;
				pendingArg = null;
			}
		} catch (CmdLineActionException e) {
			throw new CmdLineException(e);
		}
	}

	@Nullable
	private static <T extends CmdLineAction> T matchAction(List<T> actions, String arg) {
		T matchingAction = null;

		for (T action : actions) {
			if (action.match(arg)) {
				matchingAction = action;
				break;
			}
		}
		return matchingAction;
	}

	/**
	 * Register a switch action.
	 *
	 * @param consumer The consumer to be invoked with the argument if this action matches during command line
	 *        evaluation.
	 * @return The registered action.
	 * @see CmdLineAction
	 */
	public CmdLineAction switchAction(Consumer<String> consumer) {
		assert consumer != null;

		CmdLineActionConsumer switchAction = new CmdLineActionConsumer(consumer);

		this.switchActions.add(switchAction);
		return switchAction;
	}

	/**
	 * Register a named option action.
	 *
	 * @param consumer The consumer to be invoked with the argument and the option if this action matches during command
	 *        line evaluation.
	 * @return The registered action.
	 * @see CmdLineAction
	 */
	public CmdLineAction namedOptionAction(BiConsumer<String, String> consumer) {
		assert consumer != null;

		CmdLineActionBiConsumer namedOptionAction = new CmdLineActionBiConsumer(consumer);

		this.namedOptionActions.add(namedOptionAction);
		return namedOptionAction;
	}

	/**
	 * Register an unnamed option action.
	 *
	 * @param consumer The consumer to be invoked with the option if this action matches during command line evaluation.
	 */
	public void unnamedOptionAction(Consumer<String> consumer) {
		assert consumer != null;

		this.unnamedOptionAction = new CmdLineActionConsumer(consumer);
	}

	/**
	 * Register an action for unknown arguments.
	 *
	 * @param consumer The consumer to be invoked if an argument is encountered during evaluation which matches no
	 *        action.
	 */
	public void unknownArgument(Consumer<String> consumer) {
		assert consumer != null;

		this.unknownArgumentAction = new CmdLineActionConsumer(consumer);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		for (String arg : this.args) {
			if (buffer.length() > 0) {
				buffer.append(" ");
			}
			buffer.append(arg);
		}
		return Strings.safe(buffer.toString());
	}

}
