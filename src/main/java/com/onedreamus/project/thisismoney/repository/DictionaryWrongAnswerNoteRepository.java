package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DictionaryWrongAnswerNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryWrongAnswerNoteRepository extends JpaRepository<DictionaryWrongAnswerNote, Long> {
}
