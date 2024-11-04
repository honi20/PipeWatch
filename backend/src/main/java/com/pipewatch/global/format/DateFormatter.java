package com.pipewatch.global.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
	public static String convertToDateFormat() {
		LocalDateTime now = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");

		return now.format(formatter);
	}

	private static class TIME_MAXIMUM {
		public static final int SEC = 60;
		public static final int MINUTE = 60;
		public static final int HOUR = 24;
	}
}

