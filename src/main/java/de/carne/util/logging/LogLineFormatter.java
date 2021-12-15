/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.eclipse.jdt.annotation.Nullable;

/**
 * {@linkplain Formatter} implementation providing a simple static single line (except for stack trace information) log
 * format.
 */
public class LogLineFormatter extends Formatter {

	/**
	 * The {@linkplain DateTimeFormatter} used for record timestamp formatting.
	 */
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

	@Override
	public String format(@Nullable LogRecord logRecord) {
		String message = null;

		if (logRecord != null) {
			try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
				pw.print(formatMillis(logRecord));
				pw.print(" [");
				pw.print(logRecord.getThreadID());
				pw.print("] ");
				pw.print(logRecord.getLevel());
				pw.print(" ");
				pw.print(logRecord.getLoggerName());
				pw.print(": ");
				pw.println(formatMessage(logRecord));

				Throwable thrown = logRecord.getThrown();

				if (thrown != null) {
					thrown.printStackTrace(pw);
				}
				pw.flush();
				message = sw.toString();
			} catch (Exception e) {
				Logs.DEFAULT_ERROR_MANAGER.error("Failed to format log record", e, ErrorManager.FORMAT_FAILURE);
			}
		}
		return (message != null ? message : "...");
	}

	/**
	 * Formats a {@linkplain LogRecord}'s time attribute.
	 * 
	 * @param logRecord the {@linkplain LogRecord} to format.
	 * @return the formatted {@linkplain LogRecord} time.
	 * @see LogRecord#getMillis()
	 */
	public String formatMillis(LogRecord logRecord) {
		LocalDateTime recordTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(logRecord.getMillis()),
				ZoneId.systemDefault());

		return DATE_TIME_FORMAT.format(recordTimestamp);
	}

}
