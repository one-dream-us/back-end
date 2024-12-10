package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.dto.ContentScrapSummaryDto;
import com.onedreamus.project.thisismoney.model.dto.ContentScrapTagDto;
import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentScrap;
import com.onedreamus.project.thisismoney.model.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentScrapRepository extends JpaRepository<ContentScrap, Integer> {

    List<ContentScrap> findAllByUser(Users user);

    Optional<ContentScrap> findByUserAndContentAndIsDeleted(Users user, Content content, boolean isDeleted);
    @Query("SELECT COUNT(cs) FROM ContentScrap cs WHERE cs.content.id = :contentId")
    Integer countByContentId(@Param("contentId") Long contentId);

    Integer countByUser(Users user);

    boolean existsByIdAndUser(Integer contentScrapId, Users user);

    Optional<ContentScrap> findByIdAndUser(Integer contentScrapId, Users user);

    @Query("SELECT new com.onedreamus.project.thisismoney.model.dto.ContentScrapSummaryDto("
        + "cs.id as scrapId, cs.createdAt, cs.content.id as contentId, cs.content.thumbnailUrl, cs.content.title as contentTitle, ss.summaryText) "
        + "FROM ContentScrap cs "
        + "LEFT JOIN ScriptSummary ss ON ss.content = cs.content "
        + "WHERE cs.user = :user AND cs.isDeleted = false")
    List<ContentScrapSummaryDto> findContentScrapSummaryByUser(@Param("user") Users user);

    @Query("SELECT new com.onedreamus.project.thisismoney.model.dto.ContentScrapTagDto(" +
        "cs.id as scrapId, " +
        "cs.content.id as contentId, " +
        "t.value as tagValue) " +
        "FROM ContentScrap cs " +
        "LEFT JOIN ContentTag ct ON ct.content = cs.content " +
        "LEFT JOIN Tag t ON t = ct.tag " +
        "WHERE cs.user = :user AND cs.isDeleted = false")
    List<ContentScrapTagDto> findContentScrapTagByUser(@Param("user") Users user);

    Integer countByUserAndIsDeleted(Users user, boolean isDeleted);
}
