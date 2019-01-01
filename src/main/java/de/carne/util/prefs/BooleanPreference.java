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

import java.util.prefs.Preferences;

/**
 * {@linkplain Boolean} preference access.
 */
public class BooleanPreference extends Preference<Boolean> {

	/**
	 * Constructs a new {@linkplain BooleanPreference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	public BooleanPreference(String key, boolean defaultValue) {
		super(key, Boolean.valueOf(defaultValue));
	}

	@Override
	public Boolean get(Preferences prefs, Boolean def) {
		return prefs.getBoolean(key(), defaultValue().booleanValue());
	}

	@Override
	public void put(Preferences prefs, Boolean value) {
		prefs.putBoolean(key(), value.booleanValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @return the retrieved preference value.
	 */
	public boolean getBoolean(Preferences prefs) {
		return prefs.getBoolean(key(), defaultValue().booleanValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @param def the default preference value to return in case the preference is not defined.
	 * @return the retrieved preference value.
	 */
	public boolean getBoolean(Preferences prefs, boolean def) {
		return prefs.getBoolean(key(), def);
	}

	/**
	 * Sets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to set the value into.
	 * @param value the preference value to set.
	 */
	public void putBoolean(Preferences prefs, boolean value) {
		prefs.putBoolean(key(), value);
	}

}
