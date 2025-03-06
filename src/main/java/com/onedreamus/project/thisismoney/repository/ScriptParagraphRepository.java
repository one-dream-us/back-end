//package com.onedreamus.project.thisismoney.repository;
//
//import com.onedreamus.project.thisismoney.model.entity.ScriptParagraph;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ScriptParagraphRepository extends JpaRepository<ScriptParagraph, Integer> {
//	@Query("SELECT sp FROM ScriptParagraph sp " +
//		"JOIN sp.scriptFull sf " +
//		"WHERE sf.content.id = :contentId " +
//		"ORDER BY sp.timestamp")
//	List<ScriptParagraph> findByContentIdOrderByTimestamp(@Param("contentId") Long contentId);
//
//}
