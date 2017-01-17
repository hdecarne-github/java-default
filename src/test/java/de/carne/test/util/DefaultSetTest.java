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

import org.junit.Assert;
import org.junit.Test;

import de.carne.check.NonNullByDefault;
import de.carne.util.DefaultSet;

/**
 * Test {@link DefaultSet} class.
 */
@NonNullByDefault
public class DefaultSetTest {

	/**
	 * Test {@link DefaultSet} class.
	 */
	@Test
	public void testDefaultSet() {
		DefaultSet<DefaultSetTest> defaultSet1 = new DefaultSet<>();

		Assert.assertNull(defaultSet1.getDefault());

		defaultSet1.add(this);

		Assert.assertEquals(this, defaultSet1.getDefault());

		DefaultSet<DefaultSetTest> defaultSet2 = new DefaultSet<>(defaultSet1);

		defaultSet1.clear();

		Assert.assertNull(defaultSet1.getDefault());
		Assert.assertEquals(this, defaultSet2.getDefault());
	}

}
