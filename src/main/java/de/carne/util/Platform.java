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
package de.carne.util;

import de.carne.check.Check;

/**
 * Utility class providing functions to determine Java platform type and capabilities.
 */
public final class Platform {

	private Platform() {
		// Prevent instantiation
	}

	/**
	 * System property: {@code "os.arch"}
	 */
	public static final String SYSTEM_OS_ARCH = Check.notNull(System.getProperty("os.arch"));

	/**
	 * System property: {@code "os.name"}
	 */
	public static final String SYSTEM_OS_NAME = Check.notNull(System.getProperty("os.name"));

	/**
	 * System property: {@code "os.version"}
	 */
	public static final String SYSTEM_OS_VERSION = Check.notNull(System.getProperty("os.version"));

	/**
	 * Operating System: Linux
	 */
	public static final boolean IS_LINUX = SYSTEM_OS_NAME.toUpperCase().startsWith("LINUX");

	/**
	 * Operating System: macOS
	 */
	public static final boolean IS_MAC_OS = SYSTEM_OS_NAME.startsWith("Mac OS X");

	/**
	 * Operating System: WINDOWS
	 */
	public static final boolean IS_WINDOWS = SYSTEM_OS_NAME.startsWith("Windows");

	/**
	 * Name of the SWT toolkit to use (as guessed from the OS information).
	 */
	public static final String SWT_TOOLKIT = determineSwtToolkit();

	private static String determineSwtToolkit() {
		StringBuilder toolkit = new StringBuilder();

		if (IS_LINUX) {
			toolkit.append("gtk-linux");
		} else if (IS_MAC_OS) {
			toolkit.append("cocoa-macosx");
		} else if (IS_WINDOWS) {
			toolkit.append("win32-win32");
		} else {
			// Do not fail low-level in case of unknown toolkit
			toolkit.append("unknown-unknown");
		}
		if ("x86".equals(SYSTEM_OS_ARCH) || "x86_32".equals(SYSTEM_OS_ARCH)) {
			toolkit.append("-x86");
		} else if ("x86_64".equals(SYSTEM_OS_ARCH)) {
			toolkit.append("-x86_64");
		} else {
			// Do not fail low-level in case of unknown toolkit
			toolkit.append("uknown");
		}
		return toolkit.toString();
	}

}
