package com.onedreamus.project.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	// <mark> 태그 내 용어를 추출하는 메서드
	public static List<String> extractMarkedTerms(String paragraphText) {
		List<String> markedTerms = new ArrayList<>();
		Matcher matcher = Pattern.compile("<mark>(.*?)</mark>").matcher(paragraphText);

		while (matcher.find()) {
			markedTerms.add(matcher.group(1)); // <mark>태그 안의 용어 추가
		}

		return markedTerms;
	}
}
