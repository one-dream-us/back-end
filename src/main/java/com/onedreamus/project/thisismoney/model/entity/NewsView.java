package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;

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
public class NewsView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne
    private News news;

    private Integer viewCount;
    private LocalDateTime viewDate;

    public static NewsView from(News news) {
        return NewsView.builder()
            .news(news)
            .viewCount(0)
            .viewDate(LocalDateTime.now())
            .build();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
