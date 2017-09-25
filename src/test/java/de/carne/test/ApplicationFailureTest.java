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
package de.carne.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.Application;
import de.carne.ApplicationMain;

/**
 * Test {@link Application} class.
 */
public class ApplicationFailureTest implements ApplicationMain {

	/**
	 * Setup the necessary system properties.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		System.setProperty("de.carne.Application", "invalid");
		System.setProperty("de.carne.Application.DEBUG", "true");
	}

	/**
	 * Test application initialization and start.
	 */
	@Test(expected = ExceptionInInitializerError.class)
	public void testApplicationStart() {
		Application.main(new String[0]);
	}

	@Override
	public String name() {
		return ApplicationFailureTest.class.getSimpleName();
	}

	@Override
	public int run(String[] args) {
		Assert.fail();
		return 0;
	}

}
