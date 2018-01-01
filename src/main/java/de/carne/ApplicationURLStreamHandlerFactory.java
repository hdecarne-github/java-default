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
package de.carne;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

import de.carne.check.Nullable;

final class ApplicationURLStreamHandlerFactory implements URLStreamHandlerFactory {

	private final Map<String, URLStreamHandlerFactory> factoryMap = new HashMap<>();

	private ApplicationURLStreamHandlerFactory() {
		// Prevent instantiation from outside
	}

	static final ApplicationURLStreamHandlerFactory SINGLETON = new ApplicationURLStreamHandlerFactory();

	static {
		URL.setURLStreamHandlerFactory(SINGLETON);
	}

	synchronized URLStreamHandlerFactory register(String protocol, URLStreamHandlerFactory factory) {
		return this.factoryMap.put(protocol, factory);
	}

	@Override
	@Nullable
	public synchronized URLStreamHandler createURLStreamHandler(@Nullable String protocol) {
		URLStreamHandlerFactory factory = this.factoryMap.get(protocol);

		return (factory != null ? factory.createURLStreamHandler(protocol) : null);
	}

}
