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
package de.carne.util.prefs;

import java.util.prefs.Preferences;

/**
 * Utility class for {@linkplain Preferences} value access.
 * 
 * @param <T> the actual preference type.
 */
public abstract class Preference<T> {

	private final String key;
	private final T defaultValue;

	/**
	 * Constructs a new {@linkplain Preference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	protected Preference(String key, T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets this instance's preference key.
	 *
	 * @return this instance's preference key.
	 */
	public final String key() {
		return this.key;
	}

	/**
	 * Gets this instance's default preference value.
	 *
	 * @return this instance's default preference value.
	 */
	public final T defaultValue() {
		return this.defaultValue;
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @return the retrieved preference value.
	 */
	public T get(Preferences prefs) {
		return get(prefs, this.defaultValue);
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @param def the default preference value to return in case the preference is not defined.
	 * @return the retrieved preference value.
	 */
	public abstract T get(Preferences prefs, T def);

	/**
	 * Sets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to set the value into.
	 * @param value the preference value to set.
	 */
	public abstract void put(Preferences prefs, T value);

	/**
	 * Removes this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to remove the value from.
	 */
	public void remove(Preferences prefs) {
		prefs.remove(this.key);
	}

}
