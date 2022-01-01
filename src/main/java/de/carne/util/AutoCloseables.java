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
package de.carne.util;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class providing {@linkplain AutoCloseable} related functions.
 */
public final class AutoCloseables {

	private AutoCloseables() {
		// Prevent instantiation
	}

	/**
	 * Closes a potential {@linkplain AutoCloseable}.
	 * <p>
	 * An {@linkplain Exception} caused by {@linkplain AutoCloseable#close()} is forwarded to the caller.
	 *
	 * @param object the object to check and close.
	 * @throws Exception if {@link AutoCloseable#close()} fails.
	 */
	public static void close(@Nullable Object object) throws Exception {
		if (object instanceof AutoCloseable) {
			((AutoCloseable) object).close();
		}
	}

	/**
	 * Closes multiple {@linkplain AutoCloseable}s in a safe manner.
	 * <p>
	 * This function ensures that for every submitted non-null {@linkplain AutoCloseable} instance the corresponding
	 * {@linkplain AutoCloseable#close()} method is called. The first encountered {@linkplain Exception} will be
	 * forwarded to the caller. Any additional {@linkplain Exception} will be added to the initial one as a suppressed
	 * exception.
	 *
	 * @param closeables the {@linkplain AutoCloseable}s to close (may contain {@code null} values).
	 * @throws Exception if one or more {@link AutoCloseable#close()} calls fail.
	 */
	public static void closeAll(AutoCloseable... closeables) throws Exception {
		Exception exception = null;

		for (AutoCloseable closable : closeables) {
			if (closable != null) {
				try {
					closable.close();
				} catch (Exception e) {
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
	 * Closes multiple {@linkplain AutoCloseable}s in a safe manner.
	 * <p>
	 * This function ensures that for every submitted {@linkplain AutoCloseable} instance the corresponding
	 * {@linkplain AutoCloseable#close()} method is called. The first encountered {@linkplain Exception} will be
	 * forwarded to the caller. Any additional {@linkplain Exception} will be added to the initial one as a suppressed
	 * exception.
	 *
	 * @param closeables the {@linkplain AutoCloseable}s to close.
	 * @throws Exception if one or more {@link AutoCloseable#close()} calls fail.
	 */
	public static void closeAll(Iterable<AutoCloseable> closeables) throws Exception {
		Exception exception = null;

		for (AutoCloseable closable : closeables) {
			try {
				closable.close();
			} catch (Exception e) {
				if (exception == null) {
					exception = e;
				} else {
					exception.addSuppressed(e);
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * Close potential {@linkplain AutoCloseable}.
	 * <p>
	 * An {@linkplain Exception} caused by {@linkplain AutoCloseable#close()} is ignored.
	 *
	 * @param object the object to check and close.
	 */
	public static void safeClose(@Nullable Object object) {
		if (object instanceof AutoCloseable) {
			try {
				((AutoCloseable) object).close();
			} catch (Exception e) {
				Exceptions.ignore(e);
			}
		}
	}

	/**
	 * Close potential {@linkplain AutoCloseable}.
	 * <p>
	 * An {@linkplain Exception} caused by {@linkplain AutoCloseable#close()} is suppressed and added to the given outer
	 * exception.
	 *
	 * @param exception the currently handled outer exception.
	 * @param object the object to check and close.
	 */
	public static void safeClose(Throwable exception, @Nullable Object object) {
		if (object instanceof AutoCloseable) {
			try {
				((AutoCloseable) object).close();
			} catch (Exception e) {
				exception.addSuppressed(e);
			}
		}
	}

}
