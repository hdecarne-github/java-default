/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Utility class used to access a module's manifest.
 */
public class ManifestInfos {

	/**
	 * Module id attribute.
	 */
	public static final String ATTRIBUTE_MODULE_ID = "X-Module-Id";

	/**
	 * Module name attribute.
	 */
	public static final String ATTRIBUTE_MODULE_NAME = "X-Module-Name";

	/**
	 * Module version attribute.
	 */
	public static final String ATTRIBUTE_MODULE_VERSION = "X-Module-Version";

	/**
	 * Build timestamp attribute.
	 */
	public static final String ATTRIBUTE_BUILD_TIMESTAMP = "Build-Timestamp";

	/**
	 * The default value used in case an attribute is not defined in the manifest.
	 */
	public static final String NA = "n/a";

	private final String moduleId;
	private final Manifest manifest;

	private ManifestInfos(String moduleId, Manifest manifest) {
		this.moduleId = moduleId;
		this.manifest = manifest;
	}

	/**
	 * Constructs a new {@linkplain ManifestInfos} instance.
	 *
	 * @param moduleId the id of the module to get the manifest for.
	 */
	public ManifestInfos(String moduleId) {
		this(moduleId, findManifest(moduleId));
	}

	/**
	 * Collects all {@linkplain ManifestInfos} available in the current runtime environment.
	 *
	 * @return all {@linkplain ManifestInfos} available in the current runtime environment.
	 */
	public static SortedMap<String, ManifestInfos> getRuntimeInfos() {
		SortedMap<String, ManifestInfos> runtimeInfos = new TreeMap<>();

		try {
			Enumeration<URL> manifestUrls = getManifestResources();

			while (manifestUrls.hasMoreElements()) {
				Manifest manifest = loadManifestResource(manifestUrls.nextElement());
				Attributes attributes = manifest.getMainAttributes();

				if (attributes != null) {
					String moduleId = attributes.getValue(ATTRIBUTE_MODULE_ID);

					if (moduleId != null) {
						runtimeInfos.put(moduleId, new ManifestInfos(moduleId, manifest));
					}
				}
			}
		} catch (IOException e) {
			Exceptions.ignore(e);
		}
		return runtimeInfos;
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
	 * Gets the module id stored in the manifest.
	 *
	 * @return the module id stored in the manifest.
	 */
	public String id() {
		return this.moduleId;
	}

	/**
	 * Gets the module name stored in the manifest.
	 * <p>
	 * {@linkplain #NA} is returned if no manifest was found or if the manifest contains no information.
	 * </p>
	 *
	 * @return the module name stored in the manifest.
	 */
	public String name() {
		return getMainAttribute(ATTRIBUTE_MODULE_NAME, NA);
	}

	/**
	 * Gets the module version stored in the manifest.
	 * <p>
	 * {@linkplain #NA} is returned if no manifest was found or if the manifest contains no information.
	 * </p>
	 *
	 * @return the module version stored in the manifest.
	 */
	public String version() {
		return getMainAttribute(ATTRIBUTE_MODULE_VERSION, NA);
	}

	/**
	 * Gets the module build identifier stored in the manifest.
	 * <p>
	 * {@linkplain #NA} is returned if no manifest was found or if the manifest contains no information.
	 * </p>
	 *
	 * @return the module build identifier stored in the manifest.
	 */
	public String build() {
		return getMainAttribute(ATTRIBUTE_BUILD_TIMESTAMP, NA);
	}

	private static Manifest findManifest(String moduleId) {
		Manifest found = null;

		try {
			Enumeration<URL> manifestUrls = getManifestResources();

			while (manifestUrls.hasMoreElements()) {
				Manifest manifest = loadManifestResource(manifestUrls.nextElement());
				Attributes attributes = manifest.getMainAttributes();

				if (attributes != null && moduleId.equals(attributes.getValue(ATTRIBUTE_MODULE_ID))) {
					found = manifest;
					break;
				}
			}
		} catch (IOException e) {
			Exceptions.ignore(e);
		}
		return (found != null ? found : new Manifest());
	}

	private static Enumeration<URL> getManifestResources() throws IOException {
		return ManifestInfos.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
	}

	private static Manifest loadManifestResource(URL manifestUrl) throws IOException {
		Manifest manifest;

		try (InputStream manifestStream = manifestUrl.openStream()) {
			manifest = new Manifest(manifestStream);
		}
		return manifest;
	}

	@Override
	public String toString() {
		return name() + " " + version() + " (build: " + build() + ")";
	}

}
