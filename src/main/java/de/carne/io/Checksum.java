/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

/**
 * Interface for any kind of checksum generator.
 */
public interface Checksum {

	/**
	 * Resets the generator to it's start state.
	 */
	void reset();

	/**
	 * Feeds a single byte into the generator.
	 * 
	 * @param b the byte to feed.
	 */
	void update(byte b);

	/**
	 * Feeds a byte array into the generator.
	 * 
	 * @param bs the byte array to feed.
	 */
	void update(byte[] bs);

	/**
	 * Feeds a byte array into the generator.
	 * 
	 * @param bs the byte array to feed.
	 * @param off the first byte to feed.
	 * @param len the number of bytes to feed.
	 */
	void update(byte[] bs, int off, int len);

	/**
	 * Feeds a {@linkplain ByteBuffer} into the generator.
	 * 
	 * @param bs the {@linkplain ByteBuffer} to feed.
	 */
	void update(ByteBuffer bs);

	/**
	 * Finalizes the checksum generation, returns the result and resets the generator.
	 * 
	 * @return the generated checksum.
	 */
	byte[] getValue();

}
