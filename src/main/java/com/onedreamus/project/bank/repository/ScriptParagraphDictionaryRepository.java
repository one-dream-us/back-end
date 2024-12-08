package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.dto.DictionaryContentDto;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.Dictionary;
import com.onedreamus.project.bank.model.entity.ScriptParagraphDictionary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptParagraphDictionaryRepository extends JpaRepository<ScriptParagraphDictionary, Long> {
	@Query("SELECT m FROM ScriptParagraphDictionary m " +
		"JOIN FETCH m.dictionary d " +
		"WHERE m.scriptParagraph.id = :paragraphId")
	List<ScriptParagraphDictionary> findByScriptParagraphIdWithDictionary(@Param("paragraphId") Long paragraphId);

}
