package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTagRepository extends JpaRepository<ContentTag, Integer> {
	List<ContentTag> findByContentOrderBySequence(Content content);
}
