package com.onedreamus.project.global.util;

public class NumberFormatter {
	public static String format(int viewCount) {
		if (viewCount < 10000) {
			return String.valueOf(viewCount);
		}

		double count = viewCount / 10000.0;
		if (count % 1 == 0) {
			return String.format("%.0f만", count);
		}
		return String.format("%.1f만", count);
	}
}
