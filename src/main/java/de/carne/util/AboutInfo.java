/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import de.carne.ApplicationLoader;
import de.carne.check.Nullable;

/**
 * Utility class providing access to the applications version information.
 * <p>
 * Version informations are stored in the application's manifest file and therefore only available after the application
 * has been build/packaged.
 */
public final class AboutInfo {

	private AboutInfo() {
		// Make sure this class is not instantiated from outside
	}

	/**
	 * The project id.
	 */
	public static final String PROJECT_ID;

	/**
	 * The project name.
	 */
	public static final String PROJECT_NAME;

	/**
	 * The project version.
	 */
	public static final String PROJECT_VERSION;

	/**
	 * The project build.
	 */
	public static final String PROJECT_BUILD;

	static {
		Attributes attributes = null;

		try (JarFile codeJar = ApplicationLoader.getApplicationJarFile()) {
			if (codeJar != null) {
				Manifest manifest = codeJar.getManifest();

				if (manifest != null) {
					attributes = manifest.getMainAttributes();
				}
			}
		} catch (Exception e) {
			Exceptions.warn(e);
		}
		PROJECT_ID = getAttributeValue(attributes, "X-Version-ProjectId");
		PROJECT_NAME = getAttributeValue(attributes, "X-Version-ProjectName");
		PROJECT_VERSION = getAttributeValue(attributes, "X-Version-ProjectVersion");
		PROJECT_BUILD = getAttributeValue(attributes, "X-Version-ProjectBuild");
	}

	private static String getAttributeValue(@Nullable Attributes attributes, String name) {
		String value = (attributes != null ? attributes.getValue(name) : null);

		return (value != null ? value : "?");
	}

}
