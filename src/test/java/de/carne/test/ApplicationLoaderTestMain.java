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

import de.carne.ApplicationLoader;
import de.carne.Main;
import de.carne.VM;
import de.carne.check.NonNullByDefault;

/**
 * Test main class for testing {@link ApplicationLoader}.
 */
@NonNullByDefault
public class ApplicationLoaderTestMain implements Main {

	/**
	 * Main entry point.
	 */
	@Override
	public int run(String[] args) {
		Assert.assertTrue(VM.TEST_MODE_ENABLED);
		return 0;
	}

}
