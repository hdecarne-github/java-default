/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.prefs.Preferences;

import org.eclipse.jdt.annotation.NonNull;

/**
 * {@linkplain String} preference access.
 */
public class StringPreference extends Preference<@NonNull String> {

	/**
	 * Constructs a new {@linkplain StringPreference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	public StringPreference(String key, String defaultValue) {
		super(key, defaultValue);
	}

	@Override
	@SuppressWarnings("null")
	public String get(Preferences prefs, String def) {
		return prefs.get(key(), def);
	}

	@Override
	public void put(Preferences prefs, String value) {
		prefs.put(key(), value);
	}

}
