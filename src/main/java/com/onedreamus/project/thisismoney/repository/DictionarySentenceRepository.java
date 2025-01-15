package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionarySentence;
import com.onedreamus.project.thisismoney.model.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionarySentenceRepository extends JpaRepository<DictionarySentence, Integer> {

    Optional<DictionarySentence> findBySentence(Sentence sentence);

    List<Dictionary> findDictionaryBySentenceIn(List<Sentence> sentences);

}
