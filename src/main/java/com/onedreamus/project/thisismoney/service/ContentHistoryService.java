package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.ContentHistoryCountResponse;
import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentHistory;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.ContentHistoryRepository;
import com.onedreamus.project.thisismoney.repository.ContentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentHistoryService {

	private final ContentHistoryRepository contentHistoryRepository;
	private final ContentRepository contentRepository;

	public ContentHistoryCountResponse getContentHistoryCount(Users user) {
		Long count = contentHistoryRepository.countByUserAndIsDeleted(user, false);

		return ContentHistoryCountResponse.builder()
			.userId(user.getId())
			.watchedCount(count)
			.build();
	}

	@Transactional(readOnly = false)
	public void saveHistory(Long contentId, Users user) {
		Content content = contentRepository.findById(contentId)
			.orElseThrow(() -> new EntityNotFoundException("Content not found"));

		if (!contentHistoryRepository.existsByUserAndContent(user, content)) {
			contentHistoryRepository.save(
				ContentHistory.builder()
					.content(content)
					.user(user)
					.isDeleted(false)
					.build()
			);
		}
	}

	public void deleteAllHistory(Users user){
		List<ContentHistory> allContentHistory = contentHistoryRepository.findAllByUser(user);
		for (ContentHistory contentHistory : allContentHistory) {
			contentHistory.setIsDeleted(true);
		}

		contentHistoryRepository.saveAll(allContentHistory);
	}

}
