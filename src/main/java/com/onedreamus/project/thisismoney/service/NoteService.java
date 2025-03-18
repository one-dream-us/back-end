package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.exception.BookmarkException;
import com.onedreamus.project.thisismoney.exception.UserException;
import com.onedreamus.project.thisismoney.exception.WrongAnswerException;
import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.repository.DictionaryGraduationNoteRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryBookmarkRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryWrongAnswerNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {

    private final DictionaryBookmarkRepository dictionaryBookmarkRepository;
    private final DictionaryWrongAnswerNoteRepository dictionaryWrongAnswerNoteRepository;
    private final DictionaryGraduationNoteRepository dictionaryGraduationNoteRepository;
    private final DictionaryService dictionaryService;
    private final HistoryService historyService;

    /**
     * 핵심노트 조회
     */
    public BookmarkResponse getBookmarkList(Users user) {
        List<DictionaryBookmark> bookmarks =
            dictionaryBookmarkRepository.findByUserAndIsGraduatedOrderByCreatedAtDesc(user, false);
        return BookmarkResponse.from(bookmarks);
    }

    /**
     * <p>[북마크 추가]</p>
     *
     * @param dictionaryId
     * @param user
     */
    @Transactional
    public void addBookmark(Long dictionaryId, Users user) {
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
            .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));

        dictionaryBookmarkRepository.findByUserAndDictionary(user, dictionary)
            .ifPresent(bookmark -> {
                if (bookmark.isGraduated()) {
                    throw new BookmarkException(ErrorCode.GRADUATED_ALREADY);
                } else {
                    throw new BookmarkException(ErrorCode.BOOKMARK_ALREADY_EXIST);
                }
            });

        dictionaryBookmarkRepository.save(DictionaryBookmark.from(user, dictionary));

        // 히스토리에서 북마크 상태 값 수정 (false -> true)
        historyService.changeBookmarkStatus(dictionary, user, true);
    }

    /**
     * 북마크에서 삭제
     */
    @Transactional
    public void deleteBookmark(Long bookmarkId, Users user) {
        DictionaryBookmark bookmark = dictionaryBookmarkRepository.findById(bookmarkId)
            .orElseThrow(() -> new BookmarkException(ErrorCode.BOOKMARK_NOT_EXIST));

        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new UserException(ErrorCode.USER_NOT_MATCH);
        }

        dictionaryBookmarkRepository.delete(bookmark);

        // 히스토리에서 북마크 상태 값 수정 (true -> false)
        historyService.changeBookmarkStatus(bookmark.getDictionary(), user, false);
    }

    /**
     * 오답노트 조회
     */
    public WrongAnswerNoteResponse getWrongAnswerList(Users user) {
        List<DictionaryWrongAnswerNote> wrongAnswerNotes =
            dictionaryWrongAnswerNoteRepository.findByUserAndIsGraduatedOrderByCreatedAtDesc(user,
                false);
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
        DictionaryBookmark bookmark = dictionaryBookmarkRepository.findByUserAndDictionary(user,
                dictionary)
            .orElseThrow(() -> new BookmarkException(ErrorCode.BOOKMARK_NOT_EXIST));
        deleteBookmark(bookmark.getId(), user);
    }

    /**
     * 졸업노트 조회
     */
    public GraduationNoteResponse getGraduationNoteList(Users user) {
        List<DictionaryGraduationNote> graduationNotes =
            dictionaryGraduationNoteRepository.findByUserOrderByCreatedAtDesc(user);
        return GraduationNoteResponse.from(graduationNotes);
    }


    public List<DictionaryGraduationNote> getAllGraduationNote(Users user) {
        return dictionaryGraduationNoteRepository.findByUser(user);
    }

    /**
     * <p>졸업 노트 ID 리스트 획득</p>
     * user 의 졸업 노트 ID 리스트 획득
     *
     * @param user
     * @return
     */
    public List<Long> getAllGraduationNoteIds(Users user) {
        return dictionaryGraduationNoteRepository.findIdByUser(user);
    }


    /**
     * 졸업노트 추가
     */
    public void addGraduateNote(Dictionary dictionary, Users user) {

        // 오답 노트에서 삭제
        Optional<DictionaryWrongAnswerNote> wrongAnswerNoteOptional = dictionaryWrongAnswerNoteRepository.findByDictionary(
            dictionary);
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
        int bookmarkCnt = dictionaryBookmarkRepository.countByUserAndIsGraduated(user, false);
        int graduationCnt = dictionaryGraduationNoteRepository.countByUser(user);
        int wrongAnswerCnt = dictionaryWrongAnswerNoteRepository.countByUserAndIsGraduated(user,
            false);
        int accuracyRate = 0;

        return LearningStatus.from(user.getName(), graduationCnt, bookmarkCnt, accuracyRate,
            wrongAnswerCnt);
    }

    /**
     * 핵심 노트 조회
     */
    public List<DictionaryBookmark> getBookmarks(Users user) {
        return dictionaryBookmarkRepository.findByUserAndIsGraduated(user, false);
    }

    /**
     * <p>[퀴즈에서 맞춘 용어 유무 확인]</p>
     * 퀴즈에 출제되어 정답 처리 된 용어가 존재하는지 조회
     *
     * @param user
     * @return
     */
    public boolean doesCorrectBookmarkExist(Users user) {
        return dictionaryBookmarkRepository.existsByUserAndCorrectCntGreaterThanEqual(user, 1);
    }

    /**
     * 오답 노트 조회
     */
    public List<DictionaryWrongAnswerNote> getWrongAnswerNotes(Users user) {
        return dictionaryWrongAnswerNoteRepository.findByUserAndIsGraduated(user, false);
    }

    /**
     * <p>[용어 위치 이동]</p>
     * => 문제를 맞추거나 틀릴 경우 용어의 위치 및 상태 수정
     * <p>EX)</p>
     * <ul>
     *     <li> 오답노트에 있는 단어를 또 틀렸을 경우 틀린 회수  + 1</li>
     *     <li> 용어를 3회 이상 맞출 경우 졸업노트로 이동</li>
     * </ul>
     *
     * @param quizResult
     * @param user
     */
    public DictionaryStatusDto changeStatus(QuizResult quizResult, Users user) {
        Dictionary dictionary = dictionaryService.getDictionaryById(quizResult.getDictionaryId())
            .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));
        switch (quizResult.getStatus()) {
            case NONE -> {
                return changeNone(quizResult, user, dictionary);
            }
            case BOOKMARK -> {
                return changeBookmark(quizResult, user, dictionary);
            }
            case WRONG_ANSWER_NOTE -> {
                return changeWrongAnswerNote(quizResult, user, dictionary);
            }
        }

        throw new DictionaryException(ErrorCode.NO_APPROPRIATE_STATUS);
    }

    private DictionaryStatusDto changeNone(QuizResult quizResult, Users user,
        Dictionary dictionary) {

        historyService.add(dictionary, user);
        quizResult.setStatus(DictionaryStatus.SCRAP);

        return DictionaryStatusDto.from(quizResult, dictionary.getTerm(), 0, 0);
    }

    private DictionaryStatusDto changeBookmark(QuizResult quizResult, Users user,
        Dictionary dictionary) {
        DictionaryBookmark bookmark =
            dictionaryBookmarkRepository.findByUserAndDictionaryAndIsGraduated(user, dictionary,
                    false)
                .orElseThrow(() -> new BookmarkException(ErrorCode.BOOKMARK_NOT_EXIST));

        // 맞춘 경우
        if (quizResult.isCorrect()) {
            bookmark.setCorrectCnt(bookmark.getCorrectCnt() + 1);

            // 총 정답 수 3번 이상인 경우 -> 졸업노트 이동
            if (bookmark.getCorrectCnt() >= 3) {
                quizResult.setStatus(DictionaryStatus.GRADUATION_NOTE);
                bookmark.setGraduated(true);
                dictionaryGraduationNoteRepository.save(
                    DictionaryGraduationNote.from(user, dictionary));
            }

            dictionaryBookmarkRepository.save(bookmark);
        } else {// 틀린 경우
            bookmark.setGraduated(true);
            DictionaryWrongAnswerNote wrongAnswerNote = DictionaryWrongAnswerNote.from(dictionary,
                user);
            wrongAnswerNote.setCorrectCnt(bookmark.getCorrectCnt());
            quizResult.setStatus(DictionaryStatus.WRONG_ANSWER_NOTE);
            dictionaryWrongAnswerNoteRepository.save(wrongAnswerNote);
        }

        return DictionaryStatusDto.from(quizResult, dictionary.getTerm(), bookmark.getCorrectCnt(),
            1);
    }

    private DictionaryStatusDto changeWrongAnswerNote(QuizResult quizResult, Users user,
        Dictionary dictionary) {
        DictionaryWrongAnswerNote wrongAnswerNote =
            dictionaryWrongAnswerNoteRepository.findByUserAndDictionaryAndIsGraduated(user,
                    dictionary, false)
                .orElseThrow(() -> new WrongAnswerException(ErrorCode.WRONG_ANSWER_NOTE_NOT_EXIST));

        // 맞춘 경우
        if (quizResult.isCorrect()) {
            wrongAnswerNote.setCorrectCnt(wrongAnswerNote.getCorrectCnt() + 1);
            if (wrongAnswerNote.getCorrectCnt() >= 3) {
                wrongAnswerNote.setGraduated(true);
                quizResult.setStatus(DictionaryStatus.GRADUATION_NOTE);
                dictionaryGraduationNoteRepository.save(
                    DictionaryGraduationNote.from(user, dictionary));
            }

        } else {
            // 틀린 경우
            wrongAnswerNote.setWrongCnt(wrongAnswerNote.getWrongCnt() + 1);
        }

        dictionaryWrongAnswerNoteRepository.save(wrongAnswerNote);

        return DictionaryStatusDto.from(quizResult, dictionary.getTerm(),
            wrongAnswerNote.getCorrectCnt(), wrongAnswerNote.getWrongCnt());
    }

    /**
     * <p>[오답노트 유무 확인]</p>
     * User 를 통해 오답노트(WrongAnswerNote)가 존재하는지 확인
     *
     * @param user
     * @return
     */
    public boolean doesWrongAnswerNoteExist(Users user) {
        return dictionaryWrongAnswerNoteRepository.existsByUser(user);
    }
}
