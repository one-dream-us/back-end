package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.util.StringUtils;
import com.onedreamus.project.thisismoney.model.dto.DictionaryScrapInfo;
import com.onedreamus.project.thisismoney.model.entity.DictionaryScrap;
import com.onedreamus.project.thisismoney.model.entity.ScriptParagraphDictionary;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.DictionaryScrapRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphDictionaryRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DictionaryScrapService {
	private final ScriptParagraphRepository scriptParagraphRepository;
	private final ScriptParagraphDictionaryRepository scriptParagraphDictionaryRepository;
	private final DictionaryScrapRepository dictionaryScrapRepository;

	public List<DictionaryScrapInfo> getUserDictionaryScrapStatus(Long contentId, Users user) {
		List<DictionaryScrap> userScraps = dictionaryScrapRepository.findByUserAndIsDeletedFalse(user);
		// 사용자 스크랩 정보를 Map으로 변환 (DictionaryId -> DictionaryScrap)
		Map<Long, DictionaryScrap> userScrapMap = userScraps.stream()
			.collect(Collectors.toMap(scrap -> scrap.getDictionary().getId(), scrap -> scrap));

		// 스크립트 문단과 관련된 Dictionary 정보를 처리
		List<DictionaryScrapInfo> result = new ArrayList<>();

		// 문단별로 <mark> 용어를 추출하고 그 순서에 맞게 정렬
		scriptParagraphRepository
			.findByContentIdOrderByTimestamp(contentId)
			.forEach(sp -> {
				// <mark> 태그 내 용어 추출
				List<String> markedTerms = StringUtils.extractMarkedTerms(sp.getParagraphText());

				// 해당 문단과 관련된 Dictionary를 가져오기
				List<ScriptParagraphDictionary> mappings = scriptParagraphDictionaryRepository
					.findByScriptParagraphIdWithDictionary(sp.getId())
					.stream()
					.filter(mapping -> mapping.getDictionary() != null) // Dictionary가 있을 경우
					.collect(Collectors.toList());

				// 용어 순서대로 Dictionary들을 정렬
				mappings.sort(Comparator.comparingInt(mapping -> markedTerms.indexOf(mapping.getDictionary().getTerm())));

				// 정렬된 순서대로 DictionaryScrapInfo를 생성
				mappings.forEach(mapping -> {
					DictionaryScrap dictionaryScrap = userScrapMap.get(mapping.getDictionary().getId());

					result.add(DictionaryScrapInfo.builder()
						.dictionaryId(mapping.getDictionary().getId())
						.dictionaryScrapId(dictionaryScrap != null ? dictionaryScrap.getId() : null)
						.isScrapped(dictionaryScrap != null)
						.build());
				});
			});

		return result;
	}


}