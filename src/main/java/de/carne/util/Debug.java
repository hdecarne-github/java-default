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

/**
 * Utility class providing debug related functions.
 */
public final class Debug {

	private Debug() {
		// Prevent instantiation
	}

	/**
	 * Gets the calling method's name.
	 *
	 * @return the calling method's name.
	 */
	public static String getCaller() {
		StackTraceElement[] stes = new Exception().getStackTrace();
		String caller;

		if (stes.length > 2) {
			StackTraceElement callerSte = stes[2];

			caller = callerSte.getClassName() + "." + callerSte.getMethodName();
		} else {
			caller = "<unknown>";
		}
		return caller;
	}

}
