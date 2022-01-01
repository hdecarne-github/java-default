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
 * {@linkplain Integer} preference access.
 */
public class IntPreference extends Preference<@NonNull Integer> {

	/**
	 * Constructs a new {@linkplain IntPreference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	public IntPreference(String key, int defaultValue) {
		super(key, Integer.valueOf(defaultValue));
	}

	@Override
	public Integer get(Preferences prefs, Integer def) {
		return prefs.getInt(key(), def.intValue());
	}

	@Override
	public void put(Preferences prefs, Integer value) {
		prefs.putInt(key(), value.intValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @return the retrieved preference value.
	 */
	public int getInt(Preferences prefs) {
		return prefs.getInt(key(), defaultValue().intValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @param def the default preference value to return in case the preference is not defined.
	 * @return the retrieved preference value.
	 */
	public int getInt(Preferences prefs, int def) {
		return prefs.getInt(key(), def);
	}

	/**
	 * Sets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to set the value into.
	 * @param value the preference value to set.
	 */
	public void putInt(Preferences prefs, int value) {
		prefs.putInt(key(), value);
	}

}
