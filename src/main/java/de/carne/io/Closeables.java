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
package de.carne.io;

import java.io.Closeable;
import java.io.IOException;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.boot.Exceptions;

/**
 * Utility class providing {@linkplain Closeable} related functions.
 */
public final class Closeables {

	private Closeables() {
		// prevent instantiation
	}

	/**
	 * Closes a potential {@linkplain Closeable}.
	 * <p>
	 * An {@linkplain IOException} caused by {@linkplain Closeable#close()} is forwarded to the caller.
	 *
	 * @param object the object to check and close.
	 * @throws IOException if {@link Closeable#close()} fails.
	 */
	public static void close(@Nullable Object object) throws IOException {
		if (object instanceof Closeable) {
			((Closeable) object).close();
		}
	}

	/**
	 * Closes multiple {@linkplain Closeable}s in a safe manner.
	 * <p>
	 * This function ensures that for every submitted non-null {@linkplain Closeable} instance the corresponding
	 * {@linkplain Closeable#close()} method is called. The first encountered {@linkplain IOException} will be forwarded
	 * to the caller. Any additional {@linkplain IOException} will be added to the initial one as a suppressed
	 * exception.
	 *
	 * @param closeables the {@linkplain Closeable}s to close (may contain {@code null} values).
	 * @throws IOException if one or more {@link Closeable#close()} calls fail.
	 */
	public static void closeAll(Closeable... closeables) throws IOException {
		IOException exception = null;

		for (Closeable closable : closeables) {
			if (closable != null) {
				try {
					closable.close();
				} catch (IOException e) {
					if (exception == null) {
						exception = e;
					} else {
						exception.addSuppressed(e);
					}
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * Close potential {@linkplain Closeable}.
	 * <p>
	 * An {@linkplain IOException} caused by {@linkplain Closeable#close()} is ignored.
	 *
	 * @param object the object to check and close.
	 */
	public static void safeClose(@Nullable Object object) {
		if (object instanceof Closeable) {
			try {
				((Closeable) object).close();
			} catch (IOException e) {
				Exceptions.ignore(e);
			}
		}
	}

	/**
	 * Close potential {@linkplain Closeable}.
	 * <p>
	 * An {@linkplain IOException} caused by {@linkplain Closeable#close()} is suppressed and added to the given outer
	 * exception.
	 *
	 * @param exception the currently handled outer exception.
	 * @param object the object to check and close.
	 */
	public static void safeClose(Throwable exception, @Nullable Object object) {
		if (object instanceof Closeable) {
			try {
				((Closeable) object).close();
			} catch (IOException e) {
				exception.addSuppressed(e);
			}
		}
	}

}
