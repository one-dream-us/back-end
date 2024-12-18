package com.onedreamus.project.global.util;

public class UrlUtils {
	// URL에서 비디오 ID를 추출하는 메서드
	public static String extractVideoId(String contentUrl) {
		if (contentUrl == null || !contentUrl.contains("v=")) {
			return null;
		}
		String[] parts = contentUrl.split("v=");
		if (parts.length < 2) {
			return null;
		}
		String videoId = parts[1];
		int ampersandIndex = videoId.indexOf("&");
		if (ampersandIndex != -1) {
			videoId = videoId.substring(0, ampersandIndex);
		}
		return videoId;
	}

}
