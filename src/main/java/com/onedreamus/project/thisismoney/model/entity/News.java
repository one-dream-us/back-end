package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    @Column(length = 400)
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
    private String newsAgency;
    private String link;

    public static News from(String title, String thumbnailUrl, String newsAgency,
        String originalLink) {
        return News.builder()
            .title(title)
            .thumbnailUrl(thumbnailUrl)
            .newsAgency(newsAgency)
            .link(originalLink)
            .isDeleted(false)
            .createdAt(LocalDateTime.now())
            .build();
    }

}
