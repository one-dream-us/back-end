package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DictionaryKeyNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryKeyNoteRepository extends JpaRepository<DictionaryKeyNote, Long> {
}
