package com.pipewatch.global.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
	public static String convertToDateFormat(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return localDateTime.format(formatter);
	}

	private static class TIME_MAXIMUM {
		public static final int SEC = 60;
		public static final int MINUTE = 60;
		public static final int HOUR = 24;
	}
}

