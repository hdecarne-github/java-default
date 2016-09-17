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
package de.carne.util.logging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * This class is used for logging initialization during startup and afterwards.
 * <p>
 * For proper initialization of the logging setup set the
 * "java.util.logging.config.class" property to this class' name.
 * </p>
 */
public final class LogConfig {

	private static final String THIS_PACKAGE = LogConfig.class.getPackage().getName();

	static {
		// Make sure our custom level class is loaded and the custom levels are
		// registered
		LogLevel.init();
	}

	/**
	 * Default logging configuration file name.
	 */
	public static final String CONFIG_DEFAULT = "logging-default.properties";

	/**
	 * Verbose logging configuration file name.
	 */
	public static final String CONFIG_VERBOSE = "logging-verbose.properties";

	/**
	 * Debug logging configuration file name.
	 */
	public static final String CONFIG_DEBUG = "logging-debug.properties";

	private static String currentConfig = null;

	/**
	 * Initialize the logging system using the default configuration.
	 * <p>
	 * The default configuration is determined by first checking the system
	 * property named after this package for a configuration file name. If the
	 * property is not set the default configuration file name
	 * ({@link #CONFIG_DEFAULT}) is used.
	 * </p>
	 */
	public LogConfig() {
		applyConfig(System.getProperty(THIS_PACKAGE, CONFIG_DEFAULT));
	}

	/**
	 * Apply a specific logging configuration.
	 * <p>
	 * The submitted configuration name has to denote an existing file or a
	 * resource. First the method tries to read the corresponding file. If this
	 * fails it tries to read a resource with the submitted name.
	 * </p>
	 *
	 * @param config The configuration file name or the resource name to read
	 *        the configuration from.
	 */
	public static synchronized void applyConfig(String config) {
		if (currentConfig == null || !currentConfig.equals(config)) {
			if (config != null) {
				if (!applyFileConfig(config)) {
					applyResourceConfig(config);
				}
			} else {
				LogManager.getLogManager().reset();
			}
		}
	}

	private static boolean applyFileConfig(String config) {
		boolean applied = false;

		try (InputStream configStream = new FileInputStream(config)) {
			LogManager.getLogManager().readConfiguration(configStream);
			applied = true;
		} catch (FileNotFoundException e) {
			// Ignore and report
		} catch (Exception e) {
			System.err.println("An error occurred while reading logging configuration file: " + config);
			e.printStackTrace();
		}
		return applied;
	}

	private static boolean applyResourceConfig(String config) {
		boolean applied = false;

		try (InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(config)) {
			if (configStream != null) {
				LogManager.getLogManager().readConfiguration(configStream);
				applied = true;
			} else {
				System.err.println("Unable to access logging configuration resource: " + config);
			}
		} catch (Exception e) {
			System.err.println("An error occurred while reading logging configuration resource: " + config);
			e.printStackTrace();
		}
		return applied;
	}

}
