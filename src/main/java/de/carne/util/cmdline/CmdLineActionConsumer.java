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
package de.carne.util.cmdline;

import java.util.function.Consumer;

/**
 * {@code CmdLineAction} implementation for switch and unnamed arguments. In the
 * first case the action is invoked with the argument name only (switch) in the
 * second case with the (unnamed) option string.
 */
class CmdLineActionConsumer extends CmdLineAction implements Consumer<String> {

	private Consumer<String> consumer;

	CmdLineActionConsumer(Consumer<String> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void accept(String t) throws CmdLineActionException {
		this.consumer.accept(t);
	}

}
