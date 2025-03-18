package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.dto.DictionaryNewsDto;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryHistory;
import com.onedreamus.project.thisismoney.model.entity.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryHistoryRepository extends JpaRepository<DictionaryHistory, Long> {

    List<DictionaryHistory> findAllByUser(Users user);

    Optional<DictionaryHistory> findByUserAndDictionaryAndIsDeleted(Users user, Dictionary dictionary, boolean isDeleted);

    @Query("SELECT new  com.onedreamus.project.thisismoney.model.dto.DictionaryNewsDto(dh.id, d.id, d.term, d.definition, d.description, dh.isBookmarked) "
            + "FROM DictionaryHistory dh "
            + "JOIN Dictionary d on d = dh.dictionary "
            + "WHERE dh.user = :user AND dh.isDeleted = false "
            + "ORDER BY dh.createdAt DESC")
    List<DictionaryNewsDto> findDictionaryHistoryByUser(@Param("user") Users user);

    List<DictionaryHistory> findByUserAndIsDeletedFalse(Users user);

    Boolean existsByUserAndDictionaryAndIsDeleted(Users user, Dictionary dictionary, boolean isDeleted);

    @Query("select dh.id from DictionaryHistory dh " +
            "where dh.user = :user")
    List<Long> findIdByUser(@Param("user") Users user);

    @Query("SELECT dh.dictionary.id FROM DictionaryHistory dh WHERE dh.user = :user AND dh.dictionary.id IN :dictionaryIds")
    List<Long> findDictionaryIdsByUserAndDictionaryIds(@Param("user") Users user, @Param("dictionaryIds") List<Long> dictionaryIds);
}
