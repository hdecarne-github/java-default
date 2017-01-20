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
package de.carne.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.carne.ApplicationURLStreamHandlerFactory;
import de.carne.check.Check;
import de.carne.check.Nullable;

/**
 * Test {@link ApplicationURLStreamHandlerFactory} class.
 */
public class ApplicationURLStreamHandlerFactoryTest {

	private static final String PROTOCOL_1 = "protocol1";
	private static final String PROTOCOL_2 = "protocol2";
	private static final String PROTOCOL_3 = "protocol3";

	/**
	 * Register test protocols 1 & 2.
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void registerProtocols() throws Exception {
		TestURLStreamHandlerFactory shf1 = new TestURLStreamHandlerFactory(PROTOCOL_1);
		TestURLStreamHandlerFactory shf2 = new TestURLStreamHandlerFactory(PROTOCOL_2);

		ApplicationURLStreamHandlerFactory.registerURLStreamHandlerFactory(shf1.protocol(), shf1);
		ApplicationURLStreamHandlerFactory.registerURLStreamHandlerFactory(shf2.protocol(), shf2);
	}

	private static class TestURLStreamHandlerFactory implements URLStreamHandlerFactory {

		private final String protocol;

		TestURLStreamHandlerFactory(String protocol) {
			this.protocol = protocol;
		}

		public String protocol() {
			return this.protocol;
		}

		@Override
		public URLStreamHandler createURLStreamHandler(@Nullable String _requstedProtocol) {
			String requstedProtocol = Check.nonNull(_requstedProtocol);

			Assert.assertEquals(this.protocol, requstedProtocol);

			return new TestURLStreamHandler(requstedProtocol);
		}
	}

	private static class TestURLStreamHandler extends URLStreamHandler {

		private final String protocol;

		TestURLStreamHandler(String protocol) {
			this.protocol = protocol;
		}

		@Override
		protected URLConnection openConnection(@Nullable URL u) throws IOException {
			Assert.assertNotNull(u);
			Assert.assertEquals(this.protocol, u.getProtocol());

			return new URLConnection(u) {

				@Override
				public void connect() throws IOException {
					// Nothing to do here
				}

				@Override
				public InputStream getInputStream() throws IOException {
					return new ByteArrayInputStream(getURL().getFile().getBytes());
				}

			};
		}

	}

	/**
	 * Test whether the registered protocols are working as expected.
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void testRegisteredProtocols() throws Exception {
		URL protocol1Url = new URL(PROTOCOL_1, "", "protocol1Data");

		readAndVerifyUrl(protocol1Url);

		URL protocol2Url = new URL(PROTOCOL_2, "", "protocol2Data");

		readAndVerifyUrl(protocol2Url);
	}

	/**
	 * Test whether the not registered protocol is working as expected.
	 *
	 * @throws Exception if an error occurs.
	 */
	@Test(expected = MalformedURLException.class)
	public void testNotRegisteredProtocol() throws Exception {
		URL protocol3Url = new URL(PROTOCOL_3, "", "protocol3Data");

		readAndVerifyUrl(protocol3Url);
	}

	private void readAndVerifyUrl(URL url) throws Exception {
		try (InputStream urlStream = url.openStream()) {
			Assert.assertNotNull(urlStream);

			byte[] expectedData = url.getFile().getBytes();
			byte[] streamedData = new byte[expectedData.length];
			int streamedDataLength = urlStream.read(streamedData);

			Assert.assertEquals(streamedData.length, streamedDataLength);
			Assert.assertArrayEquals(expectedData, streamedData);
		}
	}

}
