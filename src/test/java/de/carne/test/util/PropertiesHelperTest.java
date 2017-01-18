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
package de.carne.test.util;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.check.NonNullByDefault;
import de.carne.util.PropertiesHelper;

/**
 * Test {@link PropertiesHelper} class.
 */
@NonNullByDefault
public class PropertiesHelperTest {

	/**
	 * Setup necessary System properties.
	 */
	@BeforeClass
	public static void setupSystemProperties() {
		System.setProperty(PropertiesHelperTest.class.getPackage().getName() + ".property2", "2");
		System.setProperty(PropertiesHelperTest.class.getPackage().getName() + ".propertyB", "B");
	}

	/**
	 * Test properties access in success scenario.
	 *
	 * @throws IOException if an error occurs.
	 */
	@Test
	public void testPropertiesAccess() throws IOException {
		Properties initProperties = PropertiesHelper.init(getClass());

		Assert.assertEquals(2, initProperties.size());

		Properties loadProperties = PropertiesHelper.load(getClass());

		Assert.assertEquals(2, loadProperties.size());

		Assert.assertEquals(1, PropertiesHelper.getInt(loadProperties, "property1", -1));
		Assert.assertEquals("A", PropertiesHelper.get(loadProperties, "propertyA", ""));

		Assert.assertEquals(-1, PropertiesHelper.getInt(loadProperties, "propertyA", -1));
		Assert.assertEquals(-1, PropertiesHelper.getInt(loadProperties, "property3", -1));
		Assert.assertEquals("", PropertiesHelper.get(loadProperties, "propertyC", ""));

		Assert.assertEquals(2, PropertiesHelper.getInt(getClass(), ".property2", -1));
		Assert.assertEquals("B", PropertiesHelper.get(getClass(), ".propertyB", ""));

		Assert.assertEquals(-1, PropertiesHelper.getInt(getClass(), ".propertyB", -1));
		Assert.assertEquals(-1, PropertiesHelper.getInt(getClass(), ".property3", -1));
		Assert.assertEquals("", PropertiesHelper.get(getClass(), ".propertyC", ""));
	}

}
