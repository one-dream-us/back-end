package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.DictionaryScrapInfo;
import com.onedreamus.project.thisismoney.model.entity.DictionaryScrap;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.DictionaryScrapRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphDictionaryRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphRepository;
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
		// 사용자 스크랩 정보를 한 번에 가져오기
		List<DictionaryScrap> userScraps = dictionaryScrapRepository.findByUserAndIsDeletedFalse(user);

		// 사용자 스크랩 정보를 Map으로 변환 (DictionaryId -> DictionaryScrap)
		Map<Long, DictionaryScrap> userScrapMap = userScraps.stream()
			.collect(Collectors.toMap(scrap -> scrap.getDictionary().getId(), scrap -> scrap));

		// 스크립트 문단과 관련된 Dictionary 정보를 처리
		return scriptParagraphRepository
			.findByContentIdOrderByTimestamp(contentId)
			.stream()
			.flatMap(sp -> scriptParagraphDictionaryRepository
				.findByScriptParagraphIdWithDictionary(sp.getId())
				.stream()
				.filter(mapping -> mapping.getDictionary() != null)) // Dictionary가 있을 경우
			.map(mapping -> {
				// Map에서 스크랩 정보 조회
				DictionaryScrap dictionaryScrap = userScrapMap.get(mapping.getDictionary().getId());

				return DictionaryScrapInfo.builder()
					.dictionaryId(mapping.getDictionary().getId())
					.dictionaryScrapId(dictionaryScrap != null ? dictionaryScrap.getId() : null)
					.isScrapped(dictionaryScrap != null)
					.build();
			})
			.collect(Collectors.toList());
	}
}