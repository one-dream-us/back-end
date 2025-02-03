package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryWrongAnswerNote;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryWrongAnswerNoteRepository extends JpaRepository<DictionaryWrongAnswerNote, Long> {
    Optional<DictionaryWrongAnswerNote> findByDictionary(Dictionary dictionary);

    List<DictionaryWrongAnswerNote> findByUserAndIsGraduated(Users user, boolean isGraduated);

    List<DictionaryWrongAnswerNote> findByUserAndIsGraduatedOrderByCreatedAtDesc(Users user, boolean isGraduated);

    Optional<DictionaryWrongAnswerNote> findByUserAndDictionary(Users user, Dictionary dictionary);

    Optional<DictionaryWrongAnswerNote> findByUserAndDictionaryAndIsGraduated(Users user, Dictionary dictionary, boolean isGraduated);

    boolean existsByUser(Users user);

    int countByUserAndIsGraduated(Users user, boolean isGraduated);

}
