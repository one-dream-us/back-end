package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.model.dto.DictionaryScrapInfo;
import com.onedreamus.project.bank.model.entity.DictionaryScrap;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.DictionaryScrapRepository;
import com.onedreamus.project.bank.repository.ScriptParagraphDictionaryRepository;
import com.onedreamus.project.bank.repository.ScriptParagraphRepository;
import java.util.List;
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
		return scriptParagraphRepository
			.findByContentIdOrderByTimestamp(contentId)
			.stream()
			.flatMap(sp -> scriptParagraphDictionaryRepository
				.findByScriptParagraphIdWithDictionary(sp.getId())
				.stream())
			.map(mapping -> {
				Optional<DictionaryScrap> dictionaryScrap = dictionaryScrapRepository
					.findByUserAndDictionaryAndIsDeletedFalse(user, mapping.getDictionary());

				return DictionaryScrapInfo.builder()
					.dictionaryId(mapping.getDictionary().getId())
					.dictionaryScrapId(dictionaryScrap.map(DictionaryScrap::getId).orElse(null))
					.isScrapped(dictionaryScrap.isPresent())
					.build();
			})
			.collect(Collectors.toMap(
				DictionaryScrapInfo::getDictionaryId,
				info -> info,
				(existing, replacement) -> existing))
			.values()
			.stream()
			.collect(Collectors.toList());
	}
}