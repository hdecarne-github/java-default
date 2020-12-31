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
package de.carne.io;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 *
 */
public abstract class MessageDigestChecksum implements Checksum {

	private final MessageDigest messageDigest;

	protected MessageDigestChecksum(MessageDigest digest) {
		this.messageDigest = digest;
	}

	@Override
	public void reset() {
		this.messageDigest.reset();
	}

	@Override
	public void update(byte b) {
		this.messageDigest.update(b);
	}

	@Override
	public void update(byte[] bs) {
		this.messageDigest.update(bs);
	}

	@Override
	public void update(byte[] bs, int off, int len) {
		this.messageDigest.update(bs, off, len);
	}

	@Override
	public void update(ByteBuffer bs) {
		this.messageDigest.update(bs);
	}

	@Override
	public byte[] getValue() {
		return this.messageDigest.digest();
	}

}
