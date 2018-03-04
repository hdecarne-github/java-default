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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.carne.check.Nullable;
import de.carne.util.Late;
import de.carne.util.prefs.FilePreferencesFactory;

/**
 * Test {@linkplain FilePreferencesFactory} class.
 */
class FilePreferencesTest {

	private static final Late<Path> TEMP_PATH = new Late<>();

	@BeforeAll
	static void setUpSystemPropertiesAndTempPath() throws IOException {
		TEMP_PATH.set(Files.createTempDirectory(FilePreferencesTest.class.getName()));
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty("de.carne.util.prefs.FilePreferences", TEMP_PATH.get().toString());
	}

	@AfterAll
	static void cleanUpTempPath() throws IOException {
		Files.walkFileTree(TEMP_PATH.get(), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(@Nullable Path file, @Nullable BasicFileAttributes attrs)
					throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(@Nullable Path dir, @Nullable IOException exc)
					throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});
	}

	@Test
	void testCustomPreferences() throws IOException, BackingStoreException {
		Preferences customPrefs1 = FilePreferencesFactory.customRoot(loadData());

		Assertions.assertEquals(1, customPrefs1.keys().length);
		Assertions.assertEquals(2, customPrefs1.childrenNames().length);

		for (String key : customPrefs1.keys()) {
			customPrefs1.remove(key);
		}
		for (String childrenName : customPrefs1.childrenNames()) {
			customPrefs1.node(childrenName).removeNode();
		}

		Assertions.assertEquals(0, customPrefs1.keys().length);
		Assertions.assertEquals(0, customPrefs1.childrenNames().length);

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

	@Test
	void testUserPreferences() throws IOException, BackingStoreException {
		Preferences referencePrefs = FilePreferencesFactory.customRoot(loadData());
		Preferences userPrefs1 = Preferences.userRoot();

		Assertions.assertEquals(0, userPrefs1.keys().length);
		Assertions.assertEquals(0, userPrefs1.childrenNames().length);

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

	@Test
	void testSystemPreferences() throws IOException, BackingStoreException {
		Preferences referencePrefs = FilePreferencesFactory.customRoot(loadData());
		Preferences systemPrefs1 = Preferences.systemRoot();

		Assertions.assertEquals(0, systemPrefs1.keys().length);
		Assertions.assertEquals(0, systemPrefs1.childrenNames().length);

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
		Assertions.assertEquals(expected.keys().length, actual.keys().length);
		for (String key : expected.keys()) {
			Assertions.assertEquals(expected.get(key, null), actual.get(key, key));
		}
		Assertions.assertEquals(expected.childrenNames().length, actual.childrenNames().length);
		for (String childrenName : expected.childrenNames()) {
			Preferences subExpected = expected.node(childrenName);
			Preferences subActual = actual.node(childrenName);

			verifyPrefs(subExpected, subActual);
		}
	}

}
