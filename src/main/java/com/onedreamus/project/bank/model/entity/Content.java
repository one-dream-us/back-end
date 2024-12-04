package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "content")
@Entity(name="Content")
public class Content{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name="content_url")
    private String contentUrl;
    @Column(name="thumbnail_url")
    private String thumbnailUrl;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="author")
    private String author;

}
