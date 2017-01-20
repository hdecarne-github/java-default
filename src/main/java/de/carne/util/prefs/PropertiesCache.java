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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import de.carne.check.NonNullByDefault;
import de.carne.check.Nullable;
import de.carne.nio.FileAttributes;
import de.carne.util.logging.Log;

@NonNullByDefault
final class PropertiesCache {

	private final Log LOG = new Log();

	private final Path propertiesPath;

	private final Properties cache = new Properties();

	@Nullable
	private FileTime propertiesPathLastModifiedTime = null;

	PropertiesCache(Path propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public synchronized Map<String, String> getValues(PropertiesPreferences preferences) {
		initCache();

		Map<String, String> values = new HashMap<>();
		String absolutePreferencesPath = getAbsolutePreferencesPath(preferences);

		for (Map.Entry<Object, Object> cacheEntry : this.cache.entrySet()) {
			String cacheKey = cacheEntry.getKey().toString();
			String key = determinePreferencesKey(absolutePreferencesPath, cacheKey);

			if (key != null) {
				Object value = cacheEntry.getValue();

				if (value != null) {
					values.put(key, value.toString());
				}
			}
		}
		return values;
	}

	public synchronized Map<String, PropertiesPreferences> getChildren(PropertiesPreferences parent) {
		initCache();

		Map<String, PropertiesPreferences> children = new HashMap<>();
		String absolutePreferencesPath = getAbsolutePreferencesPath(parent);

		for (Map.Entry<Object, Object> cacheEntry : this.cache.entrySet()) {
			String cacheKey = cacheEntry.getKey().toString();
			String childName = determinePreferencesChildName(absolutePreferencesPath, cacheKey);

			if (childName != null && !children.containsKey(childName)) {
				children.put(childName, new PropertiesPreferences(parent, childName));
			}
		}
		return children;
	}

	public synchronized void sync(PropertiesPreferences preferences, @Nullable Map<String, String> modifiedValues,
			@Nullable Set<String> modifiedChildrenNames) {
		try {
			initCache();

			String absolutePreferencesPath = getAbsolutePreferencesPath(preferences);
			Iterator<Object> cacheKeys = this.cache.keySet().iterator();

			while (cacheKeys.hasNext()) {
				String cacheKey = cacheKeys.next().toString();

				// Remove all value keys if we have modified values (that modified values will added afterwards)
				if (modifiedValues != null) {
					String key = determinePreferencesKey(absolutePreferencesPath, cacheKey);

					if (key != null) {
						cacheKeys.remove();
					}
				}

				// Remove all outdated children if if have modified children names
				if (modifiedChildrenNames != null) {
					String childName = determinePreferencesChildName(absolutePreferencesPath, cacheKey);

					if (childName != null && !modifiedChildrenNames.contains(childName)) {
						cacheKeys.remove();
					}
				}
			}
			if (modifiedValues != null) {
				for (Map.Entry<String, String> valueEntry : modifiedValues.entrySet()) {
					String absoluteKey = absolutePreferencesPath + valueEntry.getKey();

					this.cache.put(absoluteKey, valueEntry.getValue());
				}
			}

			this.LOG.info("Writing preferences file ''{0}''...", this.propertiesPath);

			try (OutputStream propertiesStream = Files.newOutputStream(this.propertiesPath)) {
				this.cache.store(propertiesStream, null);
			}
			this.propertiesPathLastModifiedTime = Files.getLastModifiedTime(this.propertiesPath);
		} catch (IOException e) {
			this.LOG.warning(e, "An error occurred while syncing preferences file ''{0}''", this.propertiesPath);
		}
	}

	private void initCache() {
		try {
			if (!Files.exists(this.propertiesPath)) {
				this.LOG.info("Creating preferences file ''{0}''...", this.propertiesPath);
				Path propertiesDirectoryPath = this.propertiesPath.getParent();
				Files.createDirectories(propertiesDirectoryPath,
						FileAttributes.defaultUserDirectoryAttributes(propertiesDirectoryPath));
				Files.createFile(this.propertiesPath, FileAttributes.defaultUserFileAttributes(this.propertiesPath));
				this.propertiesPathLastModifiedTime = null;
			}

			FileTime lastModifiedTime = Files.getLastModifiedTime(this.propertiesPath);

			if (!Objects.equals(lastModifiedTime, this.propertiesPathLastModifiedTime)) {
				this.LOG.debug("Re-loading preferences from file ''{0}''...", this.propertiesPath);
				try (InputStream propertiesStream = Files.newInputStream(this.propertiesPath)) {
					this.cache.clear();
					this.cache.load(propertiesStream);
					this.propertiesPathLastModifiedTime = lastModifiedTime;
				}
			}
		} catch (IOException e) {
			this.LOG.warning(e, "An error occurred while initializing/reading preferences file ''{0}''",
					this.propertiesPath);
		}
	}

	private String getAbsolutePreferencesPath(PropertiesPreferences preferences) {
		String absolutePreferencesPath = preferences.absolutePath();

		if (preferences.parent() != null) {
			absolutePreferencesPath += "/";
		}
		return absolutePreferencesPath;
	}

	@Nullable
	private String determinePreferencesKey(String absolutePreferencesPath, String cacheKey) {
		String key = null;

		if (cacheKey.startsWith(absolutePreferencesPath)) {
			if (cacheKey.indexOf('/', absolutePreferencesPath.length()) < 0) {
				key = cacheKey.substring(absolutePreferencesPath.length());
			}
		}
		return key;
	}

	@Nullable
	private String determinePreferencesChildName(String absolutePreferencesPath, String cacheKey) {
		String key = null;

		if (cacheKey.startsWith(absolutePreferencesPath)) {
			int childKeyIndex = cacheKey.indexOf('/', absolutePreferencesPath.length());

			if (childKeyIndex > 0) {
				key = cacheKey.substring(absolutePreferencesPath.length(), childKeyIndex);
			}
		}
		return key;
	}

}
