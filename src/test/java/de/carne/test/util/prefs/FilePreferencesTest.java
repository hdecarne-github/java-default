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
package de.carne.test.util.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.carne.util.prefs.FilePreferencesFactory;

/**
 * Test {@linkplain FilePreferencesFactory} class.
 */
public class FilePreferencesTest {

	/**
	 * Temporary HOME folder for config file creation.
	 */
	@ClassRule
	public static final TemporaryFolder HOME = new TemporaryFolder();

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty("de.carne.util.prefs.FilePreferences", HOME.getRoot().getAbsolutePath());
	}

	/**
	 * Test custom {@linkplain Preferences} access.
	 *
	 * @throws IOException if an I/ error occurs.
	 * @throws BackingStoreException if a backing store error occurs.
	 */
	@Test
	public void testCustomPreferences() throws IOException, BackingStoreException {
		Preferences customPrefs1 = FilePreferencesFactory.customRoot(loadData());

		Assert.assertEquals(1, customPrefs1.keys().length);
		Assert.assertEquals(2, customPrefs1.childrenNames().length);

		for (String key : customPrefs1.keys()) {
			customPrefs1.remove(key);
		}
		for (String childrenName : customPrefs1.childrenNames()) {
			customPrefs1.node(childrenName).removeNode();
		}

		Assert.assertEquals(0, customPrefs1.keys().length);
		Assert.assertEquals(0, customPrefs1.childrenNames().length);

		Preferences customPrefs2 = FilePreferencesFactory.customRoot(loadData());

		copyPrefs(customPrefs2, customPrefs1);
		verifyPrefs(customPrefs2, customPrefs1);
		customPrefs1.sync();
		verifyPrefs(customPrefs2, customPrefs1);
		copyPrefs(customPrefs2, customPrefs1);
		verifyPrefs(customPrefs2, customPrefs1);
		customPrefs1.flush();
		verifyPrefs(customPrefs2, customPrefs1);
		FilePreferencesFactory.flush();
	}

	/**
	 * Test user {@linkplain Preferences} access.
	 *
	 * @throws IOException if an I/ error occurs.
	 * @throws BackingStoreException if a backing store error occurs.
	 */
	@Test
	public void testUserPreferences() throws IOException, BackingStoreException {
		Preferences referencePrefs = FilePreferencesFactory.customRoot(loadData());
		Preferences userPrefs1 = Preferences.userRoot();

		Assert.assertEquals(0, userPrefs1.keys().length);
		Assert.assertEquals(0, userPrefs1.childrenNames().length);

		copyPrefs(referencePrefs, userPrefs1);
		verifyPrefs(referencePrefs, userPrefs1);
		userPrefs1.sync();
		copyPrefs(referencePrefs, userPrefs1);
		verifyPrefs(referencePrefs, userPrefs1);
		userPrefs1.flush();
		verifyPrefs(referencePrefs, userPrefs1);

		Preferences userPrefs2 = Preferences.userRoot();

		verifyPrefs(referencePrefs, userPrefs2);

		Preferences userPrefs3 = FilePreferencesFactory.customRoot(FilePreferencesFactory.userRootFile());

		verifyPrefs(referencePrefs, userPrefs3);
		FilePreferencesFactory.flush();
	}

	/**
	 * Test system {@linkplain Preferences} access.
	 *
	 * @throws IOException if an I/ error occurs.
	 * @throws BackingStoreException if a backing store error occurs.
	 */
	@Test
	public void testSystemPreferences() throws IOException, BackingStoreException {
		Preferences referencePrefs = FilePreferencesFactory.customRoot(loadData());
		Preferences systemPrefs1 = Preferences.systemRoot();

		Assert.assertEquals(0, systemPrefs1.keys().length);
		Assert.assertEquals(0, systemPrefs1.childrenNames().length);

		copyPrefs(referencePrefs, systemPrefs1);
		verifyPrefs(referencePrefs, systemPrefs1);
		systemPrefs1.sync();
		copyPrefs(referencePrefs, systemPrefs1);
		verifyPrefs(referencePrefs, systemPrefs1);
		systemPrefs1.flush();
		verifyPrefs(referencePrefs, systemPrefs1);

		Preferences systemPrefs2 = Preferences.userRoot();

		verifyPrefs(referencePrefs, systemPrefs2);

		Preferences userPrefs3 = FilePreferencesFactory.customRoot(FilePreferencesFactory.userRootFile().toFile());

		verifyPrefs(referencePrefs, userPrefs3);
		FilePreferencesFactory.flush();
	}

	private Properties loadData() throws IOException {
		Properties data = new Properties();

		try (InputStream dataStream = getClass().getResourceAsStream(getClass().getSimpleName() + ".properties")) {
			data.load(dataStream);
		}
		return data;
	}

	private void copyPrefs(Preferences from, Preferences to) throws BackingStoreException {
		for (String key : from.keys()) {
			to.put(key, from.get(key, null));
		}
		for (String childrenName : from.childrenNames()) {
			Preferences subFrom = from.node(childrenName);
			Preferences subTo = to.node(childrenName);

			copyPrefs(subFrom, subTo);
		}
	}

	private void verifyPrefs(Preferences expected, Preferences actual) throws BackingStoreException {
		Assert.assertEquals(expected.keys().length, actual.keys().length);
		for (String key : expected.keys()) {
			Assert.assertEquals(expected.get(key, null), actual.get(key, key));
		}
		Assert.assertEquals(expected.childrenNames().length, actual.childrenNames().length);
		for (String childrenName : expected.childrenNames()) {
			Preferences subExpected = expected.node(childrenName);
			Preferences subActual = actual.node(childrenName);

			verifyPrefs(subExpected, subActual);
		}
	}

}
