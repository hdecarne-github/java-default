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
 * {@linkplain Long} preference access.
 */
public class LongPreference extends Preference<@NonNull Long> {

	/**
	 * Constructs a new {@linkplain LongPreference} instance.
	 *
	 * @param key the preference key to access.
	 * @param defaultValue the default preference value to use.
	 */
	public LongPreference(String key, long defaultValue) {
		super(key, Long.valueOf(defaultValue));
	}

	@Override
	public Long get(Preferences prefs, Long def) {
		return prefs.getLong(key(), def.longValue());
	}

	@Override
	public void put(Preferences prefs, Long value) {
		prefs.putLong(key(), value.longValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @return the retrieved preference value.
	 */
	public long getLong(Preferences prefs) {
		return prefs.getLong(key(), defaultValue().longValue());
	}

	/**
	 * Gets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to get the value from.
	 * @param def the default preference value to return in case the preference is not defined.
	 * @return the retrieved preference value.
	 */
	public long getLong(Preferences prefs, long def) {
		return prefs.getLong(key(), def);
	}

	/**
	 * Sets this instance's preference value.
	 *
	 * @param prefs the {@linkplain Preferences} object to set the value longo.
	 * @param value the preference value to set.
	 */
	public void putLong(Preferences prefs, long value) {
		prefs.putLong(key(), value);
	}

}
