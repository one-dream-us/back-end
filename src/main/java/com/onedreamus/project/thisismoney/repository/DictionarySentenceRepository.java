package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionarySentence;
import com.onedreamus.project.thisismoney.model.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionarySentenceRepository extends JpaRepository<DictionarySentence, Integer> {

    Optional<DictionarySentence> findBySentence(Sentence sentence);

    @Query("SELECT ds.dictionary FROM DictionarySentence ds WHERE ds.sentence IN :sentences")
    List<Dictionary> findDictionaryBySentenceIn(@Param("sentences") List<Sentence> sentences);

}
