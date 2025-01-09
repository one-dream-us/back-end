package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DictionaryGraduationNote;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryGraduationNoteRepository extends JpaRepository<DictionaryGraduationNote, Long> {
    List<DictionaryGraduationNote> findByUser(Users user);
}
