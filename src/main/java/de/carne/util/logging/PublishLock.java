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
package de.carne.util.logging;

import de.carne.util.Lazy;

final class PublishLock implements AutoCloseable {

	private static final Lazy<PublishLock> INSTANCE_HOLDER = new Lazy<>(PublishLock::new);

	private final ThreadLocal<Boolean> locked = ThreadLocal.withInitial(() -> Boolean.FALSE);

	private PublishLock() {
		// Prevent instantiation outside this class
	}

	public static PublishLock getInstance() {
		return INSTANCE_HOLDER.get();
	}

	public void ifNotLocked(Runnable publisher) {
		if (!this.locked.get().booleanValue()) {
			try {
				this.locked.set(Boolean.TRUE);
				publisher.run();
			} finally {
				this.locked.set(Boolean.FALSE);
			}
		}
	}

	@Override
	public void close() {
		this.locked.remove();
	}

}
