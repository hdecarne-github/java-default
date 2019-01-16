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
package de.carne.util.prefs;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import org.eclipse.jdt.annotation.NonNull;

import de.carne.boot.Exceptions;

/**
 * {@linkplain Path} preference access.
 */
public class PathPreference extends Preference<@NonNull Path> {

	/**
	 * Constructs a new {@linkplain PathPreference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	public PathPreference(String key, Path defaultValue) {
		super(key, defaultValue);
	}

	@Override
	public Path get(Preferences prefs, Path def) {
		String pathString = prefs.get(key(), null);
		Path path = def;

		if (pathString != null) {
			try {
				path = Paths.get(pathString);
			} catch (InvalidPathException e) {
				Exceptions.ignore(e);
			}
		}
		return path;
	}

	@Override
	public void put(Preferences prefs, Path value) {
		prefs.put(key(), value.toString());
	}

}
