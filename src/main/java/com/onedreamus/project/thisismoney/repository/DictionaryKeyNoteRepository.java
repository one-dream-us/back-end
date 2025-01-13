package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryKeyNote;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryKeyNoteRepository extends JpaRepository<DictionaryKeyNote, Long> {
    Optional<DictionaryKeyNote> findByDictionary(Dictionary dictionary);

    Optional<DictionaryKeyNote> findByDictionaryAndIsGraduatedFalse(Dictionary dictionary);

    Optional<DictionaryKeyNote> findByDictionaryAndIsGraduated(Dictionary dictionary, boolean isGraduated);

    List<DictionaryKeyNote> findByUserAndIsGraduatedOrderByCreatedAtDesc(Users user, boolean isGraduated);

    List<DictionaryKeyNote> findByUserAndIsGraduated(Users user, boolean b);

    int countByUserAndIsGraduated(Users user, boolean isGraduated);

    Optional<DictionaryKeyNote> findByUserAndDictionary(Users user, Dictionary dictionary);

    Optional<DictionaryKeyNote> findByUserAndDictionaryAndIsGraduated(Users user, Dictionary dictionary, boolean isGraduated);

    Boolean existsByUserAndCorrectCntGreaterThanEqual(Users user, int i);
}
