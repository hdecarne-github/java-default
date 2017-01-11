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
package de.carne.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * {@link Formatter} implementation producing a static non-localized single line
 * format output.
 */
public class LogLineFormatter extends Formatter {

	private static final int STRING_BUFFER_SIZE = 2048;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss,SSS");

	@Override
	public String format(LogRecord record) {
		String message = "...";

		try (StringWriter sw = new StringWriter(STRING_BUFFER_SIZE); PrintWriter pw = new PrintWriter(sw)) {
			pw.print(DATE_FORMAT.format(new Date(record.getMillis())));
			pw.print(" [");
			pw.print(record.getThreadID());
			pw.print("] ");
			pw.print(record.getLevel());
			pw.print(" ");
			pw.print(record.getLoggerName());
			pw.print(": ");
			pw.println(formatMessage(record));

			Throwable thrown = record.getThrown();

			if (thrown != null) {
				thrown.printStackTrace(pw);
			}
			pw.flush();
			message = sw.toString();
		} catch (Exception e) {
			System.err.println("An error occurred during log message formatting");
			e.printStackTrace();
		}
		return message;
	}

}
