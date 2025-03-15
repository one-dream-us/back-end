package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.NumberUtils;
import com.onedreamus.project.thisismoney.exception.QuizException;
import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final NoteService noteService;
    private final HistoryService historyService;
    private final DictionaryService dictionaryService;
    private final UserService userService;
    private final MissionService missionService;


    // 방법 1 : 앱에서 중복 처리 -> 처리 복잡, 단일 쿼리가 많아짐
    // 방법 2 : DB에 처리 전임 -> 단일 책임(앱에서는 비즈니스 로직에만 집중 가능), 처리가 간단해짐, DB 부하 많아질 수 있음
    // 방법 3 : 앱, DB 함께 활용 -> 앱에 전체 용어 저장 후 DB 에서 스크랩, 핵심, 오답, 졸업 만 조회하여 앱에서 중복 처리
    /**
     * <p>퀴즈 문제 획득</p>
     *
     */
    @Transactional
    public List<Quiz> getQuizList(Users user) {
        List<DictionaryQuiz> quizDictionaries = new ArrayList<>();

        // 1. 3문제 : 북마크 + 오답
        List<DictionaryQuiz> mainDictionaries = new ArrayList<>();
        noteService.getBookmarks(user)
                .forEach(bookmark -> mainDictionaries.add(DictionaryQuiz.from(bookmark.getDictionary(), DictionaryStatus.BOOKMARK)));
        noteService.getWrongAnswerNotes(user)
                .forEach(wrongAnswerNote ->
                        mainDictionaries.add(DictionaryQuiz.from(wrongAnswerNote.getDictionary(), DictionaryStatus.WRONG_ANSWER_NOTE)));

        // 북마크 + 오답 개수가 3개 이상이어야 퀴즈 가능
        if (mainDictionaries.size() < 3) {
            throw new QuizException(ErrorCode.NOT_ENOUGH_DICTIONARY);
        }

        // 랜덤으로 3개의 단어 뽑음
        List<Integer> randomNumList = NumberUtils.pickRandomNumList(0, mainDictionaries.size() - 1, 3);
        for (int idx : randomNumList) {
            quizDictionaries.add(mainDictionaries.get(idx));
        }

        // 2. 2문제 : 전체 단어 중 랜덤 선택
        long maxId = dictionaryService.getMaxId();

        Set<Long> dictionaryIds = mainDictionaries.stream()
                .map(DictionaryQuiz::getDictionaryId)
                .collect(Collectors.toSet());
        dictionaryIds.addAll(noteService.getAllGraduationNoteIds(user));
        dictionaryIds.addAll(historyService.getDictionaryHistoryIds(user));

        List<Dictionary> subDictionaries = getRandomDictionaries((int) maxId, 2, dictionaryIds);
        for (Dictionary dictionary : subDictionaries) {
            quizDictionaries.add(DictionaryQuiz.from(dictionary, DictionaryStatus.NONE));
        }

        // 3. Quiz 로 변환
        List<Quiz> result = new ArrayList<>();

        // 각 선지 추출
        for (DictionaryQuiz answerDictionary : quizDictionaries) {
            QuizChoice[] choices = new QuizChoice[4];

            List<DictionaryQuiz> choiceDictionary =
                    getRandomDictionaries((int) maxId, 3, new HashSet<>(List.of(answerDictionary.getDictionaryId()))).stream()
                            .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                            .toList();
            List<Integer> randomIdx = NumberUtils.shuffleNumber(0, 4);

            for (int i = 0; i < randomIdx.size(); i++) {
                if (i == 0) {
                    choices[randomIdx.get(i)] = QuizChoice.from(answerDictionary, i + 1);
                    continue;
                }

                choices[randomIdx.get(i)] = QuizChoice.from(choiceDictionary.get(i - 1), i + 1);
            }

            result.add(Quiz.from(randomIdx.get(0), choices));
        }

        return result;
    }


    /**
     * <p>[랜덤 용어 구하기]</p>
     * notBeDuplicatedNum에 중복되지 않는 dictionary를 n개 구하는 함수.
     */
    private List<Dictionary> getRandomDictionaries(int maxId, int n, Set<Long> notBeDuplicatedNum) {
        boolean[] visited = new boolean[maxId + 1];
        int cnt = 0;

        List<Long> randomIdList = new ArrayList<>();
        while (randomIdList.size() < n && cnt < maxId) {
            List<Integer> idList = NumberUtils.pickRandomNumList(1, maxId, n - randomIdList.size());
            for (int id : idList) {
                if (visited[id]) {
                    continue;
                }

                visited[id] = true;
                cnt++;
                if (!notBeDuplicatedNum.contains((long) id) && dictionaryService.contains(id)) {
                    randomIdList.add((long) id);
                }
            }
        }

        return dictionaryService.getDictionaryList(randomIdList);
    }

    /**
     * <p>[더미 퀴즈 획득]</p>
     * - 더미 퀴즈에 사용 될 퀴즈 목록 획득
     * @param user
     * @return
     */
    @Transactional
    public List<Quiz> getRandomQuizList(Users user) {
        long maxId = dictionaryService.getMaxId();
        Set<Long> scrapedDictionaryIds = historyService.getDictionaryHistoryList(user).stream()
                .map(scrap -> scrap.getDictionary().getId())
                .collect(Collectors.toSet());

        List<DictionaryQuiz> quizAnswerList = getRandomDictionaries((int) maxId, 5, scrapedDictionaryIds).stream()
                .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                .toList();

        List<Quiz> quizList = new ArrayList<>();
        for (DictionaryQuiz answerDictionary : quizAnswerList) {
            QuizChoice[] choices = new QuizChoice[4];

            Set<Long> notBeDuplicatedNum = new HashSet<>(List.of(answerDictionary.getDictionaryId()));

            List<DictionaryQuiz> choiceDictionary = new ArrayList<>();
            for (Dictionary randDictionary : getRandomDictionaries((int) maxId, 3, notBeDuplicatedNum)) {
                choiceDictionary.add(DictionaryQuiz.from(randDictionary, DictionaryStatus.NONE));
            }

            List<Integer> randomIdx = NumberUtils.shuffleNumber(0, 4);

            for (int i = 0; i < randomIdx.size(); i++) {
                if (i == 0) {
                    choices[randomIdx.get(i)] = QuizChoice.from(answerDictionary, i + 1);
                    continue;
                }

                choices[randomIdx.get(i)] = QuizChoice.from(choiceDictionary.get(i - 1), i + 1);
            }

            quizList.add(Quiz.from(randomIdx.get(0), choices));
        }

        return quizList;
    }

    /**
     * 퀴즈 결과 처리
     */
    @Transactional
    public QuizResultResponse processQuizResult(Users user, List<QuizResult> quizResults) {

        int totalCorrect = 0;
        int totalWrong = 0;
        int totalGraduation = 0;
        List<DictionaryStatusDto> resultDetails = new ArrayList<>();

        for (QuizResult quizResult : quizResults) {
            if (quizResult.isCorrect()) {
                totalCorrect++;
            } else {
                totalWrong++;
            }

            DictionaryStatusDto statusDto = noteService.changeStatus(quizResult, user);
            resultDetails.add(statusDto);
            if (statusDto.getStatus().equals(DictionaryStatus.GRADUATION_NOTE)) {
                totalGraduation++;
            }
        }

        int accuracyRate = (int) Math.round(((double) totalCorrect / quizResults.size()) * 100);

        // 첫 번째 퀴즈 시도인 경우
        if (!user.isQuizAttempt()) {
            user.setQuizAttempt(true);
            userService.saveUser(user);
        }

        // 퀴즈 미션 상태 수정
        missionService.updateQuizSolveStatus(user);

        return QuizResultResponse.from(totalGraduation, totalWrong, accuracyRate, resultDetails);
    }


}
