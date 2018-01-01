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
package de.carne.test.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.util.ApplicationManifestInfo;

/**
 * Test {@linkplain ApplicationManifestInfo} class.
 */
public class ApplicationManifestInfoTest {

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("de.carne.Application", "test");
		System.setProperty("de.carne.Application.DEBUG", "true");
	}

	/**
	 * Test {@linkplain ApplicationManifestInfo} attributes.
	 */
	@Test
	public void testInfos() {
		// As defined in our test manifest
		Assert.assertEquals("Test Application", getApplicationName());
		Assert.assertEquals("1.0-test", getApplicationVersion());
		// As undefined in our test manifest
		Assert.assertEquals("<undefined>", getApplicationBuild());
	}

	// wrap in function to keep Sonar happy
	private static String getApplicationName() {
		return ApplicationManifestInfo.APPLICATION_NAME;
	}

	// wrap in function to keep Sonar happy
	private static String getApplicationVersion() {
		return ApplicationManifestInfo.APPLICATION_VERSION;
	}

	// wrap in function to keep Sonar happy
	private static String getApplicationBuild() {
		return ApplicationManifestInfo.APPLICATION_BUILD;
	}

}
