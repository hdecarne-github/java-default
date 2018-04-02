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
package de.carne.util.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import de.carne.check.Nullable;
import de.carne.nio.file.attribute.FileAttributes;
import de.carne.util.Exceptions;
import de.carne.util.ShutdownHooks;
import de.carne.util.Strings;
import de.carne.util.logging.Log;

/**
 * This class provides the actual store access for all {@linkplain FilePreferences} objects.
 */
abstract class FilePreferencesStore {

	private static final Log LOG = new Log();

	private static final Set<FileStore> FILE_STORES = new HashSet<>();

	@Nullable
	private Properties cachedData = null;

	private List<Consumer<Properties>> changeLog = new LinkedList<>();

	public static FilePreferencesStore fromFile(Path file) {
		FileStore fileStore = new FileStore(file);

		synchronized (FILE_STORES) {
			if (FILE_STORES.isEmpty()) {
				ShutdownHooks.add(FilePreferencesStore::flushFileStores);
			}
			FILE_STORES.add(fileStore);
		}
		return fileStore;
	}

	public static void flushFileStores() {
		LOG.info("Flushing all opended file stores...");
		synchronized (FILE_STORES) {
			for (FileStore fileStore : FILE_STORES) {
				try {
					fileStore.flush();
				} catch (BackingStoreException e) {
					LOG.error(e, "Failed to store configuration file: ''{0}''", fileStore);
				}
			}
		}
	}

	public static FilePreferencesStore fromData(Properties data) {
		return new DataStore(data);
	}

	public FilePreferences root() {
		return new FilePreferences(this);
	}

	public synchronized void put(FilePreferences preferences, String key, String value) {
		String preferencesKey = getPreferencesKey(preferences, key);

		recordChange(data -> data.put(preferencesKey, value));
	}

	@Nullable
	public synchronized String get(FilePreferences preferences, String key) {
		String preferencesKey = getPreferencesKey(preferences, key);

		return getCachedData().getProperty(preferencesKey);
	}

	public synchronized void remove(FilePreferences preferences, String key) {
		String preferencesKey = getPreferencesKey(preferences, key);

		recordChange(data -> data.remove(preferencesKey));
	}

	public synchronized void removeNode(FilePreferences preferences) {
		String preferencesKeyPrefix = getPreferencesKey(preferences, "");

		recordChange(data -> {
			Iterator<Map.Entry<Object, Object>> entryIterator = data.entrySet().iterator();

			while (entryIterator.hasNext()) {
				if (entryIterator.next().getKey().toString().startsWith(preferencesKeyPrefix)) {
					entryIterator.remove();
				}
			}
		});
	}

	public synchronized String[] keys(FilePreferences preferences) {
		String preferencesKeyPrefix = getPreferencesKey(preferences, "");
		Set<String> keys = getCachedData().keySet().stream().map(Object::toString)
				.map(key -> extractKey(preferencesKeyPrefix, key)).filter(Strings::notEmpty)
				.collect(Collectors.toSet());

		return keys.toArray(new String[keys.size()]);
	}

	public synchronized String[] childrenNames(FilePreferences preferences) {
		String preferencesKeyPrefix = getPreferencesKey(preferences, "");
		Set<String> childrenNames = getCachedData().keySet().stream().map(Object::toString)
				.map(key -> extractChildrenName(preferencesKeyPrefix, key)).filter(Strings::notEmpty)
				.collect(Collectors.toSet());

		return childrenNames.toArray(new String[childrenNames.size()]);
	}

	public synchronized FilePreferences child(FilePreferences preferences, String name) {
		return new FilePreferences(preferences, name);
	}

	public synchronized void sync() throws BackingStoreException {
		LOG.info("Syncing preferences store ''{0}''...", this);
		try {
			this.cachedData = syncData(this.changeLog);
		} catch (IOException e) {
			throw new BackingStoreException(e);
		}
		this.changeLog.clear();
	}

	public synchronized void flush() throws BackingStoreException {
		if (!this.changeLog.isEmpty()) {
			LOG.info("FLushing preferences store ''{0}''...", this);
			try {
				syncData(this.changeLog);
			} catch (IOException e) {
				throw new BackingStoreException(e);
			}
			this.changeLog.clear();
		}
	}

	private void recordChange(Consumer<Properties> change) {
		Properties data = getCachedData();

		change.accept(data);
		this.changeLog.add(change);
	}

	private String getPreferencesKey(Preferences preferences, String key) {
		StringBuilder keyBuilder = new StringBuilder();

		keyBuilder.append(preferences.absolutePath());
		if (preferences.parent() != null) {
			keyBuilder.append('/');
		}
		keyBuilder.append(key);
		return keyBuilder.toString();
	}

	private String extractKey(String prefix, String preferencesKey) {
		String key = "";

		if (preferencesKey.startsWith(prefix)) {
			int keyIndex = prefix.length();

			if (preferencesKey.indexOf('/', keyIndex) < 0) {
				key = preferencesKey.substring(keyIndex);
			}
		}
		return key;
	}

	private String extractChildrenName(String prefix, String preferencesKey) {
		String childrenName = "";

		if (preferencesKey.startsWith(prefix)) {
			int childrenNameIndex = prefix.length();
			int nextIndex = preferencesKey.indexOf('/', childrenNameIndex);

			if (nextIndex > childrenNameIndex) {
				childrenName = preferencesKey.substring(childrenNameIndex, nextIndex);
			}
		}
		return childrenName;
	}

	private Properties getCachedData() {
		Properties data = this.cachedData;

		if (data == null) {
			try {
				data = loadData();
			} catch (IOException e) {
				LOG.error(e, "Failed to load configuration file: ''{0}''", this);
				data = new Properties();
			}
			this.cachedData = data;
		}
		return data;
	}

	protected abstract Properties loadData() throws IOException;

	protected abstract Properties syncData(List<Consumer<Properties>> changes) throws IOException;

	private static class FileStore extends FilePreferencesStore {

		private final Path file;

		FileStore(Path file) {
			this.file = file;
		}

		@Override
		protected Properties loadData() throws IOException {
			Properties data = new Properties();

			try (FileChannel dataChannel = FileChannel.open(this.file, StandardOpenOption.READ);
					InputStream dataInputStream = Channels.newInputStream(dataChannel);
					FileLock dataLock = dataChannel.tryLock(0, Long.MAX_VALUE, true)) {
				if (dataLock == null) {
					throw new IOException("Failed to obtain read lock for file: '" + this.file.toString() + "'");
				}
				data.load(dataInputStream);
			} catch (NoSuchFileException e) {
				Exceptions.ignore(e);
			}
			return data;
		}

		@Override
		protected Properties syncData(List<Consumer<Properties>> changes) throws IOException {
			Properties data = new Properties();

			Files.createDirectories(this.file.getParent(), FileAttributes.userDirectoryDefault(this.file));
			try (FileChannel dataChannel = FileChannel.open(this.file, StandardOpenOption.READ,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
					InputStream dataInputStream = Channels.newInputStream(dataChannel);
					OutputStream dataOutputStream = Channels.newOutputStream(dataChannel);
					FileLock dataLock = dataChannel.tryLock(0, Long.MAX_VALUE, false)) {
				if (dataLock == null) {
					throw new IOException("Failed to obtain write lock for file: '" + this.file.toString() + "'");
				}
				data.load(dataInputStream);
				for (Consumer<Properties> change : changes) {
					change.accept(data);
				}
				data.store(dataOutputStream, this.file.toString());
			}
			return data;
		}

		@Override
		public int hashCode() {
			return this.file.hashCode();
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			return obj instanceof FileStore && this.file.equals(((FileStore) obj).file);
		}

		@Override
		public String toString() {
			return this.file.toString();
		}

	}

	private static class DataStore extends FilePreferencesStore {

		private final Properties data;

		DataStore(Properties data) {
			this.data = data;
		}

		@Override
		protected Properties loadData() {
			return this.data;
		}

		@Override
		protected Properties syncData(List<Consumer<Properties>> changes) throws IOException {
			for (Consumer<Properties> change : changes) {
				change.accept(this.data);
			}
			return this.data;
		}

		@Override
		public String toString() {
			return "<transient>";
		}

	}

}
