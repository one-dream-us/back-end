package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.s3.ImageCategory;
import com.onedreamus.project.global.s3.S3Uploader;
import com.onedreamus.project.thisismoney.exception.BackOfficeException;
import com.onedreamus.project.thisismoney.model.dto.DictionarySentenceRequest;
import com.onedreamus.project.thisismoney.model.dto.backOffice.DraftNewsRequest;
import com.onedreamus.project.thisismoney.model.dto.backOffice.DraftNewsResponse;
import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import com.onedreamus.project.thisismoney.model.entity.DraftNews;
import com.onedreamus.project.thisismoney.repository.DraftNewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DraftNewsService {

    private final DraftNewsRepository draftNewsRepository;
    private final S3Uploader s3Uploader;

    /**
     * <p>콘텐츠 임시 저장</p>
     * @param draftNewsRequest
     * @param dictionarySentenceList
     * @param thumbnailImage
     */
    @Transactional
    public void saveDrafts(DraftNewsRequest draftNewsRequest, List<DictionarySentenceRequest> dictionarySentenceList,
                           MultipartFile thumbnailImage){

        // 기존 임시 저장 콘텐츠의 ID를 가지고 있을 경우 수정 로직 진행
        if (draftNewsRequest.getDraftNewsId() != null) {
            updateDrafts(draftNewsRequest, dictionarySentenceList, thumbnailImage);
            return;
        }

        String thumbnailUrl = null;
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            thumbnailUrl = s3Uploader.uploadMultipartFileByStream(thumbnailImage, ImageCategory.THUMBNAIL);
        }

        NewsContent newsContent = NewsContent.from(draftNewsRequest, thumbnailUrl, dictionarySentenceList);

        draftNewsRepository.save(DraftNews.from(newsContent, draftNewsRequest.getScheduledAt()));
    }

    /**
     * 임시 저장 콘텐츠 수정
     */
    @Transactional
    private void updateDrafts(DraftNewsRequest draftNewsRequest, List<DictionarySentenceRequest> dictionarySentenceList,
                              MultipartFile thumbnailImage){
        DraftNews draftNews = draftNewsRepository.findById(draftNewsRequest.getDraftNewsId())
                .orElseThrow(() -> new BackOfficeException(ErrorCode.CONTENT_NOT_EXIST));

        // 새 이미지가 있다면 기존 이미지 삭제 후 새 이미지 업로드
        String thumbnailUrl = null;
        if (thumbnailImage != null && !thumbnailImage.isEmpty()){
            if (draftNews.getNewsContent().getThumbnailUrl() != null) {
                s3Uploader.deleteImageByUrl(draftNews.getNewsContent().getThumbnailUrl());
            }
            thumbnailUrl = s3Uploader.uploadMultipartFileByStream(thumbnailImage, ImageCategory.THUMBNAIL);
        }

        NewsContent newsContent = NewsContent.from(draftNewsRequest, thumbnailUrl, dictionarySentenceList);

        // 기존 값 수정
        draftNews.setNewsContent(newsContent);
        draftNews.setScheduledAt(draftNewsRequest.getScheduledAt());
        draftNews.setCreatedAt(LocalDateTime.now());

        // 수정된 값 저장
        draftNewsRepository.save(draftNews);
    }

    /**
     * <p>임시 저장 콘텐츠 리스트 조회</p>
     * @return
     */
    public Page<DraftNewsResponse> getDraftList(Pageable pageable) {
        return draftNewsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(DraftNewsResponse::from);
    }

    /**
     * 주어진 draftId를 사용하여 임시 저장된 뉴스 콘텐츠의 상세 정보를 조회합니다.
     *
     * @param draftId 임시 저장된 뉴스 콘텐츠의 고유 식별자 (Long 타입)
     * @return 임시 저장된 뉴스 콘텐츠의 상세 정보를 담은 DraftNewsResponse 객체
     * @throws BackOfficeException 해당 draftId에 대한 콘텐츠를 찾을 수 없는 경우 발생
     */
    public DraftNewsResponse getDraftNews(Integer draftId) {
        return draftNewsRepository.findById(draftId)
                .map(DraftNewsResponse::from)
                .orElseThrow(() -> new BackOfficeException(ErrorCode.CONTENT_NOT_EXIST));
    }

    public void deleteDraftNews(Integer draftId) {
        draftNewsRepository.deleteById(draftId);
    }
}
