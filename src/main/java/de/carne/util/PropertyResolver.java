/*
 * Copyright (c) 2016-2021 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.util;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import de.carne.util.logging.Log;

/**
 * Utility class providing dynamic property resolution support.
 */
public final class PropertyResolver {

	private static final Log LOG = new Log();

	private final Map<String, String> properties;
	private final boolean useSystemProperties;
	private final boolean useEnvironment;

	/**
	 * Constructs a new {@linkplain PropertyResolver} instance.
	 *
	 * @see #resolve(String)
	 */
	public PropertyResolver() {
		this(Collections.emptyMap(), true, true);
	}

	/**
	 * Constructs a new {@linkplain PropertyResolver} instance.
	 *
	 * @param properties the custom properties to consider while resolving.
	 * @see #resolve(String)
	 */
	public PropertyResolver(Map<?, ?> properties) {
		this(properties, true, true);
	}

	/**
	 * Constructs a new {@linkplain PropertyResolver} instance.
	 *
	 * @param useSystemProperties whether to consider system properties while resolving.
	 * @param useEnvironemnt whether to consider environment variables while resolving.
	 * @see #resolve(String)
	 */
	public PropertyResolver(boolean useSystemProperties, boolean useEnvironemnt) {
		this(Collections.emptyMap(), useSystemProperties, useEnvironemnt);
	}

	/**
	 * Constructs a new {@linkplain PropertyResolver} instance.
	 *
	 * @param properties the custom properties to consider while resolving.
	 * @param useSystemProperties whether to consider system properties while resolving.
	 * @param useEnvironemnt whether to consider environment variables while resolving.
	 * @see #resolve(String)
	 */
	public PropertyResolver(Map<?, ?> properties, boolean useSystemProperties, boolean useEnvironemnt) {
		this.properties = new HashMap<>(properties.size());
		for (Map.Entry<?, ?> property : properties.entrySet()) {
			this.properties.put(String.valueOf(property.getKey()), String.valueOf(property.getValue()));
		}
		this.useSystemProperties = useSystemProperties;
		this.useEnvironment = useEnvironemnt;
	}

	/**
	 * Resolves the given key by searching the sources selected during instance creation.
	 * <p>
	 * The sources are searched in the following order:
	 * <ol>
	 * <li>Custom properties</li>
	 * <li>System properties</li>
	 * <li>Environment variables</li>
	 * </ol>
	 * For Custom and system properties the key is used unchanged. For environment variables the key is converted to
	 * upper case and any period character ({@code '.'}) is replaced by an underscore ({@code '_'}).
	 *
	 * @param key the key to resolve.
	 * @return the resolved value or {@code null} if the key is undefined.
	 */
	@Nullable
	public String resolve(String key) {
		String value = this.properties.get(key);

		if (value == null && this.useSystemProperties) {
			value = System.getProperty(key);
		}
		if (value == null && this.useEnvironment) {
			value = System.getenv(key.replace('.', '_').toUpperCase());
		}

		LOG.debug("Resolved ''{0}'' to ''{1}''", key, value);

		return value;
	}

	/**
	 * Resolves the given key by searching the sources selected during instance creation.
	 *
	 * @param key the key to resolve.
	 * @param defaultValue the value to return in case the key is undefined.
	 * @return the resolved value or {@code defaultValue} if the key is undefined.
	 * @see #resolve(String)
	 */
	public String resolve(String key, String defaultValue) {
		String value = resolve(key);

		return (value != null ? value : defaultValue);
	}

	/**
	 * Expands the given string by resolving all embedded property keys.
	 *
	 * @param s the string to expand.
	 * @return the expanded string.
	 * @throws ParseException if a parse or resolve error occurs during expansion.
	 */
	public String expand(String s) throws ParseException {
		Expansions expansions = new Expansions(s);
		StringBuilder expanded = null;

		if (expansions.hasRemaining()) {
			expanded = new StringBuilder();
			do {
				expansions.processAndAdvance(expanded, this);
			} while (expansions.hasRemaining());
			expansions.flush(expanded);
		}
		return (expanded != null ? expanded.toString() : s);
	}

	private static class Expansions {

		private static final char TAG = '$';
		private static final char LBRACE = '{';
		private static final char RBRACE = '}';

		private final String s;
		private int fromIndex = 0;
		private int toIndex;

		Expansions(String s) {
			this.s = s;
			this.toIndex = s.indexOf(TAG);
		}

		public boolean hasRemaining() {
			return 0 <= this.toIndex && this.toIndex < this.s.length();
		}

		public void flush(StringBuilder expanded) {
			int flushIndex = (this.toIndex >= 0 ? this.toIndex : this.s.length());

			if (this.fromIndex < flushIndex) {
				expanded.append(this.s, this.fromIndex, flushIndex);
				this.fromIndex = flushIndex;
			}
		}

		public void processAndAdvance(StringBuilder expanded, PropertyResolver resolver) throws ParseException {
			flush(expanded);

			char peek = peek();

			if (peek == LBRACE) {
				processKey(expanded, resolver);
			} else if (peek == TAG) {
				processQuote(expanded);
			} else {
				throw new ParseException("Unexpected char: " + peek, this.toIndex);
			}
			this.toIndex = this.s.indexOf(TAG, this.fromIndex);
		}

		private void processKey(StringBuilder expanded, PropertyResolver resolver) throws ParseException {
			int keyIndex = this.toIndex + 2;
			int rBraceIndex = this.s.indexOf(RBRACE, keyIndex);

			if (rBraceIndex < 0) {
				throw new ParseException("Missing }", this.toIndex);
			}

			String key = this.s.substring(keyIndex, rBraceIndex);
			String value = resolver.resolve(key);

			if (value == null) {
				throw new ParseException("Unknown key: " + key, keyIndex);
			}

			expanded.append(value);
			this.fromIndex = rBraceIndex + 1;
		}

		private void processQuote(StringBuilder expanded) {
			expanded.append(TAG);
			this.fromIndex = this.toIndex + 2;
		}

		private char peek() {
			int peekIndex = this.toIndex + 1;

			return (0 < peekIndex && peekIndex < this.s.length() ? this.s.charAt(peekIndex) : '\0');
		}

	}

}
