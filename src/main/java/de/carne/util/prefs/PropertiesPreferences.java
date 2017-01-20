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
package de.carne.util.prefs;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import de.carne.check.Check;
import de.carne.check.NonNullByDefault;
import de.carne.check.Nullable;

@NonNullByDefault
class PropertiesPreferences extends AbstractPreferences {

	private final PropertiesCache propertiesCache;

	@Nullable
	private Map<String, String> cachedValues = null;

	@Nullable
	private Map<String, PropertiesPreferences> cachedChildren = null;

	PropertiesPreferences(Path propertiesPath) {
		super(null, "");
		this.propertiesCache = new PropertiesCache(propertiesPath);
	}

	PropertiesPreferences(PropertiesPreferences parent, String name) {
		super(parent, name);
		this.propertiesCache = parent.propertiesCache;
	}

	@Override
	protected void putSpi(@Nullable String key, @Nullable String value) {
		getCachedValues().put(key, value);
	}

	@Override
	protected String getSpi(@Nullable String key) {
		return getCachedValues().get(key);
	}

	@Override
	protected void removeSpi(@Nullable String key) {
		getCachedValues().remove(key);
	}

	@Override
	protected void removeNodeSpi() throws BackingStoreException {
		getCachedValues().clear();
	}

	@Override
	protected String[] keysSpi() throws BackingStoreException {
		Set<String> keys = getCachedValues().keySet();

		return keys.toArray(new String[keys.size()]);
	}

	@Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		Collection<PropertiesPreferences> childrenPreferences = getCachedChildren().values();
		Set<String> childrenNames = new HashSet<>(childrenPreferences.size());

		for (PropertiesPreferences childPreferences : childrenPreferences) {
			if (!childPreferences.isRemoved()) {
				childrenNames.add(childPreferences.name());
			}
		}
		return childrenNames.toArray(new String[childrenNames.size()]);
	}

	@Override
	protected AbstractPreferences childSpi(@Nullable String _name) {
		String name = Check.nonNull(_name);
		Map<String, PropertiesPreferences> children = getCachedChildren();
		PropertiesPreferences child = children.get(name);

		if (child == null) {
			child = new PropertiesPreferences(this, name);
			children.put(name, child);
		}
		return child;
	}

	@Override
	protected void syncSpi() throws BackingStoreException {
		flushSpi();
		this.cachedValues = null;
		this.cachedChildren = null;
	}

	@Override
	protected void flushSpi() throws BackingStoreException {
		if (this.cachedValues != null || this.cachedChildren != null) {
			Set<String> childrenNames = (this.cachedChildren != null ? getChildrenNames() : null);

			this.propertiesCache.sync(this, this.cachedValues, childrenNames);
		}
	}

	private Map<String, String> getCachedValues() {
		Map<String, String> values;

		if (this.cachedValues != null) {
			values = this.cachedValues;
		} else {
			values = this.cachedValues = this.propertiesCache.getValues(this);
		}
		return values;
	}

	private Map<String, PropertiesPreferences> getCachedChildren() {
		Map<String, PropertiesPreferences> children;

		if (this.cachedChildren != null) {
			children = this.cachedChildren;
		} else {
			children = this.cachedChildren = this.propertiesCache.getChildren(this);
		}
		return children;
	}

	private Set<String> getChildrenNames() {
		Collection<PropertiesPreferences> childrenPreferences = getCachedChildren().values();
		Set<String> childrenNames = new HashSet<>(childrenPreferences.size());

		for (PropertiesPreferences childPreferences : childrenPreferences) {
			if (!childPreferences.isRemoved()) {
				childrenNames.add(childPreferences.name());
			}
		}
		return childrenNames;
	}

}
