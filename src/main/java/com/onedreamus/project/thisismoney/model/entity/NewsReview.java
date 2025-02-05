package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NewsReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    private String value;
    private Integer score; // 리뷰 점수
    private LocalDate createdAt;

    public static NewsReview from(News news, int score, String value) {
        return NewsReview.builder()
                .news(news)
                .value(value)
                .score(score)
                .createdAt(LocalDate.now())
                .build();
    }

}
