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
package de.carne.util.logging;

import java.io.IOException;
import java.util.logging.ErrorManager;

/**
 * Helper class used to apply the default logging during {@linkplain java.util.logging.LogManager} initialization.
 * <p>
 * By setting the system property {@code java.util.logging.config.class} to this class' name the
 * {@linkplain java.util.logging.LogManager} will be initialized with the application's default logging configuration.
 */
public final class Config {

	/**
	 * Construct {@linkplain Config} and apply default logging configuration.
	 */
	public Config() {
		String config = System.getProperty(getClass().getName(), Logs.CONFIG_DEFAULT);

		try {
			Logs.readConfig(config);
		} catch (IOException e) {
			new ErrorManager().error("Failed to read logging config: " + config, e, ErrorManager.GENERIC_FAILURE);
		}
	}

}