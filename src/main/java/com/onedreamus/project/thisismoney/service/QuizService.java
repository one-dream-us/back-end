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


        // 2. 2문제 : 아무 단어 중 랜덤 선택
        long totalDictionarySize = dictionaryService.countAll();

        Set<Long> dictionaryIds = quizDictionaries.stream()
                .map(DictionaryQuiz::getDictionaryId)
                .collect(Collectors.toSet());

        // 랜덤으로 2개의 index 선택
        List<Long> randomNumList = NumberUtils.pickRandomNumber(totalDictionarySize, 2, dictionaryIds);;


        // 랜덤으로 전체 단어에서 2개 뽑음
        List<DictionaryQuiz> dictionaries2 =
                dictionaryService.getDictionaryList(randomNumList).stream()
                        .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                        .toList();

        quizDictionaries.addAll(dictionaries2);

        // 3. Quiz 로 변환
        List<Quiz> result = new ArrayList<>();

        // 각 선지 추출
        for (DictionaryQuiz answerDictionary : quizDictionaries) {
            QuizChoice[] choices = new QuizChoice[4];
            randomNumList = NumberUtils.pickRandomNumber(totalDictionarySize, 3);
            List<DictionaryQuiz> choiceDictionary = dictionaryService.getDictionaryList(randomNumList).stream()
                    .map(dictionary -> DictionaryQuiz.from(dictionary, DictionaryStatus.NONE))
                    .toList();
            List<Integer> randomIdx = NumberUtils.shuffleNumber(0, 4);

            for (int i = 0; i < randomIdx.size(); i++) {
                if (i == 0) {
                    choices[randomIdx.get(i)] = QuizChoice.from(answerDictionary);
                    continue;
                }

                choices[randomIdx.get(i)] = QuizChoice.from(choiceDictionary.get(i - 1));
            }

            result.add(Quiz.from(randomIdx.get(0), choices));
        }

        return result;
    }


    /**
     * 퀴즈 결과 처리
     */
    public QuizResultResponse processQuizResult(Users user, List<QuizResult> quizResults) {


        return null;
    }
}
