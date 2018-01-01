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
package de.carne.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.Application;
import de.carne.ApplicationMain;
import de.carne.check.Nullable;

/**
 * Test {@linkplain Application} class.
 */
public class ApplicationTest implements ApplicationMain {

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("de.carne.Application", "test");
		System.setProperty("de.carne.Application.DEBUG", "true");
	}

	private static final String[] TEST_ARGS = new String[] { "arg1", "arg2" };

	/**
	 * Test application initialization and start.
	 */
	@Test
	public void testApplicationStart() {
		Application.main(TEST_ARGS);
	}

	@Override
	public String name() {
		return ApplicationTest.class.getSimpleName();
	}

	@Override
	public int run(String[] args) {
		// Application access
		Assert.assertEquals(name(), Application.getMain(getClass()).name());
		Assert.assertEquals(this, Application.getMain(getClass()));
		// Command line handling
		Assert.assertArrayEquals(TEST_ARGS, args);
		// Property handling
		Assert.assertTrue(Boolean.getBoolean(ApplicationTest.class.getName() + ".Property1"));
		Assert.assertFalse(Boolean.getBoolean(ApplicationTest.class.getName() + ".Property2"));
		Assert.assertEquals("Any text...", System.getProperty(ApplicationTest.class.getName() + ".Property3"));

		// Class loading
		try {
			String externalSupplierClassName = getClass().getPackage().getName() + ".ExternalStringSupplier";
			Supplier<?> externalSupplier = Class.forName(externalSupplierClassName).asSubclass(Supplier.class)
					.newInstance();

			Assert.assertEquals(externalSupplierClassName, externalSupplier.get().toString());
		} catch (ReflectiveOperationException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		String resourceName = "/" + (getClass().getPackage().getName().replace('.', '/')) + "/ExternalResource.txt";

		try (InputStream resourceStream = getClass().getResourceAsStream(resourceName)) {
			Assert.assertNotNull(resourceStream);
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		// URL Factory support
		Application.registerURLStreamHandlerFactory("test", (@Nullable String protocol) -> new URLStreamHandler() {

			@Override
			protected URLConnection openConnection(@Nullable URL u) throws IOException {
				return ApplicationTest.class.getResource(ApplicationTest.class.getSimpleName() + ".class")
						.openConnection();
			}
		});

		try {
			URL testUrl = new URL("test", "", "any");

			Assert.assertNotNull(testUrl.openConnection());
		} catch (IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}
		return 0;
	}

}
