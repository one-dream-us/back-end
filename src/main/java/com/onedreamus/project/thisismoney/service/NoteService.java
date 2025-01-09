package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.exception.KeyNoteException;
import com.onedreamus.project.thisismoney.model.dto.GraduationNoteResponse;
import com.onedreamus.project.thisismoney.model.dto.KeyNoteResponse;
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
                dictionaryKeyNoteRepository.findByDictionary(dictionary);
        if (keyNoteOptional.isPresent()) {
            DictionaryKeyNote keyNote = keyNoteOptional.get();
            if (keyNote.isGraduated()) {
                throw new KeyNoteException(ErrorCode.GRADUATED_ALREADY);
            }else{
                throw new KeyNoteException(ErrorCode.KEYNOTE_ALREADY_EXIST);
            }
        }

        dictionaryKeyNoteRepository.save(DictionaryKeyNote.from(user, dictionary));
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

        Optional<DictionaryWrongAnswerNote> wrongAnswerNoteOptional = dictionaryWrongAnswerNoteRepository.findByDictionary(dictionary);

        DictionaryWrongAnswerNote wrongAnswerNote;
        if (wrongAnswerNoteOptional.isEmpty()) { // 새롭게 틀린 단어인 경우
            wrongAnswerNote = DictionaryWrongAnswerNote.from(dictionary, user);
        } else { // 이전에 틀린적이 있는 단어인 경우
            wrongAnswerNote = wrongAnswerNoteOptional.get();
            // 틀린 횟수 + 1
            wrongAnswerNote.setWrongCnt(wrongAnswerNote.getWrongCnt() + 1);
        }

        dictionaryWrongAnswerNoteRepository.save(wrongAnswerNote);
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

        // 핵심 노트에서 삭제
        Optional<DictionaryKeyNote> keyNoteOptional = dictionaryKeyNoteRepository.findByDictionaryAndIsGraduated(dictionary, false);
        if (keyNoteOptional.isPresent()) {
            DictionaryKeyNote keyNote = keyNoteOptional.get();
            keyNote.setGraduated(true);

            dictionaryKeyNoteRepository.save(keyNote);
        }


        // 졸업노트 추가
        dictionaryGraduationNoteRepository.save(DictionaryGraduationNote.from(user, dictionary));
    }


}
