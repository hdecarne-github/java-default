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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Manifest;

import de.carne.check.Nullable;

/**
 * Generic main class responsible for bootstrapping of the actual application and taking care of proper class loader
 * setup depending on the execution context.
 */
public final class Application {

	private Application() {
		// prevent instantiation
	}

	// Early log support
	private static final boolean DEBUG = Boolean.getBoolean(Application.class.getName() + ".DEBUG");

	@SuppressWarnings("squid:S106")
	private static String debug(String format, Object... args) {
		String msg = String.format(format, args);

		System.out.println(msg);
		return msg;
	}

	@SuppressWarnings("squid:S106")
	private static String error(@Nullable Throwable thrown, String format, Object... args) {
		String msg = String.format(format, args);

		System.err.println(msg);
		if (thrown != null) {
			thrown.printStackTrace(System.err);
		}
		return msg;
	}

	// Application config resource setup
	private static final URL APPLICATION_CONFIG_URL;

	static {
		String configResourceSuffix = System.getProperty(Application.class.getName(), "").trim();
		String configResource = "/META-INF/" + Application.class.getName()
				+ (configResourceSuffix.length() > 0 ? "." + configResourceSuffix : configResourceSuffix);
		URL configUrl = Application.class.getResource(configResource);

		if (configUrl == null) {
			throw new ApplicationInitializationException(
					error(null, "Cannot find application config resource: %1$s", configResource));
		}

		if (DEBUG) {
			debug("Found application config: %1$s", configUrl.toExternalForm());
		}

		APPLICATION_CONFIG_URL = configUrl;
	}

	// Application class loader and manifest setup
	private static final boolean USE_LAUNCHER_CLASS_LOADER = Boolean
			.getBoolean(Application.class.getName() + ".USE_LAUNCHER_CLASS_LOADER");
	private static final ClassLoader APPLICATION_CLASS_LOADER;
	private static final Manifest APPLICATION_MANIFEST;

	static {
		// The following execution setups are supported
		// a) Fat jar: Application is started from jar containing all extra jars.
		// b) Exploded jar: Application is started from a single folder structure which also contains all extra jars.
		// c) Development: Application is started from one or more folders and extra jars are provided via current class
		// loader.
		// In setup a) and b) we need our own class loader. In setup c) we use the current one.
		// Which setup we are in is derived from the config url above.
		ClassLoader classLoader;
		Manifest manifest;

		try {
			if (DEBUG) {
				debug("Assembling classpath...");
			}

			String configUrlProtocol = APPLICATION_CONFIG_URL.getProtocol();
			URL[] applicationClasspath;
			Manifest applicationManifest;

			if ("jar".equals(configUrlProtocol)) {
				JarURLConnection jarConnection = (JarURLConnection) APPLICATION_CONFIG_URL.openConnection();

				applicationClasspath = ApplicationClassLoader.assembleClasspath(jarConnection);
				applicationManifest = jarConnection.getManifest();
			} else if ("file".equals(configUrlProtocol)) {
				Path applicationPath = Paths.get(APPLICATION_CONFIG_URL.toURI()).getParent().getParent();

				applicationClasspath = ApplicationClassLoader.assembleClasspath(applicationPath);

				Path manifestPath = applicationPath.resolve("META-INF/MANIFEST.MF");

				if (manifestPath.toFile().exists()) {
					URL manifestUrl = manifestPath.toUri().toURL();

					try (InputStream manifestInputStream = manifestUrl.openStream()) {
						applicationManifest = new Manifest(manifestInputStream);
					}
				} else {
					applicationManifest = null;
				}
			} else {
				throw new ApplicationInitializationException(error(null,
						"Unable to determine application classloader for protocol: %1$s", configUrlProtocol));
			}

			if (DEBUG) {
				int urlIndex = 0;

				for (URL url : applicationClasspath) {
					debug(" url[%1$d] = %2$s", urlIndex++, url.toExternalForm());
				}
			}

			classLoader = (!USE_LAUNCHER_CLASS_LOADER && applicationClasspath.length > 1
					? new ApplicationClassLoader(applicationClasspath)
					: Application.class.getClassLoader());
			manifest = (applicationManifest != null ? applicationManifest : new Manifest());
		} catch (URISyntaxException | IOException e) {
			throw new ApplicationInitializationException(e);
		}
		APPLICATION_CLASS_LOADER = classLoader;
		APPLICATION_MANIFEST = manifest;
	}

	/**
	 * Get application {@linkplain Manifest}.
	 *
	 * @return The application {@linkplain Manifest}.
	 */
	public static Manifest manifest() {
		return new Manifest(APPLICATION_MANIFEST);
	}

	// Support for multiplexed URLStreamHandlerFactory setups

	/**
	 * Register {@linkplain URLStreamHandlerFactory}
	 *
	 * @param protocol The {@linkplain URL} protocol handled by this {@linkplain URLStreamHandlerFactory}.
	 * @param factory The {@linkplain URLStreamHandlerFactory} to register.
	 * @return The previously registered {@linkplain URLStreamHandlerFactory} or {@code null} if none has been
	 *         registered so far.
	 */
	public static URLStreamHandlerFactory registerURLStreamHandlerFactory(String protocol,
			URLStreamHandlerFactory factory) {
		return ApplicationURLStreamHandlerFactory.SINGLETON.register(protocol, factory);
	}

	// Application startup
	private static final ApplicationMain APPLICATION_MAIN;

	static {
		if (DEBUG) {
			debug("Evaluating application config...");
		}

		ApplicationMain main;

		try (BufferedReader configReader = new BufferedReader(
				new InputStreamReader(APPLICATION_CONFIG_URL.openStream()))) {
			String mainClassName = configReader.readLine();

			if (mainClassName == null) {
				throw new EOFException(error(null, "Application config is empty"));
			}

			if (DEBUG) {
				debug("Main-Class = %1$s", mainClassName);
				debug("Applying system properties...");
			}

			String configLine;

			while ((configLine = configReader.readLine()) != null) {
				String trimmedConfigLine = configLine.trim();

				// Ignore empty lines as well as comments
				if (trimmedConfigLine.length() == 0 && trimmedConfigLine.startsWith("#")) {
					continue;
				}
				evalConfigPropertyLine(trimmedConfigLine);
			}

			Thread.currentThread().setContextClassLoader(APPLICATION_CLASS_LOADER);

			Class<? extends ApplicationMain> mainClass = Class.forName(mainClassName, true, APPLICATION_CLASS_LOADER)
					.asSubclass(ApplicationMain.class);

			main = mainClass.newInstance();
		} catch (IOException | ReflectiveOperationException e) {
			throw new ApplicationInitializationException(e);
		}
		APPLICATION_MAIN = main;
	}

	private static void evalConfigPropertyLine(String propertyLine) {
		int splitIndex = propertyLine.indexOf('=');
		String propertyKey;
		String propertyValue;

		if (splitIndex < 0) {
			propertyKey = propertyLine.trim();
			propertyValue = Boolean.TRUE.toString();
		} else if (splitIndex > 0) {
			propertyKey = propertyLine.substring(0, splitIndex).trim();
			propertyValue = propertyLine.substring(splitIndex + 1).trim();
		} else {
			propertyKey = "";
			propertyValue = "";
		}
		if (propertyKey.length() > 0 && propertyValue.length() > 0) {
			System.setProperty(propertyKey, propertyValue);

			if (DEBUG) {
				debug(" %1$s = %2$s", propertyKey, propertyValue);
			}
		} else {
			error(null, "Ignoring invalid system property line: %1$s", propertyLine);
		}
	}

	/**
	 * Main entry point.
	 *
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		if (DEBUG) {
			debug("Invoking application...");
		}

		int status;

		try {
			status = APPLICATION_MAIN.run(args);

			if (DEBUG) {
				debug("Application finished with status: %1$d", status);
			}
		} catch (Exception e) {
			error(e, "Application failed with exception: %1$s", e.getClass().getTypeName());
			status = -1;
		}
		if (status != 0) {
			System.exit(status);
		}
	}

	/**
	 * Get the currently executing {@linkplain ApplicationMain} class.
	 *
	 * @param <T> The actual main class to retrieve.
	 * @param clazz The actual type of the {@linkplain ApplicationMain} class.
	 * @return The currently executing {@linkplain ApplicationMain} class.
	 */
	public static <T extends ApplicationMain> T getMain(Class<T> clazz) {
		return clazz.cast(APPLICATION_MAIN);
	}

}
