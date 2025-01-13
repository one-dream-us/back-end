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
    private final ScrapService scrapService;
    private final DictionaryService dictionaryService;

    /**
     * 퀴즈 문제 획득
     */
    public List<Quiz> getQuizList(Users user) {
        List<DictionaryQuiz> quizDictionaries = new ArrayList<>();

        // 1. 3문제 : 핵심 + 오답
        List<DictionaryQuiz> dictionaries1 = new ArrayList<>();
        noteService.getKeynotes(user)
                .forEach(keynote -> dictionaries1.add(
                        DictionaryQuiz.from(keynote.getDictionary(), DictionaryStatus.KEY_NOTE)));
        noteService.getWrongAnswerNotes(user)
                .forEach(wrongAnswerNote -> dictionaries1.add(
                        DictionaryQuiz.from(wrongAnswerNote.getDictionary(), DictionaryStatus.WRONG_ANSWER_NOTE)));

        // 핵심 + 오답 개수가 3개 이상이어야 퀴즈 가능
        if (dictionaries1.size() < 3) {
            throw new QuizException(ErrorCode.NOT_ENOUGH_DICTIONARY);
        }

        // 랜덤으로 3개의 단어 뽑음
        Collections.shuffle(dictionaries1);
        quizDictionaries.addAll(dictionaries1.subList(0, 3));


        // 2. 2문제 : 전체 단어 중 랜덤 선택
        long maxId = dictionaryService.getMaxId();
        Set<Long> dictionaryIds = dictionaries1.stream()
                .map(DictionaryQuiz::getDictionaryId)
                .collect(Collectors.toSet());
        dictionaryIds.addAll(noteService.getAllGraduationNote(user).stream()
                .map(graduationNote -> graduationNote.getDictionary().getId())
                .toList());
        dictionaryIds.addAll(scrapService.getDictionaryScrapList(user).stream()
                .map(scrap -> scrap.getDictionary().getId())
                .toList());

        // 랜덤으로 전체 단어에서 2개 뽑음
        List<DictionaryQuiz> dictionaries2 =
                getRandomDictionary(maxId, 2, dictionaryIds).stream()
                        .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                        .toList();

        quizDictionaries.addAll(dictionaries2);

        // 3. Quiz 로 변환
        List<Quiz> result = new ArrayList<>();

        // 각 선지 추출
        for (DictionaryQuiz answerDictionary : quizDictionaries) {
            QuizChoice[] choices = new QuizChoice[4];

            List<DictionaryQuiz> choiceDictionary =
                    getRandomDictionary(
                            maxId,
                            3,
                            new HashSet<>(List.of(answerDictionary.getDictionaryId()))
                    ).stream()
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
    private List<Dictionary> getRandomDictionary(long maxId, int n, Set<Long> notBeDuplicatedNum) {
        List<Long> randomNumList = NumberUtils.pickRandomNumber(maxId, n);
        Set<Long> randomNumSet = new HashSet<>();
        for (Long randNum : randomNumList) {
            if (!notBeDuplicatedNum.contains(randNum)) {
                randomNumSet.add(randNum);
            }
        }

        List<Dictionary> result = dictionaryService.getDictionaryList(new ArrayList<>(randomNumSet));

        // 원하는 수 만큼의 dictionary 가 뽑히지 않은 경우 (해당 id 값이 존재하지 않는 경우 부족할 수 있음)
        while (result.size() != n) {
            // 1. 부족 수 만큼 랜덤 수 뽑기
            // - 근데 이전에 뽑은 랜덤 수와 중복되면 안댐.
            int insufficientNum = n - result.size();
            List<Long> newRandomNum = NumberUtils.pickRandomNumber(maxId, insufficientNum);
            List<Long> notDuplicate = new ArrayList<>();
            for (Long randomNum : newRandomNum) {
                if (randomNumSet.contains(randomNum)) {
                    continue;
                }

                if (notBeDuplicatedNum.contains(randomNum)) {
                    continue;
                }

                notDuplicate.add(randomNum);
            }

            randomNumSet.addAll(notDuplicate);

            if (notDuplicate.isEmpty()) {
                continue;
            }

            // 중복없이 새로 뽑힌 랜덤수로 다시 랜덤 문제 선정 -> result 에 추가
            result.addAll(dictionaryService.getDictionaryList(notDuplicate));
        }

        return result;
    }

    /**
     * <p>[더미 퀴즈 획득]</p>
     * - 더미 퀴즈에 사용 될 퀴즈 목록 획득
     * @param user
     * @return
     */
    public List<Quiz> getRandomQuizList(Users user) {
        long maxId = dictionaryService.getMaxId();
        Set<Long> scrapedDictionaryIds = scrapService.getDictionaryScrapList(user).stream()
                .map(scrap -> scrap.getDictionary().getId())
                .collect(Collectors.toSet());

        List<DictionaryQuiz> quizAnswerList = getRandomDictionary(maxId, 5, scrapedDictionaryIds).stream()
                .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                .toList();

        List<Quiz> quizList = new ArrayList<>();
        for (DictionaryQuiz answerDictionary : quizAnswerList) {
            QuizChoice[] choices = new QuizChoice[4];

            Set<Long> notBeDuplicatedNum = new HashSet<>();
            notBeDuplicatedNum.add(answerDictionary.getDictionaryId());

            List<DictionaryQuiz> choiceDictionary = new ArrayList<>();
            for (Dictionary randDictionary : getRandomDictionary(maxId, 3, notBeDuplicatedNum)) {
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

        return QuizResultResponse.from(totalGraduation, totalWrong, accuracyRate, resultDetails);
    }


}
