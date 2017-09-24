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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import de.carne.check.Nullable;

/**
 * Utility class for command line processing.
 * <p>
 * An instance of this class wraps a single command line.
 * <p>
 * Actions can be added to define how the encountered arguments are processed. The following action types are supported:
 * <ul>
 * <li>switch (e.g. {@code --switch} or {@code -s}; see {@link #onSwitch(Consumer)})</li>
 * <li>option (e.g. {@code --option value} or {@code -o value}; see {@link #onOption(BiConsumer)})</li>
 * <li>unnamed (e.g. {@code file.txt)}; see {@link #onUnnamedOption(Consumer)})</li>
 * <li>unknown (default processing; see {@link #onUnknownArg(Consumer)})
 * </ul>
 * Invoking the {@link #process()} function processes all arguments and invokes the corresponding actions.
 */
public final class CmdLineProcessor {

	private static final Pattern ACTION_ARG_PATTERN = Pattern.compile("(-[^-\\s])|(--[^-\\s]+)");

	private final String cmd;

	private final Iterable<String> args;

	private final List<SwitchCmdLineAction> switchActions = new ArrayList<>();

	private final List<OptionCmdLineAction> optionActions = new ArrayList<>();

	@Nullable
	private Consumer<String> unnamedAction = null;

	@Nullable
	private Consumer<String> unknownAction = null;

	/**
	 * Construct {@link CmdLineProcessor}.
	 *
	 * @param cmd The command executing the command line (used by {@link #toString()} to build a complete command line
	 *        string).
	 * @param args The command line to process.
	 */
	public CmdLineProcessor(String cmd, String[] args) {
		this(cmd, Arrays.asList(args));
	}

	/**
	 * Construct {@link CmdLineProcessor}.
	 *
	 * @param cmd The command executing the command line.
	 * @param args The command line to process.
	 */
	public CmdLineProcessor(String cmd, Iterable<String> args) {
		this.cmd = cmd;
		this.args = args;
	}

	/**
	 * Check whether an argument string is a valid action argument.
	 * <p>
	 * An action argument must be of the form single '-' and character (e.g. {@code -a}) or double '-' and a name (e.g.
	 * {@code --argument}).
	 *
	 * @param arg The argument string to check.
	 * @return {@code true} if the argument string is a valid action argument.
	 */
	public static boolean isActionArg(String arg) {
		return ACTION_ARG_PATTERN.matcher(arg).matches();
	}

	/**
	 * Process the command line and invoke the correspond actions.
	 *
	 * @throws CmdLineException if the command line contains an error.
	 * @see #onSwitch(Consumer)
	 * @see #onOption(BiConsumer)
	 * @see #onUnnamedOption(Consumer)
	 * @see #onUnknownArg(Consumer)
	 */
	public void process() throws CmdLineException {
		OptionCmdLineAction pendingOptionAction = null;
		String pendingArg = null;

		for (String arg : this.args) {
			// Check whether there is a pending named option waiting for completion.
			// If this is the case, check and invoke the action.
			if (pendingOptionAction != null && pendingArg != null) {
				if (isActionArg(arg)) {
					throw new CmdLineException(this, pendingArg);
				}
				pendingOptionAction.accept(pendingArg, arg);
				pendingOptionAction = null;
				pendingArg = null;
				continue;
			}

			// Check whether the argument is a known option argument.
			// If this is the case, remember it as pending and continue (processing will be done above).
			Optional<OptionCmdLineAction> optOptionAction = this.optionActions.stream()
					.filter(action -> action.contains(arg)).findFirst();

			if (optOptionAction.isPresent()) {
				OptionCmdLineAction optionAction = optOptionAction.get();

				pendingOptionAction = optionAction;
				pendingArg = arg;
				continue;
			}

			// Check whether the argument is a known switch argument.
			// If this is the case, invoke it.
			Optional<SwitchCmdLineAction> optSwitchAction = this.switchActions.stream()
					.filter(action -> action.contains(arg)).findFirst();

			if (optSwitchAction.isPresent()) {
				SwitchCmdLineAction switchAction = optSwitchAction.get();

				switchAction.accept(arg);
				continue;
			}

			// No action found so far. Invoke the corresponding default action.
			Consumer<String> defaultAction = (isActionArg(arg) ? this.unknownAction : this.unnamedAction);

			if (defaultAction != null) {
				defaultAction.accept(arg);
			} else {
				throw new CmdLineException(this, arg);
			}
		}
		if (pendingOptionAction != null && pendingArg != null) {
			throw new CmdLineException(this, pendingArg);
		}
	}

	/**
	 * Add a {@link CmdLineAction} for switch argument ({@code e.g. --switch}) processing.
	 *
	 * @param action The {@link Consumer} to invoke with the argument string.
	 * @return The created {@link CmdLineAction}.
	 */
	public CmdLineAction onSwitch(Consumer<String> action) {
		SwitchCmdLineAction switchAction = new SwitchCmdLineAction(action);

		this.switchActions.add(switchAction);
		return switchAction;
	}

	/**
	 * Add a {@link CmdLineAction} for option argument ({@code e.g. --option value}) processing.
	 *
	 * @param action The {@link BiConsumer} to invoke with the argument and option string.
	 * @return The created {@link CmdLineAction}.
	 */
	public CmdLineAction onOption(BiConsumer<String, String> action) {
		OptionCmdLineAction optionAction = new OptionCmdLineAction(action);

		this.optionActions.add(optionAction);
		return optionAction;
	}

	/**
	 * Add an action for unnamed options (e.g. {@code file.txt}).
	 * <p>
	 * If no action is defined for unnamed options the command line processing will fail in case an unnamed option is
	 * encountered.
	 *
	 * @param action The {@link Consumer} to invoke with the option string.
	 */
	public void onUnnamedOption(Consumer<String> action) {
		this.unnamedAction = action;
	}

	/**
	 * Add an action for unknown arguments (e.g. {@code --unknown}).
	 * <p>
	 * If no action is defined for unknown arguments the command line processing will fail in case an unknown arguments
	 * is encountered.
	 *
	 * @param action The {@link Consumer} to invoke with the argument string.
	 */
	public void onUnknownArg(Consumer<String> action) {
		this.unknownAction = action;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(this.cmd);
		for (String arg : this.args) {
			buffer.append(" ");
			buffer.append(arg);
		}
		return buffer.toString();
	}

	private class SwitchCmdLineAction extends CmdLineAction implements Consumer<String> {

		private final Consumer<String> action;

		SwitchCmdLineAction(Consumer<String> action) {
			this.action = action;
		}

		@Override
		public void accept(@Nullable String t) {
			this.action.accept(t);
		}

	}

	private class OptionCmdLineAction extends CmdLineAction implements BiConsumer<String, String> {

		private final BiConsumer<String, String> action;

		OptionCmdLineAction(BiConsumer<String, String> action) {
			this.action = action;
		}

		@Override
		public void accept(@Nullable String t, @Nullable String u) {
			this.action.accept(t, u);
		}

	}

}
