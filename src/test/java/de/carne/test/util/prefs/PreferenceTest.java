/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.test.util.prefs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.prefs.BooleanPreference;
import de.carne.util.prefs.FilePreferencesFactory;
import de.carne.util.prefs.IntPreference;
import de.carne.util.prefs.LongPreference;
import de.carne.util.prefs.PathPreference;
import de.carne.util.prefs.Preference;
import de.carne.util.prefs.StringPreference;

/**
 * Test {@linkplain Preference} based classes.
 */
class PreferenceTest {

	@Test
	void testStringPreference() {
		Preferences prefs = FilePreferencesFactory.customRoot(new Properties());
		StringPreference preference = new StringPreference(String.class.getName(), "");

		testPutGetRemove(preference, prefs, "string-value");
	}

	@Test
	void testBooleanPreference() {
		Preferences prefs = FilePreferencesFactory.customRoot(new Properties());
		BooleanPreference preference = new BooleanPreference(Boolean.class.getName(), false);

		testPutGetRemove(preference, prefs, Boolean.TRUE);

		preference.putBoolean(prefs, true);

		Assertions.assertTrue(preference.getBoolean(prefs));
	}

	@Test
	void testIntPreference() {
		Preferences prefs = FilePreferencesFactory.customRoot(new Properties());
		IntPreference preference = new IntPreference(Integer.class.getName(), -1);

		testPutGetRemove(preference, prefs, Integer.valueOf(42));

		preference.putInt(prefs, -42);

		Assertions.assertEquals(-42, preference.getInt(prefs));
	}

	@Test
	void testLongPreference() {
		Preferences prefs = FilePreferencesFactory.customRoot(new Properties());
		LongPreference preference = new LongPreference(Long.class.getName(), -1);

		testPutGetRemove(preference, prefs, Long.valueOf(42));

		preference.putLong(prefs, -42);

		Assertions.assertEquals(-42, preference.getLong(prefs));
	}

	@Test
	void testPathPreference() {
		Preferences prefs = FilePreferencesFactory.customRoot(new Properties());
		PathPreference preference = new PathPreference(Path.class.getName(), Paths.get("."));

		testPutGetRemove(preference, prefs, Paths.get("./aPath"));
	}

	private <T> void testPutGetRemove(Preference<T> preference, Preferences prefs, T value) {
		preference.put(prefs, value);

		Assertions.assertEquals(value, preference.get(prefs));

		preference.remove(prefs);

		Assertions.assertEquals(preference.defaultValue(), preference.get(prefs));
	}

}
