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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class for command line processing.
 * <p>
 * An instance of this class wraps a single command line.
 * <p>
 * Actions can be added to define how the encountered arguments are processed. The following action types are supported:
 * <ul>
 * <li>switch (e.g. {@code --switch} or {@code -s}; see {@linkplain #onSwitch(Consumer)})</li>
 * <li>option (e.g. {@code --option value} or {@code -o value}; see {@linkplain #onOption(BiConsumer)})</li>
 * <li>unnamed (e.g. {@code file.txt)}; see {@linkplain #onUnnamedOption(Consumer)})</li>
 * <li>unknown (default processing; see {@linkplain #onUnknownArg(Consumer)})
 * </ul>
 * Invoking the {@linkplain #process()} function processes all arguments and invokes the corresponding actions.
 */
public final class CmdLineProcessor {

	private static final Pattern ACTION_ARG_PATTERN = Pattern.compile("(-[^-\\s])|(--[^-\\s]+)");

	private final String cmd;

	private final Iterable<String> args;

	private final List<SwitchCmdLineAction> switchActions = new ArrayList<>();

	private final List<OptionCmdLineAction> optionActions = new ArrayList<>();

	private @Nullable Consumer<String> unnamedAction = null;

	private @Nullable Consumer<String> unknownAction = null;

	/**
	 * Constructs a new {@linkplain CmdLineProcessor} instance.
	 *
	 * @param cmd the command executing the command line (used by {@linkplain #toString()} to build a complete command
	 * line string).
	 * @param args the command line to process.
	 */
	public CmdLineProcessor(String cmd, @NonNull String[] args) {
		this(cmd, Arrays.asList(args));
	}

	/**
	 * Constructs a new {@linkplain CmdLineProcessor} instance.
	 *
	 * @param cmd the command executing the command line.
	 * @param args the command line to process.
	 */
	public CmdLineProcessor(String cmd, Iterable<String> args) {
		this.cmd = cmd;
		this.args = args;
	}

	/**
	 * Checks whether an argument string is a valid action argument.
	 * <p>
	 * An action argument must be of the form single '-' and character (e.g. {@code -a}) or double '-' and a name (e.g.
	 * {@code --argument}).
	 *
	 * @param arg the argument string to check.
	 * @return {@code true} if the argument string is a valid action argument.
	 */
	public static boolean isActionArg(String arg) {
		return ACTION_ARG_PATTERN.matcher(arg).matches();
	}

	/**
	 * Processes the command line and invoke the correspond actions.
	 *
	 * @throws CmdLineException if the command line contains an error.
	 * @see #onSwitch(Consumer)
	 * @see #onOption(BiConsumer)
	 * @see #onUnnamedOption(Consumer)
	 * @see #onUnknownArg(Consumer)
	 */
	public void process() throws CmdLineException {
		ProcessingContext context = new ProcessingContext();

		for (String arg : this.args) {
			if (!context.processPendingOptionAction(arg) && !context.processOptionAction(arg, this.optionActions)
					&& !context.processSwitchAction(arg, this.switchActions)) {
				// No action found so far. Invoke the corresponding default action.
				Consumer<String> defaultAction = (isActionArg(arg) ? this.unknownAction : this.unnamedAction);

				if (defaultAction != null) {
					try {
						defaultAction.accept(arg);
					} catch (RuntimeException e) {
						throw new CmdLineException(this, arg, e);
					}
				} else {
					throw new CmdLineException(this, arg);
				}
			}
		}
		context.verifyNoPendingOptionAction();
	}

	private class ProcessingContext {

		private @Nullable OptionCmdLineAction pendingOptionAction = null;
		private @Nullable String pendingArg = null;

		ProcessingContext() {
			// Nothing to do, just to make it accessible for out class
		}

		public boolean processPendingOptionAction(String option) throws CmdLineException {
			// Check whether there is a pending named option waiting for completion.
			// If this is the case, check and invoke the action.
			OptionCmdLineAction optionAction = this.pendingOptionAction;
			String arg = this.pendingArg;
			boolean processed = false;

			if (optionAction != null && arg != null) {
				if (isActionArg(option)) {
					throw new CmdLineException(CmdLineProcessor.this, arg);
				}

				try {
					optionAction.accept(arg, option);
				} catch (RuntimeException e) {
					throw new CmdLineException(CmdLineProcessor.this, arg, e);
				}
				this.pendingOptionAction = null;
				this.pendingArg = null;
				processed = true;
			}
			return processed;
		}

		public void verifyNoPendingOptionAction() throws CmdLineException {
			OptionCmdLineAction optionAction = this.pendingOptionAction;
			String arg = this.pendingArg;

			if (optionAction != null && arg != null) {
				throw new CmdLineException(CmdLineProcessor.this, arg);
			}
		}

		public boolean processOptionAction(String arg, List<OptionCmdLineAction> actions) {
			// Check whether the argument is a known option argument.
			// If this is the case, remember it as pending and continue (processing will be done above).
			Optional<OptionCmdLineAction> optOptionAction = actions.stream().filter(action -> action.contains(arg))
					.findFirst();
			boolean processed = false;

			if (optOptionAction.isPresent()) {
				this.pendingOptionAction = optOptionAction.get();
				this.pendingArg = arg;
				processed = true;
			}
			return processed;
		}

		public boolean processSwitchAction(String arg, List<SwitchCmdLineAction> actions) throws CmdLineException {
			// Check whether the argument is a known switch argument.
			// If this is the case, invoke it.
			Optional<SwitchCmdLineAction> optSwitchAction = actions.stream().filter(action -> action.contains(arg))
					.findFirst();
			boolean processed = false;

			if (optSwitchAction.isPresent()) {
				try {
					optSwitchAction.get().accept(arg);
				} catch (RuntimeException e) {
					throw new CmdLineException(CmdLineProcessor.this, arg, e);
				}
				processed = true;
			}
			return processed;
		}

	}

	/**
	 * Add a {@linkplain CmdLineAction} for switch argument ({@code e.g. --switch}) processing.
	 *
	 * @param action The {@linkplain Consumer} to invoke with the argument string.
	 * @return The created {@linkplain CmdLineAction}.
	 */
	public CmdLineAction onSwitch(Consumer<String> action) {
		SwitchCmdLineAction switchAction = new SwitchCmdLineAction(action);

		this.switchActions.add(switchAction);
		return switchAction;
	}

	/**
	 * Add a {@linkplain CmdLineAction} for option argument ({@code e.g. --option value}) processing.
	 *
	 * @param action The {@linkplain BiConsumer} to invoke with the argument and option string.
	 * @return The created {@linkplain CmdLineAction}.
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
	 * @param action The {@linkplain Consumer} to invoke with the option string.
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
	 * @param action The {@linkplain Consumer} to invoke with the argument string.
	 */
	public void onUnknownArg(Consumer<String> action) {
		this.unknownAction = action;
	}

	/**
	 * Pre-defined ignore action to ignore specific arguments.
	 *
	 * @param arg The ignored argument.
	 */
	@SuppressWarnings("squid:S1172")
	public static void ignore(String arg) {
		// Nothing to do
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
		public void accept(String t) {
			this.action.accept(t);
		}

	}

	private class OptionCmdLineAction extends CmdLineAction implements BiConsumer<String, String> {

		private final BiConsumer<String, String> action;

		OptionCmdLineAction(BiConsumer<String, String> action) {
			this.action = action;
		}

		@Override
		public void accept(String t, String u) {
			this.action.accept(t, u);
		}

	}

}
