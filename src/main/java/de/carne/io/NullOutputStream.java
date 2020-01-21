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

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain OutputStream} implementation which sends any written data into oblivion.
 */
public class NullOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
		// ignore
	}

	@Override
	public void write(byte @Nullable [] b) throws IOException {
		// ignore
	}

	@Override
	public void write(byte @Nullable [] b, int off, int len) throws IOException {
		// ignore
	}

}
