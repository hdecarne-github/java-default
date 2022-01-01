/*
 * Copyright (c) 2016-2022 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.io;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * SHA-256 based checksum generator.
 */
public class SHA256Checksum extends MessageDigestChecksum {

	private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";

	private SHA256Checksum(MessageDigest messageDigest) {
		super(messageDigest);
	}

	/**
	 * Gets a {@linkplain SHA256Checksum} instance for checksum generation.
	 *
	 * @return a {@linkplain SHA256Checksum} instance for checksum generation.
	 * @throws NoSuchAlgorithmException if the necessary algorithm is not available on the running platform.
	 */
	public static SHA256Checksum getInstance() throws NoSuchAlgorithmException {
		return new SHA256Checksum(MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM));
	}

	/**
	 * Gets a {@linkplain SHA256Checksum} instance for checksum generation.
	 *
	 * @param provider the security provider to use.
	 * @return a {@linkplain SHA256Checksum} instance for checksum generation.
	 * @throws NoSuchProviderException if the requested provider is not available on the running platform.
	 * @throws NoSuchAlgorithmException if the necessary algorithm is not available on the running platform.
	 */
	public static SHA256Checksum getInstance(String provider) throws NoSuchProviderException, NoSuchAlgorithmException {
		return new SHA256Checksum(MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM, provider));
	}

}
