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

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

import de.carne.check.Nullable;

/**
 * This class is used to circumvent the limitation of the {@code URL} class that only one custom
 * {@code URLStreamHandlerFactory} can be set per VM.
 * <p>
 * This {@code URLStreamHandlerFactory} acts as a multiplexer for an arbitrary number of custom
 * {@code URLStreamHandlerFactory} instances registered via the {@link #createURLStreamHandler(String)} function.
 */
public final class ApplicationURLStreamHandlerFactory implements URLStreamHandlerFactory {

	private ApplicationURLStreamHandlerFactory() {
		// Prevent this class from being instantiated from outside
	}

	private static final Map<String, URLStreamHandlerFactory> URL_STREAM_HANDLER_FACTORY_MAP = new HashMap<>();

	static {
		URL.setURLStreamHandlerFactory(new ApplicationURLStreamHandlerFactory());
	}

	/**
	 * Register an additional {@link URLStreamHandlerFactory}.
	 *
	 * @param protocol The protocol to register the factory for.
	 * @param factory The {@link URLStreamHandlerFactory} to register.
	 */
	public static void registerURLStreamHandlerFactory(String protocol, URLStreamHandlerFactory factory) {
		synchronized (URL_STREAM_HANDLER_FACTORY_MAP) {
			URL_STREAM_HANDLER_FACTORY_MAP.put(protocol, factory);
		}
	}

	@Override
	@Nullable
	public URLStreamHandler createURLStreamHandler(@Nullable String protocol) {
		URLStreamHandlerFactory factory;

		synchronized (URL_STREAM_HANDLER_FACTORY_MAP) {
			factory = URL_STREAM_HANDLER_FACTORY_MAP.get(protocol);
		}
		return (factory != null ? factory.createURLStreamHandler(protocol) : null);
	}

}
