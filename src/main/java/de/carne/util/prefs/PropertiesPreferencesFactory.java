/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.util.prefs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * {@link Properties} based implementation of the {@link PreferencesFactory}
 * interface.
 */
public class PropertiesPreferencesFactory implements PreferencesFactory {

	private static final String THIS_PACKAGE = Objects.requireNonNull(PropertiesPreferencesFactory.class.getPackage())
			.getName();

	private static final String PREFERENCES_DIR = System.getProperty(THIS_PACKAGE, "." + THIS_PACKAGE);

	@Override
	public Preferences systemRoot() {
		String userHome = getUserHome();
		String systemName = getSystemName();
		Path propertiesPath = Paths.get(userHome, PREFERENCES_DIR, "systemprefs-" + systemName + ".properties");

		return new PropertiesPreferences(propertiesPath);
	}

	@Override
	public Preferences userRoot() {
		String userHome = getUserHome();
		String userName = getUserName();
		Path propertiesPath = Paths.get(userHome, PREFERENCES_DIR, "userprefs-" + userName + ".properties");

		return new PropertiesPreferences(propertiesPath);
	}

	/**
	 * Create a {@link Preferences} object backed up by a custom properties
	 * file.
	 *
	 * @param propertiesPath The path to properties file to use as a preference
	 *        store.
	 * @return The created {@link Preferences} object.
	 */
	public static Preferences customRoot(Path propertiesPath) {
		assert propertiesPath != null;

		return new PropertiesPreferences(propertiesPath);
	}

	private static String getUserHome() {
		return System.getProperty("user.home", ".");
	}

	private static String getSystemName() {
		String systemName;

		try {
			systemName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			systemName = "localhost";
		}
		return systemName;
	}

	private static String getUserName() {
		return System.getProperty("user.name", "unknown");
	}

}
