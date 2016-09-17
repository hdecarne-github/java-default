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
package de.carne.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.util.prefs.PropertiesPreferencesFactory;

/**
 * Test {@link PropertiesPreferencesFactory} class functionality.
 */
public class PrefsTest {

	private static Path PROPERTIES_PATH = null;

	/**
	 * Create a temporary property file for the test run.
	 *
	 * @throws IOException if property file creation fails
	 */
	@BeforeClass
	public static void createPropertiesPath() throws IOException {
		PROPERTIES_PATH = Files.createTempFile(PrefsTest.class.getSimpleName(), ".properties");
	}

	/**
	 * Delete the created temporary property file.
	 *
	 * @throws IOException if property file deletion fails
	 */
	@AfterClass
	public static void deletePropertiesPath() throws IOException {
		if (PROPERTIES_PATH != null) {
			Files.delete(PROPERTIES_PATH);
		}
	}

	/**
	 * Test {@Preferences} functions
	 */
	@Test
	public void testPreferences() {
		try {
			populatePreferences();
			dumpPropertiesPath();
			readPreferences1();
			removePreferences();
			readPreferences2();
		} catch (BackingStoreException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	private void dumpPropertiesPath() throws IOException {
		if (PROPERTIES_PATH != null) {
			List<String> lines = Files.readAllLines(PROPERTIES_PATH);

			for (String line : lines) {
				System.out.println(line);
			}
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

}
