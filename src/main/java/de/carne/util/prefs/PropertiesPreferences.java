/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

class PropertiesPreferences extends AbstractPreferences {

	private final PropertiesCache propertiesCache;

	private Map<String, String> values = null;

	private Map<String, PropertiesPreferences> children = null;

	PropertiesPreferences(Path propertiesPath) {
		super(null, "");
		this.propertiesCache = new PropertiesCache(propertiesPath);
	}

	PropertiesPreferences(PropertiesPreferences parent, String name) {
		super(parent, name);
		this.propertiesCache = parent.propertiesCache;
	}

	@Override
	protected void putSpi(String key, String value) {
		initValues();
		this.values.put(key, value);
	}

	@Override
	protected String getSpi(String key) {
		initValues();
		return this.values.get(key);
	}

	@Override
	protected void removeSpi(String key) {
		initValues();
		this.values.remove(key);
	}

	@Override
	protected void removeNodeSpi() throws BackingStoreException {
		initValues();
		this.values.clear();
	}

	@Override
	protected String[] keysSpi() throws BackingStoreException {
		initValues();

		Set<String> keys = getValueKeySet();

		return keys.toArray(new String[keys.size()]);
	}

	@Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		initChildren();

		Set<String> childrenNames = getChildrenNameSet();

		return childrenNames.toArray(new String[childrenNames.size()]);
	}

	@Override
	protected AbstractPreferences childSpi(String name) {
		initChildren();

		PropertiesPreferences child = this.children.get(name);

		if (child == null) {
			child = new PropertiesPreferences(this, name);
			this.children.put(name, child);
		}
		return child;
	}

	@Override
	protected void syncSpi() throws BackingStoreException {
		flushSpi();
		this.values = null;
		this.children = null;
	}

	@Override
	protected void flushSpi() throws BackingStoreException {
		Set<String> childrenNames = (this.children != null ? getChildrenNameSet() : null);

		if (this.values != null || childrenNames != null) {
			this.propertiesCache.sync(this, this.values, childrenNames);
		}
	}

	private void initValues() {
		if (this.values == null) {
			this.values = this.propertiesCache.getValues(this);
		}
	}

	private Set<String> getValueKeySet() {
		return this.values.keySet();
	}

	private void initChildren() {
		if (this.children == null) {
			this.children = this.propertiesCache.getChildren(this);
		}
	}

	private Set<String> getChildrenNameSet() {
		Set<String> childrenNameSet = new HashSet<>(this.children.size());

		for (PropertiesPreferences child : this.children.values()) {
			if (!child.isRemoved()) {
				childrenNameSet.add(child.name());
			}
		}
		return childrenNameSet;
	}

}
