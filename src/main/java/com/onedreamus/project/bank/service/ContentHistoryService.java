package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.model.dto.ContentHistoryCountResponse;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.ContentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentHistoryService {

	private final ContentHistoryRepository contentHistoryRepository;

	public ContentHistoryCountResponse getContentHistoryCount(Users user) {
		Long count = contentHistoryRepository.countByUser(user);

		return ContentHistoryCountResponse.builder()
			.userId(user.getId())
			.watchedCount(count)
			.build();
	}

}
