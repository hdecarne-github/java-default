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
package de.carne.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.IntFunction;

import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain Writer} implementation used to remove ANSI escape codes from output (e.g. to write color enriched output
 * to a dumb terminal).
 */
public class AnsiFilter extends FilterWriter {

	private enum State {

		STANDARD(State::standardState),

		ESC(State::escState),

		UNKNOWN_ESC(State::finalState),

		CSI(State::csiOrParameterState),

		PARAMETER(State::csiOrParameterState),

		INTERMEDIATE(State::intermediateState),

		FINAL(State::finalState);

		private final IntFunction<State> transition;

		State(IntFunction<State> transition) {
			this.transition = transition;
		}

		private static State standardState(int c) {
			return (c == 0x1b ? ESC : STANDARD);
		}

		private static State escState(int c) {
			return (c == 0x9b ? CSI : UNKNOWN_ESC);
		}

		private static State csiOrParameterState(int c) {
			return (0x30 <= c && c <= 0x3f ? PARAMETER : intermediateState(c));
		}

		@SuppressWarnings("java:S3358")
		private static State intermediateState(int c) {
			return (0x20 <= c && c <= 0x2f ? INTERMEDIATE : (0x40 <= c && c <= 0x7f ? FINAL : STANDARD));
		}

		private static State finalState(@SuppressWarnings("unused") int c) {
			return STANDARD;
		}

		public State apply(int c) {
			return this.transition.apply(c);
		}

	}

	private State state = State.STANDARD;

	/**
	 * Constructs a new {@linkplain AnsiFilter} instance.
	 *
	 * @param out the {@linkplain Writer} instance to write to.
	 */
	public AnsiFilter(Writer out) {
		super(out);
	}

	@Override
	public void write(int c) throws IOException {
		this.state = this.state.apply(c);
		if (this.state == State.STANDARD) {
			super.write(c);
		} else if (this.state == State.UNKNOWN_ESC) {
			super.write(0x1b);
			super.write(c);
			this.state = State.STANDARD;
		}
	}

	@SuppressWarnings("java:S3776")
	@Override
	public void write(char @Nullable [] cbuf, int off, int len) throws IOException {
		if (cbuf != null) {
			boolean standardState = this.state == State.STANDARD;
			int writeOff1 = off;
			int writeOff2 = off;
			int offLimit = off + len;

			while (writeOff2 < offLimit) {
				char c = cbuf[writeOff2];

				this.state = this.state.apply(c);
				if (this.state == State.STANDARD) {
					if (!standardState) {
						writeOff1 = writeOff2;
						standardState = true;
					}
				} else if (this.state == State.UNKNOWN_ESC) {
					super.write(0x1b);
					this.state = State.STANDARD;
					writeOff1 = writeOff2;
					standardState = true;
				} else if (standardState) {
					if (writeOff1 < writeOff2) {
						super.write(cbuf, writeOff1, writeOff2 - writeOff1);
						writeOff1 = writeOff2;
					}
					writeOff1++;
					standardState = false;
				} else {
					writeOff1++;
				}
				writeOff2++;
			}
			if (standardState && writeOff1 < offLimit) {
				super.write(cbuf, writeOff1, offLimit - writeOff1);
			}
		} else {
			super.write(cbuf, off, len);
		}
	}

	@SuppressWarnings("java:S3776")
	@Override
	public void write(@Nullable String str, int off, int len) throws IOException {
		if (str != null) {
			boolean standardState = this.state == State.STANDARD;
			int writeOff1 = off;
			int writeOff2 = off;
			int offLimit = off + len;

			while (writeOff2 < offLimit) {
				char c = str.charAt(writeOff2);

				this.state = this.state.apply(c);
				if (this.state == State.STANDARD) {
					if (!standardState) {
						writeOff1 = writeOff2;
						standardState = true;
					}
				} else if (this.state == State.UNKNOWN_ESC) {
					super.write(0x1b);
					this.state = State.STANDARD;
					writeOff1 = writeOff2;
					standardState = true;
				} else if (standardState) {
					if (writeOff1 < writeOff2) {
						super.write(str, writeOff1, writeOff2 - writeOff1);
						writeOff1 = writeOff2;
					}
					writeOff1++;
					standardState = false;
				} else {
					writeOff1++;
				}
				writeOff2++;
			}
			if (standardState && writeOff1 < offLimit) {
				super.write(str, writeOff1, offLimit - writeOff1);
			}
		} else {
			super.write(str, off, len);
		}
	}

	@Override
	public void flush() throws IOException {
		if (this.state == State.ESC) {
			super.write(0x1b);
			this.state = State.STANDARD;
		}
		super.flush();
	}

}
