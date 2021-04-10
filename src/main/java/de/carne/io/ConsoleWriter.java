/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.io;

import java.io.Console;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.util.Platform;

/**
 * {@linkplain PrintWriter} implementation which supports ANSI sequences and uses the {@linkplain System#console()} for
 * emitting it's output.
 * <p>
 * If {@linkplain System#console()} yields {@code null}, {@linkplain System#out} is used instead. In the latter case any
 * ANSI sequence is filtered out prior to emitting the output.
 * <p>
 */
public final class ConsoleWriter extends PrintWriter {

	private static final boolean FORCE_ANSI_OUPUT = Boolean
			.parseBoolean(System.getProperty(ConsoleWriter.class.getName() + ".forceAnsiOutput"));

	/**
	 * Constructs a new {@linkplain ConsoleWriter} instance.
	 */
	public ConsoleWriter() {
		super(out(System.console()), true);
	}

	@SuppressWarnings("java:S106")
	private static Writer out(@Nullable Console console) {
		Writer out;

		if (console != null) {
			if (FORCE_ANSI_OUPUT || Platform.IS_LINUX || Platform.IS_MACOS) {
				out = console.writer();
			} else {
				out = new AnsiFilter(console.writer());
			}
		} else if (FORCE_ANSI_OUPUT) {
			out = new OutputStreamWriter(System.out);
		} else {
			out = new AnsiFilter(new OutputStreamWriter(System.out));
		}
		return out;
	}

	/**
	 * Checks whether this instance is capable of emitting ANSI sequences.
	 *
	 * @return {@code true} if ANSI sequences are emitted by this instance.
	 */
	public boolean isAnsiEnabled() {
		return !(this.out instanceof AnsiFilter);
	}

}
