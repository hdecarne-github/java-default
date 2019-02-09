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
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import de.carne.boot.Exceptions;

/**
 * Utility class used to access a module's manifest.
 */
public final class ManifestInfos {

	/**
	 * Module name attribute.
	 */
	public static final String ATTRIBUTE_MODULE_NAME = "X-Module-Name";

	/**
	 * Module version attribute.
	 */
	public static final String ATTRIBUTE_MODULE_VERSION = "X-Module-Version";

	/**
	 * Module build identifier attribute.
	 */
	public static final String ATTRIBUTE_MODULE_BUILD = "X-Module-Build";

	private final String moduleName;
	private final Manifest manifest;

	/**
	 * Constructs a new {@linkplain ManifestInfos} instance.
	 *
	 * @param moduleName the name of the module to get the manifest for.
	 */
	public ManifestInfos(String moduleName) {
		this.moduleName = moduleName;
		this.manifest = findManifest(moduleName);
	}

	/**
	 * Gets a manifest's main attribute.
	 *
	 * @param attributeName the attribute name to get.
	 * @param defaultValue the default value to return in case the attribute undefined.
	 * @return the found attribute value or the submitted default value in case the attribute is undefined.
	 */
	public String getMainAttribute(String attributeName, String defaultValue) {
		Attributes attributes = this.manifest.getMainAttributes();
		String attributeValue = (attributes != null ? attributes.getValue(attributeName) : null);

		return (attributeValue != null ? attributeValue : defaultValue);
	}

	/**
	 * Gets the module name stored in the manifest.
	 *
	 * @return the module name stored in the manifest.
	 */
	public String name() {
		return this.moduleName;
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
		return getMainAttribute(ATTRIBUTE_MODULE_VERSION, "n/a");
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
		return getMainAttribute(ATTRIBUTE_MODULE_BUILD, "n/a");
	}

	private static Manifest findManifest(String moduleName) {
		Manifest found = null;

		try {
			Enumeration<URL> manifestUrls = ManifestInfos.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

			while (manifestUrls.hasMoreElements()) {
				URL manifestUrl = manifestUrls.nextElement();

				try (InputStream manifestStream = manifestUrl.openStream()) {
					Manifest manifest = new Manifest(manifestStream);
					Attributes attributes = manifest.getMainAttributes();

					if (attributes != null && moduleName.equals(attributes.getValue(ATTRIBUTE_MODULE_NAME))) {
						found = manifest;
						break;
					}
				}
			}
		} catch (IOException e) {
			Exceptions.ignore(e);
		}
		return (found != null ? found : new Manifest());
	}

	@Override
	public String toString() {
		return name() + " " + version() + "(build: " + build() + ")";
	}

}
