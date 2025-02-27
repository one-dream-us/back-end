package com.onedreamus.project.thisismoney.model.entity;

import com.onedreamus.project.thisismoney.model.converter.NewsRequestConverter;
import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DraftNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = NewsRequestConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)  // Hibernate 6에 json 타입임을 알림
    private NewsContent newsContent;

    private LocalDate scheduledAt;
    private LocalDateTime createdAt;

    public static DraftNews from(NewsContent newsContent, LocalDate scheduledAt) {
        return DraftNews.builder()
                .newsContent(newsContent)
                .scheduledAt(scheduledAt)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
