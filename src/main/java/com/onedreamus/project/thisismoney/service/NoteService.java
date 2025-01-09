package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.exception.KeyNoteException;
import com.onedreamus.project.thisismoney.exception.UserException;
import com.onedreamus.project.thisismoney.model.dto.GraduationNoteResponse;
import com.onedreamus.project.thisismoney.model.dto.KeyNoteResponse;
import com.onedreamus.project.thisismoney.model.dto.LearningStatus;
import com.onedreamus.project.thisismoney.model.dto.WrongAnswerNoteResponse;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.repository.DictionaryGraduationNoteRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryKeyNoteRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryWrongAnswerNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {

    private final DictionaryKeyNoteRepository dictionaryKeyNoteRepository;
    private final DictionaryWrongAnswerNoteRepository dictionaryWrongAnswerNoteRepository;
    private final DictionaryGraduationNoteRepository dictionaryGraduationNoteRepository;
    private final DictionaryService dictionaryService;
    private final ScrapService scrapService;

    /**
     * 핵심노트 조회
     */
    public KeyNoteResponse getKeyNoteList(Users user) {
        List<DictionaryKeyNote> keyNotes =
                dictionaryKeyNoteRepository.findByUserAndIsGraduatedOrderByCreatedAtDesc(user, false);
        return KeyNoteResponse.from(keyNotes);
    }

    /**
     * 핵심노트 추가
     */
    public void addKeyNote(Long dictionaryId, Users user) {
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
                .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));

        Optional<DictionaryKeyNote> keyNoteOptional =
                dictionaryKeyNoteRepository.findByUserAndDictionary(user, dictionary);
        if (keyNoteOptional.isPresent()) {
            DictionaryKeyNote keyNote = keyNoteOptional.get();
            if (keyNote.isGraduated()) {
                throw new KeyNoteException(ErrorCode.GRADUATED_ALREADY);
            } else {
                throw new KeyNoteException(ErrorCode.KEYNOTE_ALREADY_EXIST);
            }
        }

        dictionaryKeyNoteRepository.save(DictionaryKeyNote.from(user, dictionary));

        // 스크랩에서 삭제
        scrapService.deleteDictionaryScrapped(dictionaryId, user);
    }

    /**
     * 핵심노트에서 삭제
     */
    public void deleteKeyNote(Long keyNoteId, Users user) {
        DictionaryKeyNote keyNote = dictionaryKeyNoteRepository.findById(keyNoteId)
                .orElseThrow(() -> new KeyNoteException(ErrorCode.KEYNOTE_NOT_EXIST));

        if (!keyNote.getUser().getId().equals(user.getId())) {
            throw new UserException(ErrorCode.USER_NOT_MATCH);
        }

        dictionaryKeyNoteRepository.delete(keyNote);
    }

    /**
     * 오답노트 조회
     */
    public WrongAnswerNoteResponse getWrongAnswerList(Users user) {
        List<DictionaryWrongAnswerNote> wrongAnswerNotes =
                dictionaryWrongAnswerNoteRepository.findByUserAndIsGraduated(user, false);
        return WrongAnswerNoteResponse.from(wrongAnswerNotes);
    }

    /**
     * 오답노트 추가
     */
    public void addWrongNote(Long dictionaryId, Users user) {
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
                .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));

        Optional<DictionaryWrongAnswerNote> wrongAnswerNoteOptional =
                dictionaryWrongAnswerNoteRepository.findByUserAndDictionary(user, dictionary);

        DictionaryWrongAnswerNote wrongAnswerNote;
        if (wrongAnswerNoteOptional.isEmpty()) { // 새롭게 틀린 단어인 경우
            wrongAnswerNote = DictionaryWrongAnswerNote.from(dictionary, user);
        } else { // 이전에 틀린적이 있는 단어인 경우
            wrongAnswerNote = wrongAnswerNoteOptional.get();
            // 틀린 횟수 + 1
            wrongAnswerNote.setWrongCnt(wrongAnswerNote.getWrongCnt() + 1);
        }

        dictionaryWrongAnswerNoteRepository.save(wrongAnswerNote);

        // 핵심노트에서 삭제
        DictionaryKeyNote keyNote = dictionaryKeyNoteRepository.findByUserAndDictionary(user, dictionary)
                .orElseThrow(() -> new KeyNoteException(ErrorCode.KEYNOTE_NOT_EXIST));
        deleteKeyNote(keyNote.getId(), user);
    }

    /**
     * 졸업노트 조회
     */
    public GraduationNoteResponse getGraduationNoteList(Users user) {
        List<DictionaryGraduationNote> graduationNotes = dictionaryGraduationNoteRepository.findByUser(user);
        return GraduationNoteResponse.from(graduationNotes);
    }


    /**
     * 졸업노트 추가
     */
    public void addGraduateNote(Dictionary dictionary, Users user) {

        // 오답 노트에서 삭제
        Optional<DictionaryWrongAnswerNote> wrongAnswerNoteOptional = dictionaryWrongAnswerNoteRepository.findByDictionary(dictionary);
        if (wrongAnswerNoteOptional.isPresent()) {
            DictionaryWrongAnswerNote wrongAnswerNote = wrongAnswerNoteOptional.get();
            wrongAnswerNote.setGraduated(true);
            dictionaryWrongAnswerNoteRepository.save(wrongAnswerNote);
        }

        // 졸업노트 추가
        dictionaryGraduationNoteRepository.save(DictionaryGraduationNote.from(user, dictionary));
    }

    /**
     * 학습상태 창 정보 조회
     */
    public LearningStatus getLearningStatus(Users user) {
        int keyNoteCnt = dictionaryKeyNoteRepository.countByUserAndIsGraduated(user, false);
        int totalScrapCnt = scrapService.getDictionaryScrapCnt(user).getDictionaryScrapCnt();
        int graduationCnt = dictionaryGraduationNoteRepository.countByUser(user);
        int accuracyRate = 0;

        return LearningStatus.from(user.getName(), totalScrapCnt, graduationCnt, keyNoteCnt, accuracyRate);
    }

}
