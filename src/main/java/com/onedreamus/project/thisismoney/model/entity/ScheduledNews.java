package com.onedreamus.project.thisismoney.model.entity;

import com.onedreamus.project.thisismoney.model.converter.NewsRequestConverter;
import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import com.onedreamus.project.thisismoney.model.dto.ScheduledNewsRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScheduledNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = NewsRequestConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)  // Hibernate 6에 json 타입임을 알림
    private ScheduledNewsRequest scheduledNewsRequest;

    private LocalDate scheduledAt; // 업로드 날짜.

    public static ScheduledNews from(ScheduledNewsRequest scheduledNewsRequest, LocalDate scheduledAt, String thumbnailUrl) {
        return ScheduledNews.builder()
            .scheduledNewsRequest(scheduledNewsRequest)
            .scheduledAt(scheduledAt)
            .build();
    }
}
