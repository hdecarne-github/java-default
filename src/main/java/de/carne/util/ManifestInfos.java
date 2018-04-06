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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import de.carne.boot.Exceptions;

/**
 * Utility class used to retrieve version info and the like from the application manifest.
 */
public final class ManifestInfos {

	private ManifestInfos() {
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
		String[] names = new String[] { "X-Application-Name", "X-Application-Version", "X-Application-Build" };
		Map<String, String> values = findAttributeValues(names);

		APPLICATION_NAME = values.getOrDefault(names[0], UNDEFINED_ATTRIBUTE);
		APPLICATION_VERSION = values.getOrDefault(names[1], UNDEFINED_ATTRIBUTE);
		APPLICATION_BUILD = values.getOrDefault(names[2], UNDEFINED_ATTRIBUTE);
	}

	private static Map<String, String> findAttributeValues(String... names) {
		Map<String, String> values = new HashMap<>();

		try {
			Enumeration<URL> manifestUrls = ManifestInfos.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

			while (manifestUrls.hasMoreElements()) {
				URL manifestUrl = manifestUrls.nextElement();

				try (InputStream manifestStream = manifestUrl.openStream()) {
					Manifest manifest = new Manifest(manifestStream);
					Attributes attributes = manifest.getMainAttributes();

					if (attributes != null) {
						for (String name : names) {
							String value = attributes.getValue(name);

							if (value != null) {
								values.put(name, value);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			Exceptions.ignore(e);
		}
		return values;
	}

}
