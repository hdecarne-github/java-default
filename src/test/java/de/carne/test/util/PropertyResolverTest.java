/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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

import java.text.ParseException;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.util.PropertyResolver;

/**
 * Test {@linkplain PropertyResolver} class.
 */
class PropertyResolverTest {

	private static final String TEST_SYSTEM_PROPERTY_KEY = System.getProperties().keys().nextElement().toString();
	private static final String TEST_ENVIRONMENT_KEY = System.getenv().keySet().iterator().next().toString();

	private static final String UNDEFINED_KEY = "undefined.property.42";
	private static final String EXPECTED_UNDEFINED_VALUE = PropertyResolver.class.getSimpleName();

	private static final String EXPAND_STRING1 = "Nothing to expand";
	private static final String EXPAND_STRING2 = "A quoted $${key} property";
	private static final String EXPAND_STRING2_EXPANDED = "A quoted ${key} property";
	private static final String EXPAND_STRING3 = "$${" + UNDEFINED_KEY + "} has value ${" + UNDEFINED_KEY + "}";
	private static final String EXPAND_STRING3_EXPANDED = "${" + UNDEFINED_KEY + "} has value "
			+ EXPECTED_UNDEFINED_VALUE;

	private static final String INVALID_EXPAND_STRING1 = "$";
	private static final String INVALID_EXPAND_STRING2 = "${";
	private static final String INVALID_EXPAND_STRING3 = "${" + UNDEFINED_KEY + "}";

	@Test
	void testResolveSystemProperties() {
		PropertyResolver resolver = new PropertyResolver(true, false);
		String expected = String.valueOf(System.getProperty(TEST_SYSTEM_PROPERTY_KEY));

		Assertions.assertEquals(expected, resolver.resolve(TEST_SYSTEM_PROPERTY_KEY));
		Assertions.assertNull(resolver.resolve(UNDEFINED_KEY));
		Assertions.assertEquals(expected, resolver.resolve(UNDEFINED_KEY, expected));
	}

	@Test
	void testResolveEnvironmentProperties() {
		PropertyResolver resolver = new PropertyResolver(false, true);
		String expected = String.valueOf(System.getenv(TEST_ENVIRONMENT_KEY));

		Assertions.assertEquals(expected, resolver.resolve(TEST_ENVIRONMENT_KEY.replace('_', '.').toLowerCase()));
		Assertions.assertNull(resolver.resolve(UNDEFINED_KEY));
		Assertions.assertEquals(expected, resolver.resolve(UNDEFINED_KEY, expected));
	}

	@Test
	void testResolveCustomProperties() {
		PropertyResolver resolver = new PropertyResolver(customProperties());

		Assertions.assertEquals(EXPECTED_UNDEFINED_VALUE, resolver.resolve(UNDEFINED_KEY));
	}

	@Test
	void testExpandSuccess() throws ParseException {
		PropertyResolver resolver = new PropertyResolver(customProperties());

		Assertions.assertSame(EXPAND_STRING1, resolver.expand(EXPAND_STRING1));
		Assertions.assertEquals(EXPAND_STRING2_EXPANDED, resolver.expand(EXPAND_STRING2));
		Assertions.assertEquals(EXPAND_STRING3_EXPANDED, resolver.expand(EXPAND_STRING3));
	}

	@Test
	void testExpandFailure() {
		PropertyResolver resolver = new PropertyResolver();

		Assertions.assertThrows(ParseException.class, () -> resolver.expand(INVALID_EXPAND_STRING1));
		Assertions.assertThrows(ParseException.class, () -> resolver.expand(INVALID_EXPAND_STRING2));
		Assertions.assertThrows(ParseException.class, () -> resolver.expand(INVALID_EXPAND_STRING3));
	}

	private Properties customProperties() {
		Properties custom = new Properties();

		custom.put(UNDEFINED_KEY, EXPECTED_UNDEFINED_VALUE);
		return custom;
	}

}
