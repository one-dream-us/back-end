package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.dto.DictionaryContentDto;
import com.onedreamus.project.thisismoney.model.dto.DictionaryNewsDto;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryScrap;
import com.onedreamus.project.thisismoney.model.entity.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryScrapRepository extends JpaRepository<DictionaryScrap, Long> {

    List<DictionaryScrap> findAllByUser(Users user);

    Optional<DictionaryScrap> findByUserAndDictionaryAndIsDeleted(Users user, Dictionary dictionary, boolean isDeleted);

    Integer countByUserAndIsDeleted(Users user, boolean isDeleted);

    boolean existsByIdAndUser(Long dictionaryScrapId, Users user);

    @Query("SELECT new  com.onedreamus.project.thisismoney.model.dto.DictionaryContentDto(ds.id, d.id, d.term, d.definition, dsc.content.id) "
            + "FROM DictionaryScrap ds "
            + "JOIN Dictionary d on d = ds.dictionary "
            + "JOIN DictionaryScrapContent dsc ON dsc.dictionaryScrap = ds "
            + "WHERE ds.user = :user AND ds.isDeleted = false "
            + "ORDER BY ds.createdAt DESC")
    List<DictionaryContentDto> findDictionaryScrapWithContentByUser(@Param("user") Users user);

    @Query("SELECT new  com.onedreamus.project.thisismoney.model.dto.DictionaryNewsDto(ds.id, d.id, d.term, d.definition, d.description) "
            + "FROM DictionaryScrap ds "
            + "JOIN Dictionary d on d = ds.dictionary "
            + "WHERE ds.user = :user AND ds.isDeleted = false "
            + "ORDER BY ds.createdAt DESC")
    List<DictionaryNewsDto> findDictionaryScrapByUser(@Param("user") Users user);

    Optional<DictionaryScrap> findByIdAndUser(Long dictionaryScrapId, Users user);

    @Query("SELECT ds FROM DictionaryScrap ds WHERE ds.user = :user AND ds.dictionary = :dictionary AND ds.isDeleted = false")
    Optional<DictionaryScrap> findByUserAndDictionaryAndIsDeletedFalse(
            @Param("user") Users user,
            @Param("dictionary") Dictionary dictionary
    );

    List<DictionaryScrap> findByUserAndIsDeletedFalse(Users user);

    Boolean existsByUserAndDictionaryAndIsDeleted(Users user, Dictionary dictionary, boolean isDeleted);

    Optional<DictionaryScrap> findByIdAndUserAndIsDeleted(Long dictionaryScrapId, Users user, boolean b);

    @Query("select ds.id from DictionaryScrap ds " +
            "where ds.user = :user")
    List<Long> findIdByUser(@Param("user") Users user);
}
