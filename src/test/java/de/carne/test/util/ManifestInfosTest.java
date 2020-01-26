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
package de.carne.test.util;

import java.util.SortedMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.ManifestInfos;

/**
 * Test {@linkplain ManifestInfos} class.
 */
class ManifestInfosTest {

	@Test
	void testKnownManifestInfos() {
		ManifestInfos manifestInfos = new ManifestInfos("test");

		// As defined in our test manifest
		Assertions.assertEquals("test", manifestInfos.id());
		Assertions.assertEquals("Test Application", manifestInfos.name());
		Assertions.assertEquals("1.0-test", manifestInfos.version());
		// As undefined in our test manifest
		Assertions.assertEquals(ManifestInfos.NA, manifestInfos.build());
	}

	@Test
	void testUnknownManifestInfos() {
		ManifestInfos manifestInfos = new ManifestInfos("unknown");

		Assertions.assertEquals("unknown", manifestInfos.id());
		Assertions.assertEquals(ManifestInfos.NA, manifestInfos.name());
		Assertions.assertEquals(ManifestInfos.NA, manifestInfos.version());
		Assertions.assertEquals(ManifestInfos.NA, manifestInfos.build());
	}

	@Test
	void testRuntimeInfos() {
		SortedMap<String, ManifestInfos> runtimeInfos = ManifestInfos.getRuntimeInfos();

		Assertions.assertTrue(runtimeInfos.containsKey("test"));
		Assertions.assertTrue(runtimeInfos.containsKey("java-boot"));
		Assertions.assertTrue(runtimeInfos.containsKey("java-default"));
		Assertions.assertEquals(3, runtimeInfos.size());
	}

}
