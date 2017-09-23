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
package de.carne;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import de.carne.check.Nullable;

/**
 * {@link URLClassLoader} capable of loading class from nested jar files.
 */
final class ApplicationClassLoader extends URLClassLoader {

	ApplicationClassLoader(URL[] urls) {
		super(urls, null);
	}

	static {
		ClassLoader.registerAsParallelCapable();
	}

	private static final String APPLICATION_PROTOCOL = "app";

	static {
		ApplicationURLStreamHandlerFactory.SINGLETON.register(APPLICATION_PROTOCOL, protocol -> new URLStreamHandler() {

			@Override
			@Nullable
			protected URLConnection openConnection(@Nullable URL u) throws IOException {
				return (u != null ? ApplicationClassLoader.openConnection(u) : null);
			}

		});
	}

	static URLConnection openConnection(URL u) {
		return new URLConnection(u) {

			@Override
			public void connect() throws IOException {
				// Nothing to do here
			}

			@Override
			public InputStream getInputStream() throws IOException {
				String resource = getURL().getFile();
				InputStream resourceInputStream = getSystemResourceAsStream(resource);

				if (resourceInputStream == null) {
					throw new FileNotFoundException("Unknown resource: " + resource);
				}
				return resourceInputStream;
			}

		};
	}

	static URL[] assembleClasspath(JarURLConnection jarConnection) throws IOException {
		ArrayList<URL> jars = new ArrayList<>();

		jars.add(jarConnection.getJarFileURL());
		try (JarFile jarFile = jarConnection.getJarFile()) {
			jarFile.stream().filter(entry -> entry.getName().endsWith(".jar"))
					.map(ApplicationClassLoader::jarEntryToUrl).forEach(jars::add);
		}
		return jars.toArray(new URL[jars.size()]);
	}

	private static URL jarEntryToUrl(JarEntry entry) {
		URL url;

		try {
			url = new URL("jar:" + entry.getName());
		} catch (MalformedURLException e) {
			throw new ApplicationInitializationException(e);
		}
		return url;
	}

	static URL[] assembleClasspath(Path path) throws IOException {
		ArrayList<URL> jars = new ArrayList<>();

		jars.add(pathToUrl(path));
		try (Stream<Path> files = Files.find(path, 0, (file, attributes) -> file.endsWith(".jar"))) {
			files.map(ApplicationClassLoader::pathToUrl).forEach(jars::add);
		}
		return jars.toArray(new URL[jars.size()]);
	}

	private static URL pathToUrl(Path path) {
		URL url;

		try {
			url = path.toUri().toURL();
		} catch (MalformedURLException e) {
			throw new ApplicationInitializationException(e);
		}
		return url;
	}

	@Override
	public Class<?> loadClass(@Nullable String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.loadClass(name);
	}

}
