package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryBookmark;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryBookmarkRepository extends JpaRepository<DictionaryBookmark, Long> {
    Optional<DictionaryBookmark> findByDictionary(Dictionary dictionary);

    Optional<DictionaryBookmark> findByDictionaryAndIsGraduatedFalse(Dictionary dictionary);

    Optional<DictionaryBookmark> findByDictionaryAndIsGraduated(Dictionary dictionary, boolean isGraduated);

    List<DictionaryBookmark> findByUserAndIsGraduatedOrderByCreatedAtDesc(Users user, boolean isGraduated);

    List<DictionaryBookmark> findByUserAndIsGraduated(Users user, boolean b);

    int countByUserAndIsGraduated(Users user, boolean isGraduated);

    Optional<DictionaryBookmark> findByUserAndDictionary(Users user, Dictionary dictionary);

    Optional<DictionaryBookmark> findByUserAndDictionaryAndIsGraduated(Users user, Dictionary dictionary, boolean isGraduated);

    Boolean existsByUserAndCorrectCntGreaterThanEqual(Users user, int i);
}
