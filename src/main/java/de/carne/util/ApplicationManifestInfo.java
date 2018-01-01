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

import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import de.carne.Application;
import de.carne.check.Nullable;

/**
 * Utility class used to retrieve version info and the like from the application manifest.
 */
public final class ApplicationManifestInfo {

	private ApplicationManifestInfo() {
		// prevent instantiation
	}

	/**
	 * The application name (X-Application-Name).
	 */
	public static final String APPLICATION_NAME;

	/**
	 * The application version (X-Application-Version).
	 */
	public static final String APPLICATION_VERSION;

	/**
	 * The application build (X-Application-Build).
	 */
	public static final String APPLICATION_BUILD;

	private static final String UNDEFINED_ATTRIBUTE = "<undefined>";

	static {
		Manifest manifest = Application.manifest();
		Attributes mainAttributes = manifest.getMainAttributes();

		APPLICATION_NAME = getAttributeValue(mainAttributes, "X-Application-Name", () -> UNDEFINED_ATTRIBUTE);
		APPLICATION_VERSION = getAttributeValue(mainAttributes, "X-Application-Version", () -> UNDEFINED_ATTRIBUTE);
		APPLICATION_BUILD = getAttributeValue(mainAttributes, "X-Application-Build", () -> UNDEFINED_ATTRIBUTE);
	}

	private static String getAttributeValue(@Nullable Attributes attributes, String name,
			Supplier<String> defaultSupplier) {
		String value = (attributes != null ? attributes.getValue(name) : null);

		return (value != null ? value : defaultSupplier.get());
	}

}
