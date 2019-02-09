/*
 * Copyright (c) 2016-2019 Holger de Carne and contributors, All Rights Reserved.
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
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.eclipse.jdt.annotation.NonNull;

import de.carne.boot.Exceptions;

/**
 * Utility class used to retrieve version info and the like from a module's manifest.
 */
public final class ManifestInfos {

	private static final String UNDEFINED_ATTRIBUTE = "<undefined>";

	private final String name;
	private final String version;
	private final String build;

	/**
	 * Constructs a new {@linkplain ManifestInfos} instance.
	 *
	 * @param module a {@linkplain Class} identifying the module to get the version infos for.
	 */
	public ManifestInfos(Class<?> module) {
		@NonNull String[] names = new @NonNull String[] { "X-Module-Name", "X-Module-Version", "X-Module-Build" };
		Map<String, String> values = findAttributeValues(module, names);

		this.name = Objects.toString(values.getOrDefault(names[0], UNDEFINED_ATTRIBUTE));
		this.version = Objects.toString(values.getOrDefault(names[1], UNDEFINED_ATTRIBUTE));
		this.build = Objects.toString(values.getOrDefault(names[2], UNDEFINED_ATTRIBUTE));
	}

	/**
	 * Gets the module name stored in the manifest.
	 * <p>
	 * A default value is returned if no manifest was found or if the manifest contained no information.
	 * </p>
	 *
	 * @return the module name stored in the manifest.
	 */
	public String name() {
		return this.name;
	}

	/**
	 * Gets the module version stored in the manifest.
	 * <p>
	 * A default value is returned if no manifest was found or if the manifest contained no information.
	 * </p>
	 *
	 * @return the module version stored in the manifest.
	 */
	public String version() {
		return this.version;
	}

	/**
	 * Gets the module build identifier stored in the manifest.
	 * <p>
	 * A default value is returned if no manifest was found or if the manifest contained no information.
	 * </p>
	 *
	 * @return the module build identifier stored in the manifest.
	 */
	public String build() {
		return this.build;
	}

	private static Map<String, String> findAttributeValues(Class<?> module, @NonNull String... names) {
		Map<String, String> values = new HashMap<>();
		URL moduleUrl = module.getProtectionDomain().getCodeSource().getLocation();

		try {
			Enumeration<URL> manifestUrls = module.getClassLoader().getResources("META-INF/MANIFEST.MF");

			while (manifestUrls.hasMoreElements()) {
				URL manifestUrl = manifestUrls.nextElement();

				if (manifestUrl.getProtocol().equals(moduleUrl.getProtocol())
						&& manifestUrl.getHost().equals(moduleUrl.getHost())
						&& manifestUrl.getPort() == moduleUrl.getPort()
						&& manifestUrl.getPath().startsWith(moduleUrl.getPath())) {
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
			}
		} catch (IOException e) {
			Exceptions.ignore(e);
		}
		return values;
	}

	@Override
	public String toString() {
		return this.name + " " + this.version + "(build: " + this.build + ")";
	}

}
