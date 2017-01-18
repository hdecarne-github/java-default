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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.check.NonNullByDefault;
import de.carne.io.IOHelper;
import de.carne.util.Exceptions;
import de.carne.util.prefs.BooleanPreference;
import de.carne.util.prefs.IntPreference;
import de.carne.util.prefs.LongPreference;
import de.carne.util.prefs.PathPreference;
import de.carne.util.prefs.PropertiesPreferencesFactory;
import de.carne.util.prefs.StringPreference;

/**
 * Test {@link PropertiesPreferencesFactory} class.
 */
@NonNullByDefault
public class PrefsTest {

	private static final Path PROPERTIES_PATH;

	static {
		Path propertiesDirectory = Paths.get(".");

		try {
			propertiesDirectory = Files.createTempDirectory(PrefsTest.class.getSimpleName());
		} catch (IOException e) {
			Exceptions.toRuntime(e);
		}
		PROPERTIES_PATH = propertiesDirectory.resolve(PrefsTest.class.getSimpleName() + ".properties");
	}

	/**
	 * Setup necessary system properties.
	 */
	@BeforeClass
	public static void setupSystemProperties() {
		System.setProperty("java.util.prefs.PreferencesFactory", "de.carne.util.prefs.PropertiesPreferencesFactory");
	}

	/**
	 * Delete the created temporary property file.
	 *
	 * @throws IOException if property file deletion fails
	 */
	@AfterClass
	public static void deletePropertiesPath() throws IOException {
		IOHelper.deleteDirectoryTree(PROPERTIES_PATH);
	}

	/**
	 * Test integration in standard preferences API.
	 */
	@Test
	public void testPreferencesFactory() {
		Preferences.systemNodeForPackage(getClass());
		Preferences.userNodeForPackage(getClass());
	}

	/**
	 * Test {@Preferences} functions
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void testPreferencesAccess() throws Exception {
		populatePreferences();
		dumpPropertiesPath();
		readPreferences1();
		removePreferences();
		readPreferences2();
		clearPreferences();
	}

	private void dumpPropertiesPath() throws IOException {
		List<String> lines = Files.readAllLines(PROPERTIES_PATH);

		for (String line : lines) {
			System.out.println(line);
		}
	}

	private void populatePreferences() throws BackingStoreException {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);

		root.put("k1", "v1");
		root.put("k2", "v2");

		Preferences node1 = root.node("n1");

		node1.put("k1", "v1");
		node1.put("k2", "v2");

		Preferences node2 = root.node("n2");

		node2.put("k1", "v1");
		node2.put("k2", "v2");

		root.sync();
	}

	private void readPreferences1() throws BackingStoreException {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		String[] rootKeys = root.keys();
		String[] rootChildrenNames = root.childrenNames();

		Arrays.sort(rootKeys);
		Assert.assertArrayEquals(new String[] { "k1", "k2" }, rootKeys);
		Arrays.sort(rootChildrenNames);
		Assert.assertArrayEquals(new String[] { "n1", "n2" }, rootChildrenNames);
	}

	private void removePreferences() throws BackingStoreException {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);

		root.remove("k1");
		root.remove("k2");
		root.node("n1").removeNode();
		root.node("n2").removeNode();
		root.flush();
		root.sync();
	}

	private void readPreferences2() throws BackingStoreException {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		String[] rootKeys = root.keys();
		String[] rootChildrenNames = root.childrenNames();

		Arrays.sort(rootKeys);
		Assert.assertArrayEquals(new String[] {}, rootKeys);
		Arrays.sort(rootChildrenNames);
		Assert.assertArrayEquals(new String[] {}, rootChildrenNames);
	}

	private void clearPreferences() throws BackingStoreException {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);

		root.clear();
	}

	/**
	 * Test {@link StringPreference} class.
	 */
	@Test
	public void testStringPreference() {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		StringPreference preference = new StringPreference(root, "stringPreference");

		Assert.assertEquals(null, preference.get());

		preference.put(getClass().getSimpleName());

		Assert.assertEquals(getClass().getSimpleName(), preference.get());
	}

	/**
	 * Test {@link BooleanPreference} class.
	 */
	@Test
	public void testBooleanPreference() {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		BooleanPreference preference = new BooleanPreference(root, "booleanPreference");

		Assert.assertEquals(null, preference.get());
		Assert.assertEquals(true, preference.getBoolean(true));

		preference.putBoolean(true);

		Assert.assertEquals(Boolean.TRUE, preference.get());
		Assert.assertEquals(true, preference.getBoolean(false));

		preference.put(Boolean.FALSE);

		Assert.assertEquals(Boolean.FALSE, preference.get());
		Assert.assertEquals(false, preference.getBoolean(true));

		root.put(preference.key(), "xyz");

		Assert.assertEquals(Boolean.FALSE, preference.get());
		Assert.assertEquals(true, preference.getBoolean(true));
	}

	/**
	 * Test {@link IntPreference} class.
	 */
	@Test
	public void testIntPreference() {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		IntPreference preference = new IntPreference(root, "intPreference");

		Assert.assertEquals(null, preference.get());
		Assert.assertEquals(1, preference.getInt(1));

		preference.putInt(1);

		Assert.assertEquals(Integer.valueOf(1), preference.get());
		Assert.assertEquals(1, preference.getInt(0));

		preference.put(Integer.valueOf(-1));

		Assert.assertEquals(Integer.valueOf(-1), preference.get());
		Assert.assertEquals(-1, preference.getInt(0));

		root.put(preference.key(), "xyz");

		Assert.assertEquals(null, preference.get());
		Assert.assertEquals(1, preference.getInt(1));
	}

	/**
	 * Test {@link LongPreference} class.
	 */
	@Test
	public void testLongPreference() {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		LongPreference preference = new LongPreference(root, "longPreference");

		Assert.assertEquals(null, preference.get());
		Assert.assertEquals(1, preference.getLong(1));

		preference.putLong(1);

		Assert.assertEquals(Long.valueOf(1), preference.get());
		Assert.assertEquals(1, preference.getLong(0));

		preference.put(Long.valueOf(-1));

		Assert.assertEquals(Long.valueOf(-1), preference.get());
		Assert.assertEquals(-1, preference.getLong(0));

		root.put(preference.key(), "xyz");

		Assert.assertEquals(null, preference.get());
		Assert.assertEquals(1, preference.getLong(1));
	}

	/**
	 * Test {@link PathPreference} class.
	 */
	@Test
	public void testDirectoryPreference() {
		Preferences root = PropertiesPreferencesFactory.customRoot(PROPERTIES_PATH);
		PathPreference preference = new PathPreference(root, "directoryPreference", PathPreference.IS_REGULAR_FILE);

		Assert.assertEquals(null, preference.get());

		preference.put(PROPERTIES_PATH);

		Assert.assertEquals(PROPERTIES_PATH, preference.get());

		preference.put(PROPERTIES_PATH.getParent().resolve("unknown.dat"));

		Assert.assertEquals(null, preference.get());

		root.put(preference.key(), "xyz");

		Assert.assertEquals(null, preference.get());
	}

}
