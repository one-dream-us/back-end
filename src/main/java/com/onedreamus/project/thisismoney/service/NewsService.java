package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.s3.ImageCategory;
import com.onedreamus.project.global.s3.S3Uploader;
import com.onedreamus.project.global.util.NumberFormatter;
import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import com.onedreamus.project.thisismoney.model.dto.content.PopularNewsResponse;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.model.projection.TotalViewCountProjection;
import com.onedreamus.project.thisismoney.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsViewRepository newsViewRepository;
    private final SentenceRepository sentenceRepository;
    private final DictionarySentenceRepository dictionarySentenceRepository;
    private final UsersStudyDaysRepository usersStudyDaysRepository;
    private final DictionaryService dictionaryService;
    private final AgencyService agencyService;
    private final S3Uploader s3Uploader;

    public CursorResult<NewsListResponse> getNewList(Integer cursor, Integer size) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);
        List<News> newsList = newsRepository.findByIdLessThanOrderByIdDesc(cursor, pageRequest);
        long totalElements = newsRepository.count();

        boolean hasNext = newsList.size() > size;
        if (hasNext) {
            newsList.remove(newsList.size() - 1);
        }

        List<NewsListResponse> responses = newsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        Long nextCursor = hasNext && !responses.isEmpty() ?
                responses.get(responses.size() - 1).getNewsId() : null;

        return CursorResult.<NewsListResponse>builder()
                .contents(responses)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .totalElements(totalElements)
                .build();
    }

    private NewsListResponse convertToResponse(News news) {
        List<String> tags = getTags(news);

        Integer viewCount = newsViewRepository.findTotalViewCountByNews(news)
                .orElse(0);

        String formattedViewCount = NumberFormatter.format(viewCount);

        return NewsListResponse.from(news, formattedViewCount, tags);
    }

    /**
     * <p>News 의 태그 획득</p>
     *
     * @param news
     * @return
     */
    private List<String> getTags(News news) {
        List<String> tags = new ArrayList<>();

        List<Sentence> sentences = sentenceRepository.findByNews(news);
        List<Dictionary> dictionaries =
                dictionarySentenceRepository.findDictionaryBySentenceIn(sentences);
        for (Dictionary dictionary : dictionaries) {
            tags.add(dictionary.getTerm()
            );
        }

        return tags;
    }


    /**
     * <p>로그인 유저의 news 상세페이지 조회</p>
     * 로그인 한 유저의 경우 학습 일수가 누적된다.
     *
     * @param newsId
     * @param user
     * @return
     */
    @Transactional
    public NewsDetailResponse getNewsDetail(int newsId, Users user) {
        NewsDetailResponse newsDetailResponse = getNewsDetail(newsId);

        // 학습 일수 누적
        increaseStudyDay(user);
        return newsDetailResponse;
    }

    /**
     * <p>학습 일수 누적</p>
     *
     * @param user
     */
    private void increaseStudyDay(Users user) {
        boolean isStudyDone = usersStudyDaysRepository.existsByUserAndStudyDate(user,
                LocalDate.now());
        if (!isStudyDone) {
            usersStudyDaysRepository.save(UsersStudyDays.builder()
                    .user(user)
                    .studyDate(LocalDate.now())
                    .build());
        }
    }

    /**
     * news 상세페이지 조회
     */
    @Transactional
    public NewsDetailResponse getNewsDetail(int newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        NewsDetailResponse response = buildNewsDetailResponse(news);

        // 조회수 증가
        increaseNewsViewCount(news);

        return response;
    }

    @Transactional(readOnly = true)
    public NewsDetailResponse getNewsDetailWithoutViewIncrease(int newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        return buildNewsDetailResponse(news);
    }

    private NewsDetailResponse buildNewsDetailResponse(News news) {
        List<Sentence> sentences = sentenceRepository.findByNews(news);

        StringBuilder fullSentenceBuilder = new StringBuilder();
        for (Sentence sentence : sentences) {
            fullSentenceBuilder.append(sentence.getValue());
        }

        List<DictionaryDescriptionDto> descriptionDtos = new ArrayList<>();
        for (Sentence sentence : sentences) {
            DictionarySentence dictionarySentences =
                    dictionarySentenceRepository.findBySentence(sentence)
                            .orElse(new DictionarySentence());
            descriptionDtos.add(DictionaryDescriptionDto.from(sentence.getValue(),
                    dictionarySentences.getDictionary()));
        }

        return NewsDetailResponse.from(news, fullSentenceBuilder.toString(), descriptionDtos);
    }

    /**
     * NewsView 의 viewCount + 1
     *
     * @param news
     */
    private void increaseNewsViewCount(News news) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59, 999999999);

        NewsView newsView = newsViewRepository.findByNewsAndViewDateBetween(news, startOfDay, endOfDay)
                .orElseGet(() -> newsViewRepository.save(NewsView.from(news)));

        newsView.increaseViewCount();
        newsViewRepository.save(newsView);
    }

    /**
     * <p>[최신 뉴스 콘텐츠 조회]</p>
     * 가장 최근에 올라온 뉴스 콘텐츠를 조회합니다.
     *
     * @return
     */
    public NewsListResponse getLatestNews() {
        News latestNews = newsRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        int totalViewCnt = newsViewRepository.findTotalViewCountByNews(latestNews)
                .orElse(0);

        List<String> tags = getTags(latestNews);

        return NewsListResponse.from(latestNews, NumberFormatter.format(totalViewCnt), tags);
    }


    public Optional<News> getNewsById(Integer newsId) {
        return newsRepository.findById(newsId);
    }

    @Transactional
    public void uploadScheduledNews(NewsContent newsContent,
                                    List<DictionarySentenceRequest> dictionarySentenceRequests) {
        News newNews = newsRepository.save(News.from(newsContent.getTitle(),
                newsContent.getThumbnailUrl(),
                newsContent.getNewsAgency(),
                newsContent.getOriginalLink()));
        uploadNews(newNews, dictionarySentenceRequests);
    }

    @Transactional
    public void uploadNews(NewsRequest newsRequest, MultipartFile thumbnailImage,
                           List<DictionarySentenceRequest> dictionarySentenceRequests) {
        String thumbnailUrl = s3Uploader.uploadMultipartFileByStream(
                thumbnailImage, ImageCategory.THUMBNAIL);
        News newNews = newsRepository.save(News.from(
                newsRequest.getTitle(),
                thumbnailUrl,
                newsRequest.getNewsAgency(),
                newsRequest.getOriginalLink()));
        uploadNews(newNews, dictionarySentenceRequests);
    }

    /**
     * <p>뉴스 콘텐츠 즉시 등록</p>
     */
    @Transactional
    private void uploadNews(News news, List<DictionarySentenceRequest> dictionarySentenceRequests) {
        agencyService.saveIfNotExist(news.getNewsAgency());

        for (DictionarySentenceRequest request : dictionarySentenceRequests) {
            Dictionary dictionary;
            // 기존 단어 재사용 하는 경우
            if (request.getDictionaryId() != null) {
                dictionary = dictionaryService.getDictionaryById(request.getDictionaryId())
                        .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));

            } else { // 새로운 용어를 등록하여 매핑하는 경우
                // 새로운 용어 생성
                String markedDefinition = addHighlightMarkDefinition(
                        request.getDictionaryDefinition(), request.getDictionaryTerm());
                dictionary = dictionaryService.saveNewDictionary(
                        Dictionary.from(request.getDictionaryTerm(), markedDefinition,
                                request.getDictionaryDescription()));
            }

            // 하이라이팅 필요한 부분에 <mark> 표시 추가
            String markedSentence = addHighlightMark(
                    request.getSentenceValue(), request.getStartIdx(), request.getEndIdx());

            // 문장 저장
            Sentence newSentence = sentenceRepository.save(Sentence.from(markedSentence, news));

            // 용어-문장 저장
            dictionarySentenceRepository.save(DictionarySentence.from(dictionary, newSentence));

        }
    }


    public String addHighlightMarkDefinition(String definition, String term) {
        int startIdx = definition.indexOf(term);
        // 예외 방지 - 기존 데이터 하이라이팅 없이 유지
        if (startIdx == -1) {
            return definition;
        }

        int endIdx = startIdx + term.length() - 1;

        return addHighlightMark(definition, startIdx, endIdx);
    }

    public String addHighlightMark(String sentence, int startIdx, int endIdx) {
        // 하이라이팅을 할 수 없는 경우 하이라이팅 없음
        if (startIdx < 0 || endIdx >= sentence.length() || startIdx > endIdx) {
            return sentence;
        }

        return sentence.substring(0, startIdx) +
                "<mark>" + sentence.substring(startIdx, endIdx + 1) + "</mark>" +
                sentence.substring(endIdx + 1);
    }

    public DictionaryDescriptionDto highlightDefinitionAndSentence(DictionarySentenceRequest dictionarySentenceRequest) {
        String highlightedDefinition = addHighlightMarkDefinition(
                dictionarySentenceRequest.getDictionaryDefinition(),
                dictionarySentenceRequest.getDictionaryTerm()
        );

        String highlightedSentence = addHighlightMark(
                dictionarySentenceRequest.getSentenceValue(),
                dictionarySentenceRequest.getStartIdx(),
                dictionarySentenceRequest.getEndIdx()
        );

        return DictionaryDescriptionDto.fromWithHighlighting(dictionarySentenceRequest, highlightedDefinition, highlightedSentence);
    }

    public Page<NewsResponse> getNewsList(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NewsResponse::from);
    }

    /**
     * <p>주간 인기 뉴스 콘텐츠 조회</p>
     * 지난주 인기 뉴스 콘텐츠를 조회,
     * size 만큼의 인기 뉴스 콘텐츠 조회
     *
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "popularNewsCache", key = "#size", unless = "#result == null or #result.isEmpty()")
    public List<PopularNewsResponse> getPopularNews(Integer size) {
        LocalDateTime beforeOneWeek = LocalDateTime.now().minusDays(7); // 1주일 전
        List<TotalViewCountProjection> newsViews = newsViewRepository.findWeeklyTopNewsViews(PageRequest.of(0,size), beforeOneWeek);

        return newsViews.stream()
                .map(newsView -> PopularNewsResponse.from(newsView.getNews(), newsView.getTotalViewCount(), getTags(newsView.getNews())))
                .toList();
    }
}
