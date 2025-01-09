package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.repository.DictionaryGraduationNoteRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryKeyNoteRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryWrongAnswerNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final DictionaryKeyNoteRepository dictionaryKeyNoteRepository;
    private final DictionaryWrongAnswerNoteRepository dictionaryWrongAnswerNoteRepository;
    private final DictionaryGraduationNoteRepository dictionaryGraduationNoteRepository;

}
