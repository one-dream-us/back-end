package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DictionarySentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionarySentenceRepository extends JpaRepository<DictionarySentence, Integer> {

}
